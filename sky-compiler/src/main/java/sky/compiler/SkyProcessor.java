package sky.compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
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
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import sky.IBIZ;
import sky.IParent;
import sky.IUI;
import sky.Repeat;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

@AutoService(Processor.class)
public final class SkyProcessor extends AbstractProcessor {

	private static final String								OPTION_SDK_INT	= "sky.minSdk";

	private int												sdk				= 1;

	private Elements										elementUtils;

	private Types											typeUtils;

	private Filer											filer;

	private Trees											trees;

	private static final List<Class<? extends Annotation>>	LISTENERS		= Arrays.asList(	//
			IUI.class, IBIZ.class);

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
		try {
			trees = Trees.instance(processingEnv);
		} catch (IllegalArgumentException ignored) {
		}

	}

	/**
	 * 主流程
	 */
	@Override public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
		// 搜索注解
		Map<TypeElement, SkyBind> bindingMap = findAndParseTargets(env);

		for (Map.Entry<TypeElement, SkyBind> entry : bindingMap.entrySet()) {
			TypeElement typeElement = entry.getKey();
			SkyBind binding = entry.getValue();

			JavaFile javaIFile = binding.brewIJava();
			try {
				javaIFile.writeTo(filer);
			} catch (IOException e) {
				error(typeElement, "Unable to write binding for type %s: %s", typeElement, e.getMessage());
			}
		}

		return false;
	}

	private void findAndParseListener(RoundEnvironment env, Class<? extends Annotation> annotationClass, Map<TypeElement, SkyBind.Builder> builderMap, Set<TypeElement> erasedTargetNames) {
		for (Element element : env.getElementsAnnotatedWith(annotationClass)) {
			if (!SuperficialValidation.validateElement(element)) continue;
			try {
				parseListenerAnnotation(annotationClass, element, builderMap, erasedTargetNames);
			} catch (Exception e) {
				StringWriter stackTrace = new StringWriter();
				e.printStackTrace(new PrintWriter(stackTrace));

				error(element, "Unable to generate view binder for @%s.\n\n%s", annotationClass.getSimpleName(), stackTrace.toString());
			}

		}
	}

	/**
	 * 寻找注解
	 * 
	 * @param env
	 * @return
	 */
	private Map<TypeElement, SkyBind> findAndParseTargets(RoundEnvironment env) {
		Map<TypeElement, SkyBind.Builder> builderMap = new LinkedHashMap<>();
		Set<TypeElement> erasedTargetNames = new LinkedHashSet<>();

		scanForRClasses(env);

		for (Class<? extends Annotation> listener : LISTENERS) {
			findAndParseListener(env, listener, builderMap, erasedTargetNames);
		}

		Deque<Map.Entry<TypeElement, SkyBind.Builder>> entries = new ArrayDeque<>(builderMap.entrySet());
		Map<TypeElement, SkyBind> bindingMap = new LinkedHashMap<>();
		while (!entries.isEmpty()) {
			Map.Entry<TypeElement, SkyBind.Builder> entry = entries.removeFirst();

			TypeElement type = entry.getKey();
			SkyBind.Builder builder = entry.getValue();

			TypeElement parentType = findParentType(type, erasedTargetNames);
			if (parentType == null) {
				bindingMap.put(type, builder.build());

			} else {
				SkyBind parentBinding = bindingMap.get(parentType);
				if (parentBinding != null) {
					builder.setParent(parentBinding);
					bindingMap.put(type, builder.build());
				} else {
					// Has a superclass binding but we haven't built it yet. Re-enqueue for later.
					entries.addLast(entry);
				}
			}

		}

		return bindingMap;
	}

	private void parseParent(Element element, Map<TypeElement, SkyBind.Builder> builderMap, Set<TypeElement> erasedTargetNames) {
		boolean hasError = true;

		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
		// hasError |= isBindingInWrongPackage(BindBiz.class, element);

		if (hasError) {
			return;
		}
		String name = element.getSimpleName().toString();
		TypeName type = TypeName.get(element.asType());

		SkyBind.Builder builder = getOrCreateBindingBuilder(builderMap, enclosingElement);

		FieldBinding fieldBinding = new FieldBinding(name, type);
		builder.addField(fieldBinding);
		erasedTargetNames.add(enclosingElement);

	}

	private void parseBindBiz(Element element, Map<TypeElement, SkyBind.Builder> builderMap, Set<TypeElement> erasedTargetNames) {
		boolean hasError = true;

		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
		// hasError |= isBindingInWrongPackage(BindBiz.class, element);

		if (hasError) {
			return;
		}
		String name = element.getSimpleName().toString();
		TypeName type = TypeName.get(element.asType());

		SkyBind.Builder builder = getOrCreateBindingBuilder(builderMap, enclosingElement);

		FieldBinding fieldBinding = new FieldBinding(name, type);
		builder.addField(fieldBinding);

		// Add the type-erased version to the valid binding targets set.
		erasedTargetNames.add(enclosingElement);
	}

	/** 查找提供的集合中的父绑定类型. */

	private void scanForRClasses(RoundEnvironment env) {
		if (trees == null) return;

		RClassScanner scanner = new RClassScanner();

		for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
			for (Element element : env.getElementsAnnotatedWith(annotation)) {
				JCTree tree = (JCTree) trees.getTree(element, getMirror(element, annotation));
				if (tree != null) { // tree can be null if the references are compiled types and not source
					String respectivePackageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
					scanner.setCurrentPackageName(respectivePackageName);
					tree.accept(scanner);
				}
			}
		}
	}

	private static class RClassScanner extends TreeScanner {

		// Maps the currently evaulated rPackageName to R Classes
		private final Map<String, Set<String>>	rClasses	= new LinkedHashMap<>();

		private String							currentPackageName;

		@Override public void visitSelect(JCTree.JCFieldAccess jcFieldAccess) {
			Symbol symbol = jcFieldAccess.sym;
			if (symbol != null && symbol.getEnclosingElement() != null && symbol.getEnclosingElement().getEnclosingElement() != null
					&& symbol.getEnclosingElement().getEnclosingElement().enclClass() != null) {
				Set<String> rClassSet = rClasses.get(currentPackageName);
				if (rClassSet == null) {
					rClassSet = new HashSet<>();
					rClasses.put(currentPackageName, rClassSet);
				}
				rClassSet.add(symbol.getEnclosingElement().getEnclosingElement().enclClass().className());
			}
		}

		Map<String, Set<String>> getRClasses() {
			return rClasses;
		}

		void setCurrentPackageName(String respectivePackageName) {
			this.currentPackageName = respectivePackageName;
		}
	}

	private static AnnotationMirror getMirror(Element element, Class<? extends Annotation> annotation) {
		for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
			if (annotationMirror.getAnnotationType().toString().equals(annotation.getCanonicalName())) {
				return annotationMirror;
			}
		}
		return null;
	}

	@Override public Set<String> getSupportedOptions() {
		return Collections.singleton(OPTION_SDK_INT);
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
		annotations.add(IUI.class);
		annotations.add(IParent.class);
		annotations.add(IBIZ.class);
		return annotations;
	}

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

	/**
	 * 日志 -------------------
	 */
	private void logParsingError(Element element, Class<? extends Annotation> annotation, Exception e) {
		StringWriter stackTrace = new StringWriter();
		e.printStackTrace(new PrintWriter(stackTrace));
		error(element, "Unable to parse @%s binding.\n\n%s", annotation.getSimpleName(), stackTrace);
	}

	private void error(Element element, String message, Object... args) {
		printMessage(Diagnostic.Kind.ERROR, element, message, args);
	}

	private void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
		if (args.length > 0) {
			message = String.format(message, args);
		}

		processingEnv.getMessager().printMessage(kind, message, element);
	}

	private void parseListenerAnnotation(Class<? extends Annotation> annotationClass, Element element, Map<TypeElement, SkyBind.Builder> builderMap, Set<TypeElement> erasedTargetNames)
			throws Exception {
		// This should be guarded by the annotation's @Target but it's worth a check for
		// safe casting.
		if (!(element instanceof ExecutableElement) || element.getKind() != METHOD) {
			throw new IllegalStateException(String.format("@%s annotation must be on a method.", annotationClass.getSimpleName()));
		}

		// Verify that the method and its containing class are accessible via generated
		// code.
		boolean hasError = isInaccessibleViaGeneratedCode(annotationClass, "methods", element);

		if (hasError) {
			return;
		}

		ExecutableElement executableElement = (ExecutableElement) element;
		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

		// Assemble information on the method.
		Annotation annotation = element.getAnnotation(annotationClass);

		boolean isRepeat = false;
		Repeat repeat = element.getAnnotation(Repeat.class);

		if (repeat != null) {
			isRepeat = repeat.value();
		}

		String name = executableElement.getSimpleName().toString();

		List<? extends VariableElement> methodParameters = executableElement.getParameters();

		MethodBinding binding = new MethodBinding(name, annotation, isRepeat, methodParameters, executableElement.getReturnType());
		SkyBind.Builder builder = getOrCreateBindingBuilder(builderMap, enclosingElement);
		builder.setMethodViewBinding(binding);
		// Add the type-erased version to the valid binding targets set.
		erasedTargetNames.add(enclosingElement);

	}

	private SkyBind.Builder getOrCreateBindingBuilder(Map<TypeElement, SkyBind.Builder> builderMap, TypeElement enclosingElement) {
		SkyBind.Builder builder = builderMap.get(enclosingElement);
		if (builder == null) {
			builder = SkyBind.newBuilder(enclosingElement);
			builderMap.put(enclosingElement, builder);
		}
		return builder;
	}

	private boolean isInaccessibleViaGeneratedCode(Class<? extends Annotation> annotationClass, String targetThing, Element element) {
		boolean hasError = false;
		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

		// Verify method modifiers.
		Set<Modifier> modifiers = element.getModifiers();
		if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
			error(element, "@%s %s must not be private or static. (%s.%s)", annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
			hasError = true;
		}

		// Verify containing type.
		if (enclosingElement.getKind() != CLASS) {
			error(enclosingElement, "@%s %s may only be contained in classes. (%s.%s)", annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
			hasError = true;
		}

		// Verify containing class visibility is not private.
		if (enclosingElement.getModifiers().contains(PRIVATE)) {
			error(enclosingElement, "@%s %s may not be contained in private classes. (%s.%s)", annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
					element.getSimpleName());
			hasError = true;
		}

		return hasError;
	}

	private boolean isBindingInWrongPackage(Class<? extends Annotation> annotationClass, Element element) {
		TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
		String qualifiedName = enclosingElement.getQualifiedName().toString();

		if (qualifiedName.startsWith("android.")) {
			error(element, "@%s-annotated class incorrectly in Android framework package. (%s)", annotationClass.getSimpleName(), qualifiedName);
			return true;
		}
		if (qualifiedName.startsWith("java.")) {
			error(element, "@%s-annotated class incorrectly in Java framework package. (%s)", annotationClass.getSimpleName(), qualifiedName);
			return true;
		}

		return false;
	}
}
