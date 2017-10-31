package sky.compiler;

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
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import sky.OpenBiz;

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
		logger.info(">>> Found sky, 开始... <<<");
		Map<TypeElement, SkyBind> bindingMap = parseOpenBiz(routeElements);

		for (Map.Entry<TypeElement, SkyBind> entry : bindingMap.entrySet()) {
			SkyBind binding = entry.getValue();

			JavaFile javaIFile = binding.brewModuleBiz();
			try {
				javaIFile.writeTo(filer);
			} catch (IOException e) {
				logger.error(e);
			}
		}

		return false;
	}

	private Map<TypeElement, SkyBind> parseOpenBiz(Set<? extends Element> routeElements) {
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

				SkyBind.Builder builder = getOrCreateBindingBuilder(builderMap, (TypeElement) element);
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
		return annotations;
	}

	private SkyBind.Builder getOrCreateBindingBuilder(Map<TypeElement, SkyBind.Builder> builderMap, TypeElement enclosingElement) {
		SkyBind.Builder builder = builderMap.get(enclosingElement);
		if (builder == null) {
			builder = SkyBind.newBuilder(enclosingElement);
			builderMap.put(enclosingElement, builder);
		}
		return builder;
	}
}
