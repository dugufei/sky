package sky.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.sun.source.util.Trees;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
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
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import sky.OpenBiz;
import sky.OpenDisplay;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.PRIVATE;
import static sky.compiler.SkyConsts.NAME_OF_GROUP;
import static sky.compiler.SkyConsts.NAME_OF_GROUP_DISPLAY;

@AutoService(Processor.class)
public final class SkyProcessor extends AbstractProcessor {

	private static final String	OPTION_SDK_INT	= "sky.minSdk";

	private int					sdk				= 1;

	private Elements			elementUtils;

	private Types				typeUtils;

	private Filer				filer;

	private Trees				trees;

	private SkyLogger			logger;

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

		logger = new SkyLogger(processingEnv.getMessager()); // Package the log utils.

		logger.info(">>> SkyProcessor 初始化. <<<");

		try {
			trees = Trees.instance(processingEnv);
		} catch (IllegalArgumentException ignored) {
			logger.error(ignored);
		}
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

		Set<? extends Element> routeElements = env.getElementsAnnotatedWith(OpenBiz.class);

		// 搜索注解
		logger.info(">>> Found sky biz, 开始... <<<");
		Map<TypeElement, SkyBind> bindingMap = parseOpenBiz(routeElements,OpenBiz.class);

		int count = bindingMap.entrySet().size();
		if (count < 1) { // 表示没有给类进行注解
			bindingMap = findMethods(env, OpenBiz.class);
		}

		for (Map.Entry<TypeElement, SkyBind> entry : bindingMap.entrySet()) {
			SkyBind binding = entry.getValue();

			JavaFile javaIFile = binding.brewModuleBiz();
			try {
				javaIFile.writeTo(filer);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		logger.info(">>> Found sky biz, 结束... <<<");

		// display
		logger.info(">>> Found sky display, 开始... <<<");
		Map<TypeElement, SkyBind> displayMap = findMethods(env, OpenDisplay.class);

		for (Map.Entry<TypeElement, SkyBind> entry : displayMap.entrySet()) {
			SkyBind binding = entry.getValue();

			JavaFile javaIFile = binding.brewModuleDisplay();
			try {
				javaIFile.writeTo(filer);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		logger.info(">>> Found sky display, 结束... <<<");

		return false;
	}

	private Map<TypeElement, SkyBind> parseOpenBiz(Set<? extends Element> routeElements, Class<? extends Annotation> annotationClass) {
		Map<TypeElement, SkyBind.Builder> builderMap = new LinkedHashMap<>();
		Map<TypeElement, SkyBind> bindingMap = new LinkedHashMap<>();

		if (CollectionUtils.isEmpty(routeElements)) {
			logger.info(">>> OpenBiz 没有注解. <<<");
			return bindingMap;
		}

		for (Element element : routeElements) {
			List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

			for (ExecutableElement executableElement : methods) {
				String name = executableElement.getSimpleName().toString();

				List<? extends VariableElement> methodParameters = executableElement.getParameters();

				SkyMethod binding = new SkyMethod(name, methodParameters, executableElement.getReturnType());

				SkyBind.Builder builder = getOrCreateBindingBuilder(builderMap, (TypeElement) element, annotationClass);
				builder.setMethodViewBinding(binding);
			}
		}

		Deque<Map.Entry<TypeElement, SkyBind.Builder>> entries = new ArrayDeque<>(builderMap.entrySet());
		while (!entries.isEmpty()) {
			Map.Entry<TypeElement, SkyBind.Builder> entry = entries.removeFirst();
			TypeElement type = entry.getKey();
			SkyBind.Builder builder = entry.getValue();
			bindingMap.put(type, builder.build());
		}

		return bindingMap;
	}

	private Map<TypeElement, SkyBind> findMethods(RoundEnvironment env, Class<? extends Annotation> annotationClass) {

		Map<TypeElement, SkyBind.Builder> builderMap = new LinkedHashMap<>();
		Map<TypeElement, SkyBind> bindingMap = new LinkedHashMap<>();

		for (Element element : env.getElementsAnnotatedWith(annotationClass)) {
			if (!SuperficialValidation.validateElement(element)) continue;
			try {
				parseListenerAnnotation(annotationClass, element, builderMap);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("Unable to generate view binder for @%s.\n\n%s");
			}
		}

		Deque<Map.Entry<TypeElement, SkyBind.Builder>> entries = new ArrayDeque<>(builderMap.entrySet());
		while (!entries.isEmpty()) {
			Map.Entry<TypeElement, SkyBind.Builder> entry = entries.removeFirst();
			TypeElement type = entry.getKey();
			SkyBind.Builder builder = entry.getValue();
			bindingMap.put(type, builder.build());
		}

		return bindingMap;
	}

	private void parseListenerAnnotation(Class<? extends Annotation> annotationClass, Element element, Map<TypeElement, SkyBind.Builder> builderMap) throws Exception {
		if (!(element instanceof ExecutableElement) || element.getKind() != METHOD) {
			throw new IllegalStateException(String.format("@%s annotation must be on a method.", annotationClass.getSimpleName()));
		}
		boolean hasError = isInaccessibleViaGeneratedCode(annotationClass, "methods", element);

		if (hasError) {
			return;
		}

		ExecutableElement executableElement = (ExecutableElement) element;
		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

		String name = executableElement.getSimpleName().toString();

		List<? extends VariableElement> methodParameters = executableElement.getParameters();

		SkyMethod binding = new SkyMethod(name, methodParameters, executableElement.getReturnType());

		SkyBind.Builder builder = getOrCreateBindingBuilder(builderMap, enclosingElement, annotationClass);
		builder.setMethodViewBinding(binding);

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
		annotations.add(OpenBiz.class);
		annotations.add(OpenDisplay.class);
		return annotations;
	}

	private SkyBind.Builder getOrCreateBindingBuilder(Map<TypeElement, SkyBind.Builder> builderMap, TypeElement enclosingElement, Class<? extends Annotation> annotationClass) {
		SkyBind.Builder builder = builderMap.get(enclosingElement);
		if (builder == null) {
			if (annotationClass.equals(OpenBiz.class)) {
				builder = SkyBind.newBuilder(enclosingElement,NAME_OF_GROUP);
			} else if (annotationClass.equals(OpenDisplay.class)) {
				builder = SkyBind.newBuilder(enclosingElement,NAME_OF_GROUP_DISPLAY);
			}
			builderMap.put(enclosingElement, builder);
		}
		return builder;
	}

	private boolean isInaccessibleViaGeneratedCode(Class<? extends Annotation> annotationClass, String targetThing, Element element) {
		boolean hasError = false;
		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

		// Verify method modifiers.
		Set<Modifier> modifiers = element.getModifiers();
		if (modifiers.contains(PRIVATE)) {
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
