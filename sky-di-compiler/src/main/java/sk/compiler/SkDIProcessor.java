package sk.compiler;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
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
import com.squareup.javapoet.TypeName;
import com.sun.source.util.Trees;

import sky.OpenBiz;
import sky.OpenDisplay;
import sky.SKInput;
import sky.SKProvider;
import sky.SKSingleton;
import sky.SKSource;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

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
			logger.info(">>> SkyProcessor 没有注解. <<<");
			return false;
		}

		logger.info(">>> Found SKProvider, 开始... <<<");
		Map<TypeElement, SkBind> bindingProviderMap = findMethodsProvider(env, SKProvider.class);

		if(bindingProviderMap == null){
			logger.info(">>> Found SKProvider 异常... <<<");
			return false;
		}
		Map<String, SkBind> map = new LinkedHashMap<>();

		for (Map.Entry<TypeElement, SkBind> entry : bindingProviderMap.entrySet()) {
			SkBind binding = entry.getValue();
			binding.inpuName(map);
			for (SkMethod skMethod : binding.methodViewBindings) {
				JavaFile javaIFile = binding.brewProvider(skMethod);
				try {
					javaIFile.writeTo(filer);
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		logger.info(">>> Found SKProvider 结束... <<<");

		logger.info(">>> Found SKInput, 开始... <<<");
		Map<TypeElement, SkBind> bindingInputMap = findFieldInput(env, SKInput.class);
		for (Map.Entry<TypeElement, SkBind> entry : bindingInputMap.entrySet()) {
			SkBind binding = entry.getValue();
			JavaFile javaIFile = binding.brewInput();
			try {
				javaIFile.writeTo(filer);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		logger.info(">>> Found SKInput 结束... <<<");

		logger.info(">>> Found SKSource, 开始... <<<");
		Set<? extends Element> routeElements = env.getElementsAnnotatedWith(SKSource.class);
		Map<TypeElement, SkBind> bindingSourceMap = findClass(routeElements, SKSource.class);
		for (Map.Entry<TypeElement, SkBind> entry : bindingSourceMap.entrySet()) {
			SkBind binding = entry.getValue();

			try {
				JavaFile javaIFile = binding.brewSource(map);
				javaIFile.writeTo(filer);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		logger.info(">>> Found SKSource 结束... <<<");

		return false;
	}

	private Map<TypeElement, SkBind> findClass(Set<? extends Element> routeElements, Class<? extends Annotation> annotationClass) {

		Map<TypeElement, SkBind.Builder> builderMap = new LinkedHashMap<>();
		Map<TypeElement, SkBind> bindingMap = new LinkedHashMap<>();

		if (CollectionUtils.isEmpty(routeElements)) {
			logger.info(">>> SKSource 没有注解. <<<");
			return bindingMap;
		}
		SkBind.Builder builderBind = null;

		for (Element element : routeElements) {

			List<TypeMirror> listParams = new ArrayList<>();
			List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
			for (AnnotationMirror annotationMirror : annotationMirrors) {
				Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
				for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
					if (entry.getValue().getValue() instanceof AnnotationValue) {
						TypeMirror typeMirror = (TypeMirror) entry.getValue().getValue();
						listParams.add(typeMirror);
					} else if (entry.getValue().getValue() instanceof List<?>) {
						List<? extends AnnotationValue> typeMirrors = (List<? extends AnnotationValue>) entry.getValue().getValue();
						for (AnnotationValue annotationValue : typeMirrors) {
							TypeMirror typeMirror = (TypeMirror) annotationValue.getValue();
							listParams.add(typeMirror);
						}
					}

				}
			}
			if (listParams.size() < 1) {
				continue;
			}
			String name = element.getSimpleName().toString();

			TypeMirror classType = element.asType();

			List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());

			List<SkField> skFields = new ArrayList<>();
			for (VariableElement variableElement : fields) {

				Name simpleName = variableElement.getSimpleName();

				TypeMirror elementType = variableElement.asType();

				String nameField = simpleName.toString();
				TypeName typeField = TypeName.get(elementType);

				SkField skField = new SkField(nameField, typeField, elementType);

				skFields.add(skField);
			}
			SkClass binding = new SkClass(name, listParams, classType, skFields);

			if (builderBind == null) {
				builderBind = SkBind.newBuilder((TypeElement) element);
				builderMap.put((TypeElement) element, builderBind);

			}

			if (skFields.size() < 1) {
				continue;
			}

			builderBind.setClassBinding(binding);

		}

		Deque<Map.Entry<TypeElement, SkBind.Builder>> entries = new ArrayDeque<>(builderMap.entrySet());
		while (!entries.isEmpty()) {
			Map.Entry<TypeElement, SkBind.Builder> entry = entries.removeFirst();
			TypeElement type = entry.getKey();
			SkBind.Builder builder = entry.getValue();
			bindingMap.put(type, builder.build());
		}

		return bindingMap;
	}

	private Map<TypeElement, SkBind> findFieldInput(RoundEnvironment env, Class<? extends Annotation> annotationClass) {
		Map<TypeElement, SkBind.Builder> builderMap = new LinkedHashMap<>();
		Map<TypeElement, SkBind> bindingMap = new LinkedHashMap<>();

		for (Element element : env.getElementsAnnotatedWith(annotationClass)) {
			if (!SuperficialValidation.validateElement(element)) continue;
			try {
				parseFieldAnnotation(annotationClass, element, builderMap);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("Unable to generate view binder for @%s.\n\n%s");
			}
		}

		Deque<Map.Entry<TypeElement, SkBind.Builder>> entries = new ArrayDeque<>(builderMap.entrySet());
		while (!entries.isEmpty()) {
			Map.Entry<TypeElement, SkBind.Builder> entry = entries.removeFirst();
			TypeElement type = entry.getKey();
			SkBind.Builder builder = entry.getValue();
			bindingMap.put(type, builder.build());
		}

		return bindingMap;
	}

	private void parseFieldAnnotation(Class<? extends Annotation> annotationClass, Element element, Map<TypeElement, SkBind.Builder> builderMap) {
		boolean hasError = isInaccessibleViaGeneratedCode(element);

		if (hasError) {
			return;
		}
		Name simpleName = element.getSimpleName();

		TypeMirror elementType = element.asType();

		String name = simpleName.toString();
		TypeName type = TypeName.get(elementType);

		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

		SkField skField = new SkField(name, type, elementType);

		SkBind.Builder builder = builderMap.get(enclosingElement);
		if (builder == null) {
			builder = SkBind.newBuilder(enclosingElement);
			builderMap.put(enclosingElement, builder);
		}
		builder.setFieldBinding(skField);
	}

	private Map<TypeElement, SkBind> findMethodsProvider(RoundEnvironment env, Class<? extends Annotation> annotationClass) {

		Map<TypeElement, SkBind.Builder> builderMap = new LinkedHashMap<>();
		Map<TypeElement, SkBind> bindingMap = new LinkedHashMap<>();

		for (Element element : env.getElementsAnnotatedWith(annotationClass)) {
			if (!SuperficialValidation.validateElement(element)) continue;
			try {
				parseListenerAnnotation(annotationClass, element, builderMap);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("Unable to generate view binder for @%s.\n\n%s");
			}
		}

		Deque<Map.Entry<TypeElement, SkBind.Builder>> entries = new ArrayDeque<>(builderMap.entrySet());

        HashMap<String, SKCheckModel> checkHashMap = new HashMap<>();
		HashMap<SKCheckModel, SKCheckModel> print = new HashMap();

		while (!entries.isEmpty()) {
			Map.Entry<TypeElement, SkBind.Builder> entry = entries.removeFirst();
			TypeElement type = entry.getKey();
			SkBind.Builder builder = entry.getValue();
			bindingMap.put(type, builder.build());
			// 检查是否有相同返回类型
			for (SkMethod skMethod : builder.getMethodBindings()) {
				ClassName name = (ClassName) ClassName.get(skMethod.getReturnType());
				SKCheckModel skCheckModel = checkHashMap.get(name.simpleName());

				if (skCheckModel != null && skMethod.isSingle()) {
					SKCheckModel errorCheckModel = new SKCheckModel();
					errorCheckModel.methodName = skMethod.getName();
					errorCheckModel.providerName = builder.getClassName();
					errorCheckModel.isSingle = skMethod.isSingle();
					print.put(errorCheckModel, skCheckModel);
					continue;
				}

				if(skMethod.isSingle()){
					SKCheckModel checkMethod = new SKCheckModel();
					checkMethod.methodName = skMethod.getName();
					checkMethod.providerName = builder.getClassName();
					checkMethod.isSingle = skMethod.isSingle();
					checkHashMap.put(name.simpleName(), checkMethod);
				}
			}

		}

		StringBuilder stringBuilder = null;
		for (Map.Entry<SKCheckModel, SKCheckModel> item : print.entrySet()) {

			if (stringBuilder == null) {
				stringBuilder = new StringBuilder("注解@SKSingleton的方法,返回值必须是唯一值,请删除重复返回值或者删除对应方法的@SKSingleton注解~~");
			} else {
				stringBuilder.append(",");
			}

			stringBuilder.append("[");
			stringBuilder.append(item.getKey().providerName.simpleName());
			stringBuilder.append(".");
			stringBuilder.append(item.getKey().methodName);
			stringBuilder.append(" = ");
			stringBuilder.append(item.getValue().providerName.simpleName());
			stringBuilder.append(".");
			stringBuilder.append(item.getValue().methodName);
			stringBuilder.append("]");
		}
		if(stringBuilder != null){
			logger.info(stringBuilder.toString());
		}

		return bindingMap;
	}

	//
	private void parseListenerAnnotation(Class<? extends Annotation> annotationClass, Element element, Map<TypeElement, SkBind.Builder> builderMap) {
		if (!(element instanceof ExecutableElement) || element.getKind() != METHOD) {
			throw new IllegalStateException(String.format("@%s annotation must be on a .", annotationClass.getSimpleName()));
		}
		boolean hasError = isInaccessibleViaGeneratedCode(element);

		if (hasError) {
			return;
		}

		ExecutableElement executableElement = (ExecutableElement) element;
		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

		String name = executableElement.getSimpleName().toString();

		List<? extends VariableElement> methodParameters = executableElement.getParameters();

		SKSingleton skSingleton = element.getAnnotation(SKSingleton.class);

		SkMethod binding = new SkMethod(name, methodParameters, executableElement.getReturnType(), skSingleton != null);

		SkBind.Builder builder = getOrCreateBindingBuilder(builderMap, enclosingElement);
		builder.setMethodViewBinding(binding);

	}

	private SkBind.Builder getOrCreateBindingBuilder(Map<TypeElement, SkBind.Builder> builderMap, TypeElement enclosingElement) {
		SkBind.Builder builder = builderMap.get(enclosingElement);
		if (builder == null) {
			builder = SkBind.newBuilder(enclosingElement);
			builderMap.put(enclosingElement, builder);
		}
		return builder;
	}

	//
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
