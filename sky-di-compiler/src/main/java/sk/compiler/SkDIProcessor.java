package sk.compiler;

import static com.google.auto.common.MoreElements.getPackage;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;
import static sk.compiler.SKUtils.bestGuess;
import static sk.compiler.SKUtils.lowerCase;
import static sk.compiler.SkConsts.SK_INTERFACE;
import static sk.compiler.SkConsts.SK_I_LAZY;
import static sk.compiler.SkConsts.SK_I_PROVIDER;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import org.apache.commons.collections4.CollectionUtils;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.sun.source.util.Trees;

import sk.compiler.model.SKCheckProviderModel;
import sk.compiler.model.SKInputClassModel;
import sk.compiler.model.SKInputModel;
import sk.compiler.model.SKParamProviderModel;
import sk.compiler.model.SKProviderModel;
import sk.compiler.model.SKSourceModel;
import sky.SKInput;
import sky.SKProvider;
import sky.SKSingleton;
import sky.SKSource;

@AutoService(Processor.class)
public final class SkDIProcessor extends AbstractProcessor {

	private static final String	OPTION_SDK_INT	= "sk.minSdk";

	private int					sdk				= 1;

	private Elements			elementUtils;

	private Types				typeUtils;

	private Filer				filer;

	private Trees				trees;

	private SkLogger			logger;

	@Override public synchronized void init(ProcessingEnvironment env) {
		super.init(env);

		String sdk = env.getOptions().get(OPTION_SDK_INT);
		if (sdk != null) {
			try {
				this.sdk = Integer.parseInt(sdk);
			} catch (NumberFormatException e) {
				env.getMessager().printMessage(Diagnostic.Kind.WARNING, "Unable to parse supplied minSdk option '" + sdk + "'. Falling back to API 1 support.");
			}
		}

		elementUtils = env.getElementUtils();
		typeUtils = env.getTypeUtils();
		filer = env.getFiler();

		logger = new SkLogger(processingEnv.getMessager()); // Package the log utils.

		logger.info(">>> SkProcessor 初始化. <<<");

		try {
			trees = Trees.instance(processingEnv);
		} catch (IllegalArgumentException ignored) {
			logger.error(ignored);
		}
	}

	@Override public Set<String> getSupportedOptions() {
		return Collections.singleton(OPTION_SDK_INT);
	}

	@Override public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override public Set<String> getSupportedAnnotationTypes() {
		Set<String> types = new LinkedHashSet<>();
		for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
			types.add(annotation.getCanonicalName());
		}
		return types;
	}

	private Set<Class<? extends Annotation>> getSupportedAnnotations() {
		Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
		annotations.add(SKInput.class);
		annotations.add(SKProvider.class);
		annotations.add(SKSingleton.class);
		annotations.add(SKSource.class);
		return annotations;
	}

	/**
	 * 主流程
	 */
	@Override public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
		// 如果没有注解
		if (CollectionUtils.isEmpty(elements)) {
			logger.info(">>> SkProcessor 没有注解. <<<");
			return false;
		}

		logger.info(">>> Found SKProvider, 开始... <<<");
		Map<String, SKSourceModel> skSourceModelMap = new LinkedHashMap<>();
		Map<String, SKProviderModel> skProviderModels = findMethodsProvider(env, SKProvider.class, skSourceModelMap);
		if (skProviderModels == null) {
			logger.error(">>> Found SKProvider 异常... <<<");
			return false;
		}
		SKProviderCreate skProviderCreate = new SKProviderCreate();

		for (SKProviderModel item : skProviderModels.values()) {
			JavaFile javaIFile = skProviderCreate.brewProvider(item);
			try {
				javaIFile.writeTo(filer);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		logger.info(">>> Found SKProvider 结束... <<<");

		logger.info(">>> Found SKInput, 开始... <<<");

		Map<TypeElement, SKInputClassModel> skInputModels = findFieldInput(env, SKInput.class, skProviderModels);

		SKInputCreate skInputCreate = new SKInputCreate();

		for (SKInputClassModel item : skInputModels.values()) {
			JavaFile javaIFile = skInputCreate.brewInput(item);
			try {
				javaIFile.writeTo(filer);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		logger.info(">>> Found SKInput 结束... <<<");

		logger.info(">>> Found SKDI, 开始... <<<");

		SKDICreate skdiCreate = new SKDICreate();

		JavaFile javaIFile = skdiCreate.brewDI(skProviderModels, skInputModels, skSourceModelMap);
		try {
			javaIFile.writeTo(filer);
		} catch (IOException e) {
			logger.error(e);
		}

		logger.info(">>> Found SKDI, 结束... <<<");

		return false;
	}

	private Map<TypeElement, SKInputClassModel> findFieldInput(RoundEnvironment env, Class<? extends Annotation> annotationClass, Map<String, SKProviderModel> skProviderModels) {

		Map<TypeElement, SKInputClassModel> modelMap = new LinkedHashMap<>();

		for (Element element : env.getElementsAnnotatedWith(annotationClass)) {
			if (!SuperficialValidation.validateElement(element)) continue;
			try {
				parseInputAnnotation(element, modelMap, skProviderModels);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("生成input异常");
			}
		}

		return modelMap;
	}

	private void parseInputAnnotation(Element element, Map<TypeElement, SKInputClassModel> inputClassModelMap, Map<String, SKProviderModel> skProviderModels) {
		boolean hasError = isInaccessibleViaGeneratedCode(element);

		if (hasError) {
			return;
		}

		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

		String packageName = getPackage(enclosingElement).getQualifiedName().toString();

		SKInputModel skInputModel = new SKInputModel();
		skInputModel.fieldName = element.getSimpleName().toString();
		skInputModel.type = TypeName.get(element.asType());
		skInputModel.className = ClassName.get(packageName, enclosingElement.getSimpleName().toString());
		skInputModel.packageName = packageName;
		skInputModel.typeMirror = element.asType();

		skInputModel.build(SK_I_LAZY);

		skInputModel.skProviderModel = skProviderModels.get(skInputModel.providerKey);

		// 查找是否需要自动注入
		findParentInterface(skInputModel);

		SKInputClassModel skInputClassModel = getOrCreateInputClassModel(inputClassModelMap, enclosingElement);
		skInputClassModel.className = ClassName.get(packageName, enclosingElement.getSimpleName().toString());
		skInputClassModel.packageName = packageName;
		skInputClassModel.skInputModels.add(skInputModel);
	}

	private SKInputClassModel getOrCreateInputClassModel(Map<TypeElement, SKInputClassModel> inputClassModelMap, TypeElement enclosingElement) {
		SKInputClassModel skInputClassModel = inputClassModelMap.get(enclosingElement);
		if (skInputClassModel == null) {
			skInputClassModel = new SKInputClassModel();
			skInputClassModel.skInputModels = new ArrayList<>();
			inputClassModelMap.put(enclosingElement, skInputClassModel);
		}
		return skInputClassModel;
	}

	private Map<String, SKProviderModel> findMethodsProvider(RoundEnvironment env, Class<? extends Annotation> annotationClass, Map<String, SKSourceModel> skSourceModelMap) {

		Map<String, SKProviderModel> modelMap = new LinkedHashMap<>();
		List<SKProviderModel> allModel = new ArrayList<>();

		for (Element element : env.getElementsAnnotatedWith(annotationClass)) {
			if (!SuperficialValidation.validateElement(element)) continue;
			try {
				parseProviderAnnotation(annotationClass, element,allModel, modelMap, skSourceModelMap);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("解析 @SKProvider 注解出现问题~~");
			}
		}

		//开始检查
		HashMap<String, SKCheckProviderModel> checkHashMap = new HashMap<>();
		HashMap<SKCheckProviderModel, SKCheckProviderModel> print = new HashMap();

		for (SKProviderModel item : allModel) {

			SKCheckProviderModel skCheckModel = checkHashMap.get(item.key);

			if (skCheckModel != null) {
				SKCheckProviderModel errorCheckModel = new SKCheckProviderModel();
				errorCheckModel.methodName = item.name;
				errorCheckModel.providerName = item.className.simpleName();
				errorCheckModel.isSingle = item.isSingle;
				print.put(errorCheckModel, skCheckModel);
				continue;
			}

			SKCheckProviderModel checkMethod = new SKCheckProviderModel();
			checkMethod.methodName = item.name;
			checkMethod.providerName = item.className.simpleName();
			checkMethod.isSingle = item.isSingle;
			checkHashMap.put(item.key, checkMethod);
		}

		StringBuilder stringBuilder = null;
		for (Map.Entry<SKCheckProviderModel, SKCheckProviderModel> item : print.entrySet()) {

			if (stringBuilder == null) {
				stringBuilder = new StringBuilder("返回值必须是唯一值,请删除重复返回值~~");
			} else {
				stringBuilder.append(",");
			}

			stringBuilder.append("[");
			stringBuilder.append(item.getKey().providerName);
			stringBuilder.append(".");
			stringBuilder.append(item.getKey().methodName);
			stringBuilder.append(" = ");
			stringBuilder.append(item.getValue().providerName);
			stringBuilder.append(".");
			stringBuilder.append(item.getValue().methodName);
			stringBuilder.append("]");
		}
		if (stringBuilder != null) {
			logger.error(stringBuilder.toString());
			modelMap = null;
		}

		return modelMap;

	}

	/**
	 * 解析provider注解
	 * @param annotationClass
	 * @param element
	 * @param allModelMap
	 * @param providerModels
	 * @param skSourceModelMap
	 */
	private void parseProviderAnnotation(Class<? extends Annotation> annotationClass, Element element, List<SKProviderModel> allModelMap, Map<String, SKProviderModel> providerModels, Map<String, SKSourceModel> skSourceModelMap) {
		if (!(element instanceof ExecutableElement) || element.getKind() != METHOD) {
			throw new IllegalStateException(String.format("@%s annotation must be on a .", annotationClass.getSimpleName()));
		}
		boolean hasError = isInaccessibleViaGeneratedCode(element);

		if (hasError) {
			return;
		}

		ExecutableElement executableElement = (ExecutableElement) element;
		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

		String packageName = getPackage(enclosingElement).getQualifiedName().toString();

		String name = executableElement.getSimpleName().toString();

		List<? extends VariableElement> methodParameters = executableElement.getParameters();

		SKSingleton skSingleton = element.getAnnotation(SKSingleton.class);

		SKProviderModel skProviderModel = new SKProviderModel();

		skProviderModel.name = name;
		skProviderModel.packageName = packageName;
		skProviderModel.className = ClassName.get(packageName, enclosingElement.getSimpleName().toString());

		skProviderModel.isSingle = skSingleton != null;
		skProviderModel.returnType = SKUtils.bestGuess(executableElement.getReturnType());
		skProviderModel.parameters = new ArrayList<>();

		for (VariableElement item : methodParameters) {
			SKParamProviderModel skParamProviderModel = new SKParamProviderModel();

			skParamProviderModel.name = item.getSimpleName().toString();
			skParamProviderModel.packageName = getPackage(item).getQualifiedName().toString();
			skParamProviderModel.classType = SKUtils.bestGuess(item.asType());
			skParamProviderModel.providerType = ParameterizedTypeName.get(SK_I_PROVIDER, skParamProviderModel.classType);

			skProviderModel.parameters.add(skParamProviderModel);
		}

		// 生成key
		skProviderModel.buildKey();

		providerModels.put(skProviderModel.key, skProviderModel);

		//记录-统计全部方法 用于判断是否有相同
		allModelMap.add(skProviderModel);

		// 记录source
		SKSourceModel skSourceModel = getOrCreateSourceClassModel(skSourceModelMap, skProviderModel.className);
		skSourceModel.className = skProviderModel.className;

		if (skProviderModel.isSingle) {
			skSourceModel.isSingle = true;
		}
	}

	private void findParentInterface(SKInputModel skInputModel) {

		TypeElement returnTypeElement = (TypeElement) ((DeclaredType) skInputModel.typeMirror).asElement();
		// 是否实现接口

		if (returnTypeElement.getKind().isInterface()) {
			return;
		}

		for (TypeMirror item : returnTypeElement.getInterfaces()) {
			TypeName typeName = bestGuess(item);
			if (typeName instanceof ParameterizedTypeName) {
				continue;
			}

			ClassName interfaceClassName = (ClassName) typeName;
			if (SK_INTERFACE.equals(interfaceClassName)) {
				skInputModel.isImplInitInterface = true;
				break;
			}
		}
		if (skInputModel.isImplInitInterface) {
			return;
		}

		// 查找父类
		TypeMirror superType;
		while (true) {
			superType = returnTypeElement.getSuperclass();
			if (superType.getKind() == TypeKind.NONE) {
				return;
			}
			returnTypeElement = (TypeElement) ((DeclaredType) superType).asElement();

			for (TypeMirror item : returnTypeElement.getInterfaces()) {
				TypeName typeName = bestGuess(item);
				if (typeName instanceof ParameterizedTypeName) {
					continue;
				}

				ClassName interfaceClassName = (ClassName) typeName;
				if (SK_INTERFACE.equals(interfaceClassName)) {
					skInputModel.isImplInitInterface = true;
					return;
				}
			}
		}
	}

	private SKSourceModel getOrCreateSourceClassModel(Map<String, SKSourceModel> skSourceModelMap, ClassName className) {
		SKSourceModel skSourceModel = skSourceModelMap.get(className.reflectionName());
		if (skSourceModel == null) {
			skSourceModel = new SKSourceModel();
			skSourceModelMap.put(className.reflectionName(), skSourceModel);
		}
		return skSourceModel;
	}

	private boolean isInaccessibleViaGeneratedCode(Element element) {
		boolean hasError = false;
		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

		// Verify method modifiers.
		Set<Modifier> modifiers = element.getModifiers();
		if (modifiers.contains(PRIVATE)) {
			hasError = true;
		}
		if (modifiers.contains(STATIC)) {
			hasError = true;
		}
		// Verify containing type.
		if (enclosingElement.getKind() != CLASS) {
			hasError = true;
		}

		// Verify containing class visibility is not private.
		if (enclosingElement.getModifiers().contains(PRIVATE)) {
			hasError = true;
		}

		return hasError;
	}

}
