package sk.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Attribute;

import org.apache.commons.collections4.CollectionUtils;

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
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import sk.compiler.model.SKCheckProviderModel;
import sk.compiler.model.SKConstructorsModel;
import sk.compiler.model.SKDILibraryModel;
import sk.compiler.model.SKInputClassModel;
import sk.compiler.model.SKInputModel;
import sk.compiler.model.SKParamProviderModel;
import sk.compiler.model.SKProviderModel;
import sk.compiler.model.SKSourceModel;
import sky.SKDIApp;
import sky.SKDILibrary;
import sky.SKHTTP;
import sky.SKIO;
import sky.SKInput;
import sky.SKProvider;
import sky.SKSingleton;

import static com.google.auto.common.MoreElements.getPackage;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;
import static sk.compiler.SKUtils.bestGuess;
import static sk.compiler.SKUtils.getAnnotationMirror;
import static sk.compiler.SKUtils.getAnnotationValue;
import static sk.compiler.SkConsts.NAME_DEFAULT_LIBRARY;
import static sk.compiler.SkConsts.NAME_LIBRARY;
import static sk.compiler.SkConsts.SK_I_LAZY;
import static sk.compiler.SkConsts.SK_I_PROVIDER;
import static sk.compiler.SkConsts.SK_REPOSITORY;

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
		this.sdk += 1;
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
		annotations.add(SKDIApp.class);
		annotations.add(SKDILibrary.class);
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

		logger.info(">>> Found SKDILibrary, 开始... <<<");

		SKDILibraryModel skLibraryModel = findLibrary(env, SKDILibrary.class);

		logger.info(">>> Found SKDILibrary 结束... <<<");

		logger.info(">>> Found SKProvider, 开始... <<<");
		Map<String, SKSourceModel> skSourceModelMap = new LinkedHashMap<>();
		Map<String, SKProviderModel> skProviderModels = findMethodsProvider(env, SKProvider.class, skSourceModelMap);
		if (skProviderModels == null) {
			logger.error(">>> Found SKProvider 异常... <<<");
			return false;
		}
		if (skSourceModelMap.size() < 1) {
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

		JavaFile javaIFile;

		if (skLibraryModel == null) {
			SKDICreate skdiCreate = new SKDICreate();

			List<SKDILibraryModel> skdiLibraryModelList = findApp(env, SKDIApp.class, skSourceModelMap);

			Map<TypeElement, TypeElement> skSupport = findSupport(skInputModels);

			javaIFile = skdiCreate.brewDI(skProviderModels, skInputModels, skSourceModelMap, skdiLibraryModelList, skSupport);
		} else {
			SKDILibraryCreate libraryCreate = new SKDILibraryCreate();

			javaIFile = libraryCreate.brewDILibrary(skProviderModels, skInputModels, skSourceModelMap, skLibraryModel);
		}

		try {
			javaIFile.writeTo(filer);
		} catch (IOException e) {
			logger.error(e);
		}

		logger.info(">>> Found SKDI, 结束... <<<");

		return false;
	}

	private Map<TypeElement, TypeElement> findSupport(Map<TypeElement, SKInputClassModel> skInputModels) {
		Map<TypeElement, TypeElement> map = new HashMap<>();

		Deque<Map.Entry<TypeElement, SKInputClassModel>> entries = new ArrayDeque<>(skInputModels.entrySet());

		while (!entries.isEmpty()) {
			Map.Entry<TypeElement, SKInputClassModel> entry = entries.removeFirst();

			TypeElement type = entry.getKey();

			// 收集一级父类集合
			TypeMirror supportType = type.getSuperclass();
			if (supportType.getKind() == TypeKind.NONE) {
				continue;
			}
			TypeElement typeElement = (TypeElement) ((DeclaredType) supportType).asElement();

			boolean is = true;

			for (Element enclosedElement : typeElement.getEnclosedElements()) {
				if (enclosedElement.getKind() == ElementKind.FIELD) {
					SKInput skInput = enclosedElement.getAnnotation(SKInput.class);
					if (skInput != null) {
						is = false;
						break;
					}
				}
			}
			if (is) {
				continue;
			}
			map.put(type, typeElement);
		}
		return map;
	}

	private List<SKDILibraryModel> findApp(RoundEnvironment env, Class<? extends Annotation> skdiLibraryClass, Map<String, SKSourceModel> skSourceModelMap) {
		List<SKDILibraryModel> libraryModels = new ArrayList<>();

		Set<? extends Element> set = env.getElementsAnnotatedWith(skdiLibraryClass);
		if (set.size() < 1) {
			return libraryModels;
		}
		if (set.size() > 1) {
			logger.info("@SKDIApp 注解只能有一个~~");
			return libraryModels;
		}

		for (Element element : set) {

			if (!SuperficialValidation.validateElement(element)) continue;
			try {
				parseAppAnnotation(element, libraryModels, skSourceModelMap);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("解析 @SKDIApp 异常");
			}
		}

		return libraryModels;
	}

	private void parseAppAnnotation(Element element, List<SKDILibraryModel> libraryModels, Map<String, SKSourceModel> skSourceModelMap) {
		TypeElement enclosingElement = (TypeElement) element;
		AnnotationMirror annotationMirror = getAnnotationMirror(enclosingElement, SKDIApp.class.getCanonicalName());
		AnnotationValue annotationValue = getAnnotationValue(annotationMirror, "value");

		List<Attribute.Class> elementList = (List<Attribute.Class>) annotationValue.getValue();

		for (Attribute.Class clazz : elementList) {
			SKDILibraryModel skLibraryModel = new SKDILibraryModel();
			ClassName classNameLibrary = (ClassName) ClassName.get(clazz.getValue());
			skLibraryModel.name = classNameLibrary.simpleName() + NAME_LIBRARY;
			skLibraryModel.className = ClassName.get(classNameLibrary.packageName(), skLibraryModel.name);
			if (skLibraryModel.name.equals(NAME_DEFAULT_LIBRARY)) {
				skLibraryModel.isSKDefaultLibrary = true;
			}
			libraryModels.add(skLibraryModel);

			// 找到library 所有provider
			TypeElement libraryClass = elementUtils.getTypeElement(classNameLibrary.reflectionName());

			if (libraryClass == null) {
				return;
			}
			skLibraryModel.skProviderModels = new HashMap<>();
			for (Element elementItem : libraryClass.getEnclosedElements()) {

				if (elementItem.getKind() == ElementKind.METHOD) {

					ExecutableElement executableElement = (ExecutableElement) elementItem;

					TypeElement superTypeElement = (TypeElement) ((DeclaredType) executableElement.getReturnType()).asElement();

					for (Element enclosedElement : superTypeElement.getEnclosedElements()) {
						if (enclosedElement.getKind() != ElementKind.METHOD) {
							continue;
						}
						findLibraryProvider(enclosedElement, skLibraryModel, skSourceModelMap);
					}
				}
			}
		}
	}

	private SKDILibraryModel findLibrary(RoundEnvironment env, Class<? extends Annotation> skdiLibraryClass) {
		SKDILibraryModel skLibraryModel = null;

		for (Element element : env.getElementsAnnotatedWith(skdiLibraryClass)) {

			if (!SuperficialValidation.validateElement(element)) continue;
			try {
				skLibraryModel = parseLibraryAnnotation(element);

				if (skLibraryModel != null) {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("解析library异常");
			}
		}

		return skLibraryModel;
	}

	private SKDILibraryModel parseLibraryAnnotation(Element elementLibrary) {

		TypeElement enclosingElement = (TypeElement) elementLibrary;

		if (enclosingElement.getKind() != ElementKind.INTERFACE) {
			logger.info("注解 SKDILibrary 声明的类必须是接口~~");
			return null;
		}

		String packageName = getPackage(enclosingElement).getQualifiedName().toString();

		SKDILibraryModel skLibraryModel = new SKDILibraryModel();
		skLibraryModel.packageName = packageName;
		skLibraryModel.name = enclosingElement.getSimpleName().toString();
		skLibraryModel.className = ClassName.get(packageName, enclosingElement.getSimpleName().toString());

		if ((skLibraryModel.name + NAME_LIBRARY).equals(NAME_DEFAULT_LIBRARY)) {
			skLibraryModel.isSKDefaultLibrary = true;
		}

		return skLibraryModel;
	}

	private void findLibraryProvider(Element element, SKDILibraryModel skProviderModels, Map<String, SKSourceModel> skSourceModelMap) {
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
		skProviderModel.returnType = bestGuess(executableElement.getReturnType());
		// 解析是否存在 SKIO 或者 SKHTTP
		skProviderModel.isProxy = findProviderSKIOAndSKHttp(executableElement.getReturnType());

		skProviderModel.parameters = new ArrayList<>();

		for (VariableElement item : methodParameters) {
			SKParamProviderModel skParamProviderModel = new SKParamProviderModel();

			skParamProviderModel.name = item.getSimpleName().toString();
			skParamProviderModel.packageName = getPackage(item).getQualifiedName().toString();
			skParamProviderModel.classType = bestGuess(item.asType());
			skParamProviderModel.providerType = ParameterizedTypeName.get(SK_I_PROVIDER, skParamProviderModel.classType);

			skProviderModel.parameters.add(skParamProviderModel);
		}
		// library方法
		skProviderModel.isLibrary = true;
		skProviderModel.classNameLibrary = skProviderModels.className;

		// 生成key
		skProviderModel.buildKey();

		skProviderModels.skProviderModels.put(skProviderModel.key, skProviderModel);
		// 记录source
		SKSourceModel skSourceModel = getOrCreateSourceClassModel(enclosingElement, skSourceModelMap, skProviderModels);
		skSourceModel.className = skProviderModel.className;

		if (skSourceModel.skConstructorsModelList.size() > 0) {
			skSourceModel.isSingle = true;
		}

		if (skProviderModel.isSingle) {
			skSourceModel.isSingle = true;
		}

		skSourceModel.isLibrary = true;
		skSourceModel.classNameLibrary = skProviderModels.className;
	}

	private Map<TypeElement, SKInputClassModel> findFieldInput(RoundEnvironment env, Class<? extends Annotation> annotationClass, Map<String, SKProviderModel> skProviderModels) {

		Map<TypeElement, SKInputClassModel> modelMap = new LinkedHashMap<>();
		Set<TypeElement> erasedTargetNames = new LinkedHashSet<>();

		for (Element element : env.getElementsAnnotatedWith(annotationClass)) {
			if (!SuperficialValidation.validateElement(element)) continue;
			try {
				parseInputAnnotation(element, modelMap, skProviderModels, erasedTargetNames);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("生成input异常");
			}
		}

		Deque<Map.Entry<TypeElement, SKInputClassModel>> entries = new ArrayDeque<>(modelMap.entrySet());

		while (!entries.isEmpty()) {
			Map.Entry<TypeElement, SKInputClassModel> entry = entries.removeFirst();

			TypeElement type = entry.getKey();

			TypeElement parentType = findParentType(type, erasedTargetNames);

			if (parentType != null) {
				SKInputClassModel skInputClassModel = modelMap.get(parentType);

				if (skInputClassModel != null) {
					SKInputClassModel currentModel = entry.getValue();

					for (SKInputModel skInputModel : skInputClassModel.skInputModels) {
						skInputModel.className = currentModel.className;
						currentModel.skInputModels.add(skInputModel);
					}
				}
				modelMap.remove(parentType);
			}
		}

		return modelMap;
	}

	private void parseInputAnnotation(Element element, Map<TypeElement, SKInputClassModel> inputClassModelMap, Map<String, SKProviderModel> skProviderModels, Set<TypeElement> erasedTargetNames) {
		boolean hasError = isInaccessibleViaGeneratedCode(element);

		if (hasError) {
			return;
		}

		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

		erasedTargetNames.add(enclosingElement);
		String packageName = getPackage(enclosingElement).getQualifiedName().toString();

		SKInputModel skInputModel = new SKInputModel();
		skInputModel.fieldName = element.getSimpleName().toString();
		skInputModel.type = TypeName.get(element.asType());
		skInputModel.className = ClassName.get(packageName, enclosingElement.getSimpleName().toString());
		skInputModel.packageName = packageName;
		skInputModel.typeMirror = element.asType();
		skInputModel.isProxy = findProviderSKIOAndSKHttp(skInputModel.typeMirror);
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
				parseProviderAnnotation(annotationClass, element, allModel, modelMap, skSourceModelMap);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("解析 @SKProvider 注解出现问题~~");
			}
		}

		// 开始检查
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
	 *
	 * @param annotationClass
	 * @param element
	 * @param allModelMap
	 * @param providerModels
	 * @param skSourceModelMap
	 */
	private void parseProviderAnnotation(Class<? extends Annotation> annotationClass, Element element, List<SKProviderModel> allModelMap, Map<String, SKProviderModel> providerModels,
			Map<String, SKSourceModel> skSourceModelMap) {
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
		skProviderModel.returnType = bestGuess(executableElement.getReturnType());
		// 解析是否存在 SKIO 或者 SKHTTP
		skProviderModel.isProxy = findProviderSKIOAndSKHttp(executableElement.getReturnType());
		skProviderModel.parameters = new ArrayList<>();

		for (VariableElement item : methodParameters) {
			SKParamProviderModel skParamProviderModel = new SKParamProviderModel();

			skParamProviderModel.name = item.getSimpleName().toString();
			skParamProviderModel.packageName = getPackage(item).getQualifiedName().toString();
			skParamProviderModel.classType = bestGuess(item.asType());
			skParamProviderModel.providerType = ParameterizedTypeName.get(SK_I_PROVIDER, skParamProviderModel.classType);

			skProviderModel.parameters.add(skParamProviderModel);
		}

		// 生成key
		skProviderModel.buildKey();

		providerModels.put(skProviderModel.key, skProviderModel);

		// 记录-统计全部方法 用于判断是否有相同
		allModelMap.add(skProviderModel);

		// 记录source
		SKSourceModel skSourceModel = getOrCreateSourceClassModel(enclosingElement, skSourceModelMap, skProviderModel.className);
		skSourceModel.className = skProviderModel.className;

		if (skSourceModel.skConstructorsModelList.size() > 0) {
			skSourceModel.isSingle = true;
		}

		if (skProviderModel.isSingle) {
			skSourceModel.isSingle = true;
		}
	}

	private boolean findProviderSKIOAndSKHttp(TypeMirror typeMirror) {
		TypeElement returnTypeElement = (TypeElement) ((DeclaredType) typeMirror).asElement();
		return findParentType(returnTypeElement);
	}

	private void findParentInterface(SKInputModel skInputModel) {
		TypeElement superTypeElement = (TypeElement) ((DeclaredType) skInputModel.typeMirror).asElement();

		boolean is = true;
		// 是否实现接口
		for (Element enclosedElement : superTypeElement.getEnclosedElements()) {
			if (enclosedElement.getKind() == ElementKind.FIELD) {
				SKInput skInput = enclosedElement.getAnnotation(SKInput.class);
				if (skInput != null) {
					is = false;
					break;
				}
			}
		}
		if (is) {
			return;
		}
		skInputModel.isAutoInput = true;
	}

	private SKSourceModel getOrCreateSourceClassModel(TypeElement enclosingElement, Map<String, SKSourceModel> skSourceModelMap, ClassName className) {
		SKSourceModel skSourceModel = skSourceModelMap.get(className.reflectionName());
		if (skSourceModel == null) {
			skSourceModel = new SKSourceModel();
			// 构造函数
			List<ExecutableElement> executableElements = ElementFilter.constructorsIn(enclosingElement.getEnclosedElements());

			if (executableElements.size() > 1) {
				logger.error(skSourceModel.className.simpleName() + "类的构造函数不能存在多个");
			} else if (executableElements.size() == 1) {
				skSourceModel.skConstructorsModelList = new ArrayList<>();
				ExecutableElement item = executableElements.get(0);
				for (VariableElement variableElement : item.getParameters()) {
					SKConstructorsModel skConstructorsModel = new SKConstructorsModel();
					skConstructorsModel.packageName = getPackage(variableElement).getQualifiedName().toString();
					skConstructorsModel.fieldName = variableElement.getSimpleName().toString();
					skConstructorsModel.type = TypeName.get(variableElement.asType());
					skConstructorsModel.className = (ClassName) ClassName.get(variableElement.asType());
					skConstructorsModel.typeMirror = variableElement.asType();
					skSourceModel.skConstructorsModelList.add(skConstructorsModel);
				}
			}

			skSourceModelMap.put(className.reflectionName(), skSourceModel);
		}
		return skSourceModel;
	}

	private SKSourceModel getOrCreateSourceClassModel(TypeElement enclosingElement, Map<String, SKSourceModel> skSourceModelMap, SKDILibraryModel skdiLibraryModel) {
		SKSourceModel skSourceModel = skSourceModelMap.get(skdiLibraryModel.className.reflectionName());
		if (skSourceModel == null) {
			skSourceModel = new SKSourceModel();
			// 构造函数
			List<ExecutableElement> executableElements = ElementFilter.constructorsIn(enclosingElement.getEnclosedElements());

			if (executableElements.size() > 1) {
				logger.error(skSourceModel.className.simpleName() + "类的构造函数不能存在多个");
			} else if (executableElements.size() == 1) {
				skSourceModel.skConstructorsModelList = new ArrayList<>();
				ExecutableElement item = executableElements.get(0);
				for (VariableElement variableElement : item.getParameters()) {
					SKConstructorsModel skConstructorsModel = new SKConstructorsModel();
					skConstructorsModel.packageName = getPackage(variableElement).getQualifiedName().toString();
					skConstructorsModel.fieldName = variableElement.getSimpleName().toString();
					skConstructorsModel.type = TypeName.get(variableElement.asType());
					skConstructorsModel.className = (ClassName) ClassName.get(variableElement.asType());
					skConstructorsModel.typeMirror = variableElement.asType();
					skSourceModel.skConstructorsModelList.add(skConstructorsModel);
				}
				skdiLibraryModel.skConstructorsModelList = skSourceModel.skConstructorsModelList;
			}

			skSourceModelMap.put(skdiLibraryModel.className.reflectionName(), skSourceModel);
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

	/** Finds the parent binder type in the supplied set, if any. */
	private TypeElement findParentType(TypeElement typeElement, Set<TypeElement> parents) {
		TypeMirror type;
		while (true) {
			type = typeElement.getSuperclass();
			if (type.getKind() == TypeKind.NONE) {
				return null;
			}
			typeElement = (TypeElement) ((DeclaredType) type).asElement();
			if (parents.contains(typeElement)) {
				return typeElement;
			}
		}
	}

	/** Finds the parent binder type in the supplied set, if any. */
	private boolean findParentType(TypeElement typeElement) {
		TypeMirror type;
		while (true) {
			type = typeElement.getSuperclass();
			if (type.getKind() == TypeKind.NONE) {
				return false;
			}
			typeElement = (TypeElement) ((DeclaredType) type).asElement();

			TypeName superReturnType = ClassName.get(typeElement);

			if (superReturnType.equals(SK_REPOSITORY)) {
				return true;
			}
		}
	}
}
