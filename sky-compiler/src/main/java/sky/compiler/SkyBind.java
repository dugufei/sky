package sky.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import sky.BackgroundType;
import sky.IBIZ;
import sky.IParent;
import sky.IUI;
import sky.IType;

import static com.google.auto.common.MoreElements.getPackage;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.type.TypeKind.VOID;

/**
 * @author sky
 * @version 1.0 on 2017-07-02 下午9:29
 * @see SkyBind
 */
final class SkyBind {

	private static final String			BIZ			= "BIZ";

	private static final String			Biz			= "Biz";

	private static final ClassName		UNBINDER	= ClassName.get("sky", "Unbinder");

	private static final ClassName		REPEAT		= ClassName.get("sky", "Repeat");

	private static final ClassName		SKYAppUtil	= ClassName.get("jc.sky.common.utils", "SKYAppUtil");

	private static final ClassName		SKYHelper	= ClassName.get("jc.sky", "SKYHelper");

	private static final ClassName		bizParan	= ClassName.get("jc.sky.core", "SKYBiz");

	private static final ClassName		iBizParan	= ClassName.get("jc.sky.core", "SKYIBiz");

	private final TypeName				targetTypeName;

	private final SkyBind				parentBinding;

	private final ClassName				iSkyClassName;

	private final ClassName				skyClassName;

	private final ClassName				defaultClassName;

	private final List<MethodBinding>	methodViewBindings;

	private final boolean				isFinal;

	private final boolean				isBiz;

	private final List<FieldBinding>	fieldBindings;

	private final TypeName				aParent;

	private SkyBind(TypeName aParent, TypeName targetTypeName, ClassName iSkyClassName, ClassName skyClassName, ClassName defaultClassName, List<MethodBinding> methodViewBindings,
			SkyBind parentBinding, boolean isFinal, boolean isBiz, List<FieldBinding> fieldBindings) {
		this.aParent = aParent;
		this.targetTypeName = targetTypeName;
		this.iSkyClassName = iSkyClassName;
		this.skyClassName = skyClassName;
		this.defaultClassName = defaultClassName;
		this.methodViewBindings = methodViewBindings;
		this.parentBinding = parentBinding;
		this.isFinal = isFinal;
		this.isBiz = isBiz;
		this.fieldBindings = fieldBindings;
	}

	JavaFile brewIJava() {
		return JavaFile.builder(iSkyClassName.packageName(), createInterfaceType()).addFileComment("Generated code from Sky. Do not modify!").build();
	}

	JavaFile brewJava() {
		return JavaFile.builder(skyClassName.packageName(), createType()).addFileComment("Generated code from Sky. Do not modify!").build();
	}

	private TypeSpec createType() {
		TypeSpec.Builder result = TypeSpec.classBuilder(skyClassName.simpleName()).addModifiers(PUBLIC);
		if (isFinal) {
			result.addModifiers(FINAL);
		}
		if (parentBinding != null) {
			result.superclass(parentBinding.skyClassName);
		} else {
			result.addSuperinterface(iSkyClassName);
			result.addSuperinterface(UNBINDER);
		}

		// 添加属性
		result.addField(targetTypeName, "target", PRIVATE);

		// 构造函数
		if (isBiz) {
			MethodSpec.Builder defaultConstrutor = MethodSpec.constructorBuilder().addModifiers(PUBLIC);
			result.addMethod(defaultConstrutor.build());
		}

		MethodSpec.Builder construtor = MethodSpec.constructorBuilder().addModifiers(PUBLIC).addParameter(targetTypeName, "target");
		construtor.addStatement("this.target = target");
		for (FieldBinding item : fieldBindings) {

			String name = item.getRawType().simpleName().substring(1, item.getRawType().simpleName().length());

			construtor.addStatement("this.target.$L = ($T) $T.getImplClass($T.class, $L.class,new $L())", item.getName(), item.getRawType(), SKYAppUtil, item.getRawType(), name, name);
		}
		result.addMethod(construtor.build());

		int size = methodViewBindings.size();

		for (int i = 0; i < size; i++) {
			addMethodSky(result, methodViewBindings.get(i));
		}

		// 清空
		MethodSpec.Builder unbind = MethodSpec.methodBuilder("unbind").addAnnotation(Override.class).addModifiers(PUBLIC);
		unbind.addStatement("$T target = this.target", targetTypeName);
		unbind.addStatement("if (target == null) throw new $T($S)", IllegalStateException.class, "Bindings already cleared.");
		unbind.addStatement("$N = null", "this.target");
		result.addMethod(unbind.build());

		return result.build();
	}

	private void addMethodSky(TypeSpec.Builder result, MethodBinding method) {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.getName()).addAnnotation(Override.class).addModifiers(PUBLIC).returns(bestGuess(method.getReturnType()));

		// 添加方法
		boolean hasReturnType = VOID != method.getReturnType().getKind();
		CodeBlock.Builder builder = CodeBlock.builder();

		IUI interf = (IUI) method.getAnnotation();
		IType methodType = interf.value();
		Modifier modifier = FINAL;

		switch (methodType) {
			case UI:
				if (hasReturnType) {
					builder.add("return ");
					ui(builder, method);
					methodBuilder.addCode(builder.build());
				} else {
					if (isBiz) {
						ui(builder, method);
						methodBuilder.addCode(builder.build());
					} else {
						isUI(methodBuilder, builder, method);
					}
				}
				break;
			case SINGLE:
				single(methodBuilder, builder, method);
				break;
			case HTTP:
				http(methodBuilder, builder, method);
				break;
			case WORK:
				work(methodBuilder, builder, method);
				break;
		}

		for (int i = 0, count = method.getParameters().size(); i < count; i++) {
			methodBuilder.addParameter(bestGuess(method.getParameters().get(i).asType()), "p" + i, modifier);
		}

		result.addMethod(methodBuilder.build());
	}

	private void isUI(MethodSpec.Builder methodBuilder, CodeBlock.Builder builder, MethodBinding method) {
		builder.add("if(!$T.isMainLooperThread()){\n", SKYHelper);
		builder.add("if (target != null) {\n");
		ui(builder, method);
		builder.add("}}else{\n");
		builder.add("Runnable runnable = new Runnable() {\n");
		builder.add("@Override public void run() {\n");
		builder.add("try {\n");
		builder.add("if (target != null) {\n");
		ui(builder, method);
		builder.add("}\n");
		builder.add("} catch (Exception throwable) {\n");
		builder.add("if ($T.isLogOpen()) {\n", SKYHelper);
		builder.add("throwable.printStackTrace();}}}};\n");
		builder.add("$T.mainLooper().execute(runnable);}\n", SKYHelper);
		methodBuilder.addCode(builder.build());
	}

	private void work(MethodSpec.Builder methodBuilder, CodeBlock.Builder builder, MethodBinding method) {
		builder.add("$T.threadPoolHelper().getWorkExecutorService().execute(new Runnable() {", SKYHelper);
		builder.add("\n");
		builder.add("@Override public void run() {");
		builder.add("\n");
		builder.add("if(target != null){\n");
		ui(builder, method);
		builder.add("}\n");
		builder.add("}\n");
		builder.add("});\n");
		methodBuilder.addCode(builder.build());
	}

	private void http(MethodSpec.Builder methodBuilder, CodeBlock.Builder builder, MethodBinding method) {
		builder.add("$T.threadPoolHelper().getHttpExecutorService().execute(new Runnable() {", SKYHelper);
		builder.add("\n");
		builder.add("@Override public void run() {");
		builder.add("\n");
		builder.add("if(target != null){\n");
		ui(builder, method);
		builder.add("}\n");
		builder.add("}\n");
		builder.add("});\n");
		methodBuilder.addCode(builder.build());
	}

	private void single(MethodSpec.Builder methodBuilder, CodeBlock.Builder builder, MethodBinding method) {
		builder.add("$T.threadPoolHelper().getSingleWorkExecutorService().execute(new Runnable() {", SKYHelper);
		builder.add("\n");
		builder.add("@Override public void run() {");
		builder.add("\n");
		builder.add("if(target != null){\n");
		ui(builder, method);
		builder.add("}\n");
		builder.add("}\n");
		builder.add("});\n");
		methodBuilder.addCode(builder.build());
	}

	private void ui(CodeBlock.Builder builder, MethodBinding method) {
		builder.add("target.$L(", method.getName());

		for (int i = 0, count = method.getParameters().size(); i < count; i++) {
			if (i > 0) {
				builder.add(", ");
			}
			builder.add("p$L", i);
		}
		builder.add(");\n");
	}

	private TypeSpec createInterfaceType() {
		ClassName IMPL = ClassName.get("jc.sky.core", "Impl");
		AnnotationSpec annotationSpec = AnnotationSpec.builder(IMPL).addMember("value", "$T.class", defaultClassName).build();

		TypeSpec.Builder result = TypeSpec.interfaceBuilder(iSkyClassName.simpleName()).addAnnotation(annotationSpec).addModifiers(PUBLIC);
		int size = methodViewBindings.size();

		for (int i = 0; i < size; i++) {
			MethodSpec methodSpec = createInterfaceMethod(result, methodViewBindings.get(i));
			result.addMethod(methodSpec);
		}
		if (aParent != null) {
			result.addSuperinterface(aParent);
		}

		return result.build();
	}

	private MethodSpec createInterfaceMethod(TypeSpec.Builder result, MethodBinding methodViewBinding) {
		IType methodType = null;
		if (methodViewBinding.getAnnotation() instanceof IUI) {
			IUI iui = (IUI) methodViewBinding.getAnnotation();
			methodType = iui.value();
		} else if (methodViewBinding.getAnnotation() instanceof IBIZ) {
			IBIZ ibiz = (IBIZ) methodViewBinding.getAnnotation();
			methodType = ibiz.value();
			if (aParent == null) {
				result.addSuperinterface(iBizParan);
			}
		}

		if (methodType == null) {
			return null;
		}

		AnnotationSpec repeatSpec = AnnotationSpec.builder(REPEAT).addMember("value", "$L", methodViewBinding.getRepeat()).build();

		MethodSpec.Builder callbackMethod = MethodSpec.methodBuilder(methodViewBinding.getName()).addModifiers(PUBLIC, ABSTRACT).returns(bestGuess(methodViewBinding.getReturnType()));

		callbackMethod.addAnnotation(repeatSpec);

		for (int i = 0, count = methodViewBinding.getParameters().size(); i < count; i++) {
			callbackMethod.addParameter(bestGuess(methodViewBinding.getParameters().get(i).asType()), "p" + i);
		}

		ClassName IMPL = ClassName.get("sky", "Background");
		AnnotationSpec annotationSpec;
		switch (methodType) {
			case UI:
				// annotationSpec = AnnotationSpec.builder(IMPL).addMember("value", "$T.UI",
				// BackgroundType.class).build();
				// callbackMethod.addAnnotation(annotationSpec);
				break;
			case SINGLE:
				annotationSpec = AnnotationSpec.builder(IMPL).addMember("value", "$T.SINGLE", BackgroundType.class).build();
				callbackMethod.addAnnotation(annotationSpec);
				break;
			case HTTP:
				annotationSpec = AnnotationSpec.builder(IMPL).addMember("value", "$T.HTTP", BackgroundType.class).build();
				callbackMethod.addAnnotation(annotationSpec);
				break;
			case WORK:
				annotationSpec = AnnotationSpec.builder(IMPL).addMember("value", "$T.WORK", BackgroundType.class).build();
				callbackMethod.addAnnotation(annotationSpec);
				break;
		}
		return callbackMethod.build();
	}

	static Builder newBuilder(TypeElement enclosingElement) {
		TypeMirror typeMirror = enclosingElement.asType();
		TypeName targetType = TypeName.get(typeMirror);
		if (targetType instanceof ParameterizedTypeName) {
			targetType = ((ParameterizedTypeName) targetType).rawType;
		}

		String packageName = getPackage(enclosingElement).getQualifiedName().toString();
		String className = enclosingElement.getQualifiedName().toString().substring(packageName.length() + 1).replace('.', '$');

		if (className.contains(BIZ) || className.contains(Biz)) {
			ClassName iSkyClassName = ClassName.get(packageName, "I" + className);
			ClassName skyClassName = ClassName.get(packageName, className + "_Bind_Biz");
			ClassName defaultClassName = ClassName.get(packageName, className);
			boolean isFinal = enclosingElement.getModifiers().contains(Modifier.FINAL);
			IParent iParent = enclosingElement.getAnnotation(IParent.class);

			ClassName cParent = null;
			if (iParent != null) {
				cParent = ClassName.bestGuess(iParent.value());
			}

			return new Builder(cParent, targetType, iSkyClassName, skyClassName, defaultClassName, isFinal, true);
		} else {
			ClassName iSkyClassName = ClassName.get(packageName, "I" + className);
			ClassName skyClassName = ClassName.get(packageName, className + "_Bind_UI");
			ClassName defaultClassName = ClassName.get(packageName, className);
			boolean isFinal = enclosingElement.getModifiers().contains(Modifier.FINAL);
			IParent iParent = enclosingElement.getAnnotation(IParent.class);
			ClassName cParent = null;
			if (iParent != null) {
				cParent = ClassName.bestGuess(iParent.value());
			}

			return new Builder(cParent, targetType, iSkyClassName, skyClassName, defaultClassName, isFinal, false);
		}
	}

	static final class Builder {

		private final List<MethodBinding>	methodBindings	= new ArrayList<>();

		private final List<FieldBinding>	fieldBindings	= new ArrayList<>();

		private final TypeName				targetTypeName;

		private final ClassName				iSkyClassName;

		private final ClassName				skyClassName;

		private final ClassName				defaultClassName;

		private SkyBind						parentBinding;

		private final boolean				isFinal;

		private final boolean				isBiz;

		private final TypeName				aParent;

		private Builder(TypeName aParent, TypeName targetType, ClassName iSkyClassName, ClassName skyClassName, ClassName defaultClassName, boolean isFinal, boolean isBiz) {
			this.aParent = aParent;
			this.targetTypeName = targetType;
			this.iSkyClassName = iSkyClassName;
			this.skyClassName = skyClassName;
			this.defaultClassName = defaultClassName;
			this.isFinal = isBiz;
			this.isBiz = isBiz;
		}

		SkyBind build() {
			return new SkyBind(aParent, targetTypeName, iSkyClassName, skyClassName, defaultClassName, methodBindings, parentBinding, isFinal, isBiz, fieldBindings);
		}

		void setParent(SkyBind parent) {
			this.parentBinding = parent;
		}

		public void setMethodViewBinding(MethodBinding methodViewBinding) {
			methodBindings.add(methodViewBinding);
		}

		public void addField(FieldBinding fieldBinding) {
			fieldBindings.add(fieldBinding);
		}
	}

	private static TypeName bestGuess(String type) {
		switch (type) {
			case "void":
				return TypeName.VOID;
			case "boolean":
				return TypeName.BOOLEAN;
			case "byte":
				return TypeName.BYTE;
			case "char":
				return TypeName.CHAR;
			case "double":
				return TypeName.DOUBLE;
			case "float":
				return TypeName.FLOAT;
			case "int":
				return TypeName.INT;
			case "long":
				return TypeName.LONG;
			case "short":
				return TypeName.SHORT;
			default:
				int left = type.indexOf('<');
				if (left != -1) {
					ClassName typeClassName = ClassName.bestGuess(type.substring(0, left));
					List<TypeName> typeArguments = new ArrayList<>();
					do {
						typeArguments.add(WildcardTypeName.subtypeOf(Object.class));
						left = type.indexOf('<', left + 1);
					} while (left != -1);
					return ParameterizedTypeName.get(typeClassName, typeArguments.toArray(new TypeName[typeArguments.size()]));
				}
				return ClassName.bestGuess(type);
		}
	}

	private static TypeName bestGuess(TypeMirror type) {
		switch (type.getKind()) {
			case VOID:
				return TypeName.VOID;
			case BOOLEAN:
				return TypeName.BOOLEAN;
			case BYTE:
				return TypeName.BYTE;
			case CHAR:
				return TypeName.CHAR;
			case DOUBLE:
				return TypeName.DOUBLE;
			case FLOAT:
				return TypeName.FLOAT;
			case INT:
				return TypeName.INT;
			case LONG:
				return TypeName.LONG;
			case SHORT:
				return TypeName.SHORT;
			default:
				return ClassName.get(type);
		}
	}
}
