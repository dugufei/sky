package sky.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.google.auto.common.MoreElements.getPackage;
import static javax.lang.model.element.Modifier.PUBLIC;
import static sky.compiler.SkyConsts.METHOD_LOAD_INTO;
import static sky.compiler.SkyConsts.NAME_OF_GROUP;
import static sky.compiler.SkyConsts.PACKAGE_OF_GENERATE_FILE;
import static sky.compiler.SkyConsts.SKY_I_MODULE;
import static sky.compiler.SkyConsts.SKY_I_MODULE_PARAM;
import static sky.compiler.SkyConsts.SKY_I_MODULE_PARAM_METHOD_MODEL;
import static sky.compiler.SkyConsts.SKY_I_MODULE_PARAM_MODEL;
import static sky.compiler.SkyConsts.WARNING_TIPS;

/**
 * @author sky
 * @version 1.0 on 2017-07-02 下午9:29
 * @see SkyBind
 */
final class SkyBind {

	private final String			packageName;

	private final SkyBind			parentBinding;

	private final ClassName			className;

	private final ClassName			defaultClassName;

	private final List<SkyMethod>	methodViewBindings;

	private final TypeName			aParent;

	private SkyBind(TypeName aParent, String packageName, ClassName className, ClassName defaultClassName, List<SkyMethod> methodViewBindings, SkyBind parentBinding) {
		this.aParent = aParent;
		this.packageName = packageName;
		this.className = className;
		this.defaultClassName = defaultClassName;
		this.methodViewBindings = methodViewBindings;
		this.parentBinding = parentBinding;
	}

	public JavaFile brewModuleBiz() {

		return JavaFile.builder(PACKAGE_OF_GENERATE_FILE, createModuleBizType()).addFileComment("Generated code from Sky. Do not modify!").build();
	}

	private TypeSpec createModuleBizType() {
		/*
		 * ```ConcurrentHashMap<String, SkyBizModel>```
		 */
		ParameterizedTypeName inputMapTypeOfGroup = ParameterizedTypeName.get(SKY_I_MODULE_PARAM, ClassName.get(String.class), SKY_I_MODULE_PARAM_MODEL);

		/*
		 * Build input param name.
		 */
		ParameterSpec groupParamSpec = ParameterSpec.builder(inputMapTypeOfGroup, "concurrentHashMap").build();

		MethodSpec.Builder loadIntoMethodOfGroupBuilder = MethodSpec.methodBuilder(METHOD_LOAD_INTO).addAnnotation(Override.class).addModifiers(PUBLIC).addParameter(groupParamSpec);


		CodeBlock.Builder builder = CodeBlock.builder();

		builder.add("SkyBizModel skyBizModel = new SkyBizModel($T.class);\n", defaultClassName);

		for (SkyMethod method : methodViewBindings) {

			builder.add("skyBizModel.add($S, new $T($T.class, $S, new Class[] {", method.getName(), SKY_I_MODULE_PARAM_METHOD_MODEL, defaultClassName, method.getName());

			int count = method.getParameters().size();
			for (int i = 0; i < count; i++) {
				if (i == count - 1) {
					builder.add("$T.class}));\n", bestGuess(method.getParameters().get(i).asType()));
				} else {
					builder.add("$T.class, ", bestGuess(method.getParameters().get(i).asType()));
				}
			}
			if(count == 0){
				builder.add("}));\n");
			}
		}

		builder.add("concurrentHashMap.put($S,skyBizModel);\n",defaultClassName.simpleName().toString());

		loadIntoMethodOfGroupBuilder.addCode(builder.build());


		TypeSpec.Builder result = TypeSpec.classBuilder(className);

		result.addJavadoc(WARNING_TIPS);
		result.addSuperinterface(SKY_I_MODULE);
		result.addModifiers(PUBLIC);
		result.addMethod(loadIntoMethodOfGroupBuilder.build());
		return result.build();
	}

	static Builder newBuilder(TypeElement enclosingElement) {
		TypeMirror typeMirror = enclosingElement.asType();
		TypeName targetType = TypeName.get(typeMirror);
		if (targetType instanceof ParameterizedTypeName) {
			targetType = ((ParameterizedTypeName) targetType).rawType;
		}

		String packageName = PACKAGE_OF_GENERATE_FILE;
		String className = NAME_OF_GROUP + enclosingElement.getSimpleName().toString();

		String defaultPakageNameS = getPackage(enclosingElement).getQualifiedName().toString();
		String defaultClassNameS = enclosingElement.getSimpleName().toString();

		ClassName skyClassName = ClassName.get(packageName, className);
		ClassName defaultClassName = ClassName.get(defaultPakageNameS, defaultClassNameS);
		return new Builder(targetType, packageName, skyClassName, defaultClassName);
	}

	static final class Builder {

		private final List<SkyMethod>	methodBindings	= new ArrayList<>();

		private final String			packageName;

		private final ClassName			className;

		private final ClassName			defaultClassName;

		private SkyBind					parentBinding;

		private final TypeName			aParent;

		private Builder(TypeName aParent, String packageName, ClassName className, ClassName defaultClassName) {
			this.aParent = aParent;
			this.packageName = packageName;
			this.className = className;
			this.defaultClassName = defaultClassName;
		}

		SkyBind build() {
			return new SkyBind(aParent, packageName, className, defaultClassName, methodBindings, parentBinding);
		}

		public void setMethodViewBinding(SkyMethod methodViewBinding) {
			methodBindings.add(methodViewBinding);
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
