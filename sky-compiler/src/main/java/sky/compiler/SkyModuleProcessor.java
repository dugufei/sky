package sky.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.sun.source.util.Trees;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import sk.compiler.SkLogger;
import sky.AutoID;
import sky.OpenMethod;
import sky.compiler.model.SkyClassModel;
import sky.compiler.model.SkyFieldModel;
import sky.compiler.model.SkyModuleModel;
import sky.compiler.model.SkyParamProviderModel;

import static com.google.auto.common.MoreElements.getPackage;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;
import static sky.compiler.SkyConsts.KEY_MODULE_NAME;
import static sky.compiler.SkyConsts.NO_MODULE_NAME_TIPS;
import static sky.compiler.SkyUtils.bestGuess;

@AutoService(Processor.class)
public final class SkyModuleProcessor extends AbstractProcessor {

	private static final String	OPTION_SDK_INT	= "sk.minSdk";

	private int					sdk				= 1;

	private Elements			elementUtils;

	private Types				typeUtils;

	private Filer				filer;

	private Trees				trees;

	private SkLogger			logger;

	private String				moduleName		= null;

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

		logger.info(">>> SkyModule 组件化 初始化. <<<");

		// Attempt to get user configuration [moduleName]
		Map<String, String> options = processingEnv.getOptions();
		if (MapUtils.isNotEmpty(options)) {
			moduleName = options.get(KEY_MODULE_NAME);
		}

		if (StringUtils.isNotEmpty(moduleName)) {
			moduleName = moduleName.replaceAll("[^0-9a-zA-Z_]+", "");
			logger.info("组件名称[" + moduleName + "]");
		} else {
			logger.info(NO_MODULE_NAME_TIPS);
			moduleName = "default";
		}

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
		annotations.add(OpenMethod.class);
		annotations.add(AutoID.class);
		return annotations;
	}

	/**
	 * 主流程
	 */
	@Override public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
		// 如果没有注解
		if (CollectionUtils.isEmpty(elements)) {
			return false;
		}
		logger.info(">>> SkyModule 组件化 开始生成代码 <<<");

		// 生成ID
		ArrayList<SkyClassModel> skyClassModels = findAutoApi(env, AutoID.class);

		if (skyClassModels.size() < 0) {
			logger.info(">>> SkyModule 没有定义 AutoID <<<");
			return false;
		}

		SkyAutoIDCreate skyAutoIDCreate = new SkyAutoIDCreate();
		try {
			skyAutoIDCreate.createAutoID(moduleName, skyClassModels);
		} catch (IOException e) {
			logger.error(e);
		}

		ArrayList<SkyModuleModel> skProviderModels = findMethodsProvider(env, OpenMethod.class);
		if (skProviderModels == null) {
			return false;
		}
		// 提供者
		SkyProviderCreate skyProviderCreate = new SkyProviderCreate();

		for (SkyModuleModel item : skProviderModels) {
			JavaFile javaIFile = skyProviderCreate.brewProvider(item);
			try {
				javaIFile.writeTo(filer);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		SkyCreateModule skyCreateModule = new SkyCreateModule(skProviderModels, moduleName);
		JavaFile javaIFile = skyCreateModule.brewModuleBiz();
		try {
			javaIFile.writeTo(filer);
		} catch (IOException e) {
			logger.error(e);
		}
		logger.info(">>> SkyModule 组件化 生成代码结束 <<<");
		return false;
	}

	private ArrayList<SkyModuleModel> findMethodsProvider(RoundEnvironment env, Class<? extends Annotation> annotationClass) {

		ArrayList<SkyModuleModel> modelMap = new ArrayList<>();

		for (Element element : env.getElementsAnnotatedWith(annotationClass)) {
			if (!SuperficialValidation.validateElement(element)) continue;
			try {
				parseProviderAnnotation(annotationClass, element, modelMap);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("解析 @SKProvider 注解出现问题~~");
			}
		}

		return modelMap;

	}

	private ArrayList<SkyClassModel> findAutoApi(RoundEnvironment env, Class<? extends Annotation> annotationClass) {
		ArrayList<SkyClassModel> skyClassModels = new ArrayList<>();

		Set<? extends Element> routeElements = env.getElementsAnnotatedWith(annotationClass);

		for (Element element : routeElements) {
			SkyClassModel skyClassModel = new SkyClassModel();
			skyClassModel.packageName = getPackage(element).getQualifiedName().toString();
			skyClassModel.className = ClassName.get(skyClassModel.packageName, element.getSimpleName().toString());
			skyClassModel.skyFieldModels = new ArrayList<>();

			List<VariableElement> variableElements = ElementFilter.fieldsIn(element.getEnclosedElements());

			for (VariableElement variableElement : variableElements) {
				SkyFieldModel skyFieldModel = new SkyFieldModel();
				skyFieldModel.name = variableElement.getSimpleName().toString();
				skyFieldModel.type = TypeName.get(variableElement.asType());

				List<? extends AnnotationMirror> annotationMirrors = variableElement.getAnnotationMirrors();
				for (AnnotationMirror annotationMirror : annotationMirrors) {
					Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
					for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
						String key = entry.getKey().getSimpleName().toString();
						Object value = entry.getValue().getValue();
						switch (key) {
							case "describe":
								String strVal = (String) value;
								skyFieldModel.describe = strVal;
								break;
							case "params":
								List<? extends AnnotationValue> typeMirrors = (List<? extends AnnotationValue>) value;

								for (AnnotationValue annotationValue : typeMirrors) {
									skyFieldModel.params.add(((TypeMirror) annotationValue.getValue()));
								}
								break;
						}
					}
				}

				skyClassModel.skyFieldModels.add(skyFieldModel);
			}

			skyClassModels.add(skyClassModel);
		}

		return skyClassModels;
	}

	/**
	 * 解析provider注解
	 *
	 * @param annotationClass
	 * @param element
	 * @param providerModels
	 */
	private void parseProviderAnnotation(Class<? extends Annotation> annotationClass, Element element, ArrayList<SkyModuleModel> providerModels) {
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

		SkyModuleModel skyModuleModel = new SkyModuleModel();

		skyModuleModel.name = name;
		skyModuleModel.nameCode = element.getAnnotation(OpenMethod.class).value();
		skyModuleModel.packageName = packageName;
		skyModuleModel.className = ClassName.get(packageName, enclosingElement.getSimpleName().toString());
		skyModuleModel.returnType = bestGuess(executableElement.getReturnType());
		Set<Modifier> modifiers = element.getModifiers();
		if (modifiers.contains(STATIC)) {
			skyModuleModel.isStatic = true;
		}
		skyModuleModel.parameters = new ArrayList<>();
		for (VariableElement item : methodParameters) {
			SkyParamProviderModel skyParamProviderModel = new SkyParamProviderModel();

			skyParamProviderModel.name = item.getSimpleName().toString();
			skyParamProviderModel.packageName = getPackage(item).getQualifiedName().toString();
			skyParamProviderModel.classType = bestGuess(item.asType());

			skyModuleModel.parameters.add(skyParamProviderModel);
		}

		// 生成key
		skyModuleModel.buildKey();

		providerModels.add(skyModuleModel);
	}

	private boolean isInaccessibleViaGeneratedCode(Element element) {
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
