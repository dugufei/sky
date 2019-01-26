package sk.modules;

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

import sk.di.SkLogger;
import sk.modules.model.SkClassModel;
import sk.modules.model.SkFieldModel;
import sk.modules.model.SkModuleModel;
import sk.modules.model.SkParamProviderModel;
import sky.AutoID;
import sky.OpenMethod;

import static com.google.auto.common.MoreElements.getPackage;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;
import static sk.modules.SkConsts.KEY_MODULE_NAME;
import static sk.modules.SkConsts.NO_MODULE_NAME_TIPS;
import static sk.modules.SkUtils.bestGuess;

@AutoService(Processor.class)
public final class SkModuleProcessor extends AbstractProcessor {

	private static final String	OPTION_SDK_INT			= "sk.minSdk";

	private int					sdk						= 1;

	private Elements			elementUtils;

	private Types				typeUtils;

	private Filer				filer;

	private Trees				trees;

	private SkLogger			logger;

	private String				moduleName				= null; //改变后的值

	private String				originalvalueModuleName	= null; //原始值

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

		logger.info(">>> SkModule 组件化 初始化. <<<");

		// Attempt to get user configuration [moduleName]
		Map<String, String> options = processingEnv.getOptions();
		if (MapUtils.isNotEmpty(options)) {
			moduleName = options.get(KEY_MODULE_NAME);
		}

		if (StringUtils.isNotEmpty(moduleName)) {
			originalvalueModuleName = moduleName;
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
		annotations.add(AutoID.class);
		annotations.add(OpenMethod.class);
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
		logger.info(">>> SkModule 组件化 开始生成代码 <<<");

		// 生成ID
		ArrayList<SkClassModel> skyClassModels = findAutoApi(env, AutoID.class);

		if (skyClassModels.size() < 0) {
			logger.info(">>> SkModule 没有定义 AutoID <<<");
			return false;
		}

		SkAutoIDCreate skyAutoIDCreate = new SkAutoIDCreate();
		try {
			skyAutoIDCreate.createAutoID(originalvalueModuleName, skyClassModels);
		} catch (IOException e) {
			logger.error(e);
		}

		ArrayList<SkModuleModel> skProviderModels = findMethodsProvider(env, OpenMethod.class);
		if (skProviderModels == null) {
			return false;
		}
		// 提供者
		SkProviderCreate skyProviderCreate = new SkProviderCreate();

		for (SkModuleModel item : skProviderModels) {
			JavaFile javaIFile = skyProviderCreate.brewProvider(item);
			try {
				javaIFile.writeTo(filer);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		SkCreateModule skyCreateModule = new SkCreateModule(skProviderModels, moduleName);
		JavaFile javaIFile = skyCreateModule.brewModuleBiz();
		try {
			javaIFile.writeTo(filer);
		} catch (IOException e) {
			logger.error(e);
		}
		logger.info(">>> SkModule 组件化 生成代码结束 <<<");
		return false;
	}

	private ArrayList<SkModuleModel> findMethodsProvider(RoundEnvironment env, Class<? extends Annotation> annotationClass) {

		ArrayList<SkModuleModel> modelMap = new ArrayList<>();

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

	private ArrayList<SkClassModel> findAutoApi(RoundEnvironment env, Class<? extends Annotation> annotationClass) {
		ArrayList<SkClassModel> skClassModels = new ArrayList<>();

		Set<? extends Element> routeElements = env.getElementsAnnotatedWith(annotationClass);

		for (Element element : routeElements) {
			SkClassModel skClassModel = new SkClassModel();
			skClassModel.packageName = getPackage(element).getQualifiedName().toString();
			skClassModel.className = ClassName.get(skClassModel.packageName, element.getSimpleName().toString());
			skClassModel.skFieldModels = new ArrayList<>();

			List<VariableElement> variableElements = ElementFilter.fieldsIn(element.getEnclosedElements());

			for (VariableElement variableElement : variableElements) {
				SkFieldModel skFieldModel = new SkFieldModel();
				skFieldModel.name = variableElement.getSimpleName().toString();
				skFieldModel.type = TypeName.get(variableElement.asType());

				List<? extends AnnotationMirror> annotationMirrors = variableElement.getAnnotationMirrors();
				for (AnnotationMirror annotationMirror : annotationMirrors) {
					Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror.getElementValues();
					for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues.entrySet()) {
						String key = entry.getKey().getSimpleName().toString();
						Object value = entry.getValue().getValue();
						switch (key) {
							case "describe":
								String strVal = (String) value;
								skFieldModel.describe = strVal;
								break;
							case "params":
								List<? extends AnnotationValue> typeMirrors = (List<? extends AnnotationValue>) value;

								for (AnnotationValue annotationValue : typeMirrors) {
									skFieldModel.params.add(((TypeMirror) annotationValue.getValue()));
								}
								break;
						}
					}
				}

				skClassModel.skFieldModels.add(skFieldModel);
			}

			skClassModels.add(skClassModel);
		}

		return skClassModels;
	}

	/**
	 * 解析provider注解
	 *
	 * @param annotationClass
	 * @param element
	 * @param providerModels
	 */
	private void parseProviderAnnotation(Class<? extends Annotation> annotationClass, Element element, ArrayList<SkModuleModel> providerModels) {
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

		SkModuleModel skModuleModel = new SkModuleModel();

		skModuleModel.name = name;
		skModuleModel.nameCode = element.getAnnotation(OpenMethod.class).value();
		skModuleModel.packageName = packageName;
		skModuleModel.className = ClassName.get(packageName, enclosingElement.getSimpleName().toString());
		skModuleModel.returnType = bestGuess(executableElement.getReturnType());
		Set<Modifier> modifiers = element.getModifiers();
		if (modifiers.contains(STATIC)) {
			skModuleModel.isStatic = true;
		}
		skModuleModel.parameters = new ArrayList<>();
		for (VariableElement item : methodParameters) {
			SkParamProviderModel skParamProviderModel = new SkParamProviderModel();

			skParamProviderModel.name = item.getSimpleName().toString();
			skParamProviderModel.packageName = getPackage(item).getQualifiedName().toString();
			skParamProviderModel.classType = bestGuess(item.asType());

			skModuleModel.parameters.add(skParamProviderModel);
		}

		// 生成key
		skModuleModel.buildKey();

		providerModels.add(skModuleModel);
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
