package sk.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import static com.google.auto.common.MoreElements.getPackage;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static sk.compiler.SkConsts.COLLECTIONS;
import static sk.compiler.SkConsts.MAP;
import static sk.compiler.SkConsts.SK_DC;
import static sk.compiler.SkConsts.SK_DI;
import static sk.compiler.SkConsts.SK_HELPER;
import static sk.compiler.SkConsts.SK_I_INPUT;
import static sk.compiler.SkConsts.SK_I_LAZY;
import static sk.compiler.SkConsts.SK_I_PROVIDER;
import static sk.compiler.SkConsts.SK_MANAGE_INTERFACE;
import static sk.compiler.SkConsts.SK_MAP;
import static sk.compiler.SkConsts.SK_PRECOND;
import static sk.compiler.SkConsts.WARNING_TIPS;

/**
 * @author sky
 * @version 1.0 on 2017-07-02 下午9:29
 * @see SkBind
 */
final class SkBind {

	private final String		packageName;

	private final SkBind		parentBinding;

	private final ClassName		className;

	private final ClassName		defaultClassName;

	private final List<SkClass>	classes;

	public final List<SkMethod>	methodViewBindings;

	public final List<SkField>	fields;

	private final TypeName		aParent;

	private SkBind(TypeName aParent, String packageName, ClassName className, ClassName defaultClassName, List<SkClass> classes, List<SkMethod> methodViewBindings, List<SkField> fields,
			SkBind parentBinding) {
		this.aParent = aParent;
		this.packageName = packageName;
		this.className = className;
		this.defaultClassName = defaultClassName;
		this.classes = classes;
		this.methodViewBindings = methodViewBindings;
		this.fields = fields;
		this.parentBinding = parentBinding;
	}

	public JavaFile brewProvider(SkMethod skMethod) {
		String currentPack = packageName + "." + lowerCase(className.simpleName());
		return JavaFile.builder(currentPack, createProvider(currentPack, skMethod)).addFileComment("Generated code from Sk. Do not modify!").build();
	}

	public JavaFile brewInput() {
		String currentPack = packageName;
		return JavaFile.builder(currentPack, createFieldInput(packageName)).addFileComment("Generated code from Sk. Do not modify!").build();
	}

	public JavaFile brewSource(Map<String, SkBind> map) throws IOException {
		String currentPack = "sk";
		return JavaFile.builder(currentPack, createClassInput(currentPack, map)).addFileComment("Generated code from Sk. Do not modify!").build();
	}

	private TypeSpec createClassInput(String packageName, Map<String, SkBind> map) throws IOException {
		ClassName currentClassName = ClassName.get(packageName, "SKDI");
		ClassName builderName = ClassName.get("", "Builder");

		TypeSpec.Builder result = TypeSpec.classBuilder(currentClassName);
		result.addJavadoc(WARNING_TIPS);
		result.addModifiers(PUBLIC, FINAL);
		// 创建属性 builder
		result.addField(builderName, "builder", PRIVATE);
		// 初始化方法
		MethodSpec.Builder initialize = MethodSpec.methodBuilder("initialize").addModifiers(Modifier.PRIVATE).addParameter(builderName, "builder");

		initialize.addStatement("this.builder = builder");

		HashMap<String, ClassName> checkHashMap = new HashMap<>();
		HashMap<String, Integer> nameSHashMap = new HashMap<>();
		HashMap<String, Integer> initHashMap = new HashMap<>();
		HashMap<ClassName, String> countClassName = new HashMap<>();
		// 循环创建属性
		for (SkClass skClass : classes) {
			String nameImpl = skClass.getName() + "_InputImpl";
			ClassName implClassName = ClassName.get("", nameImpl);

			TypeSpec.Builder builder = TypeSpec.classBuilder(implClassName);

			builder.addModifiers(PRIVATE, FINAL);
			builder.addSuperinterface(ParameterizedTypeName.get(SK_MANAGE_INTERFACE, TypeName.get(skClass.getType())));

			// 构造函数
			MethodSpec.Builder clazzConstructors = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE);
			clazzConstructors.addStatement("initialize()");
			builder.addMethod(clazzConstructors.build());

			// 重写方法
			MethodSpec.Builder clazzOverride = MethodSpec.methodBuilder("input").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class);

			clazzOverride.addParameter(TypeName.get(skClass.getType()), lowerCase(skClass.getName()));
			clazzOverride.addStatement("input$N($N)", skClass.getName(), lowerCase(skClass.getName()));
			builder.addMethod(clazzOverride.build());

			// 初始化方法
			MethodSpec.Builder clazzInitialize = MethodSpec.methodBuilder("initialize").addModifiers(Modifier.PRIVATE);

			// 初始化方法2
			MethodSpec.Builder clazzInput = MethodSpec.methodBuilder("input" + skClass.getName()).addModifiers(Modifier.PRIVATE).returns(TypeName.get(skClass.getType()));

			clazzInput.addParameter(TypeName.get(skClass.getType()), "instance");

			// 注入
			for (SkField field : skClass.fields) {

				for (TypeMirror typeMirror : skClass.getParameters()) {
					TypeName typeName = ClassName.get(typeMirror);
					ClassName className = (ClassName) typeName;

					SkBind skBind = map.get(className.simpleName());
					// 排序
					Collections.sort(skBind.methodViewBindings, new Comparator<SkMethod>() {

						@Override public int compare(SkMethod o1, SkMethod o2) {
							int i = o1.getParameters().size() - o2.getParameters().size();
							return i;
						}
					});

					for (SkMethod skMethod : skBind.methodViewBindings) {
						TypeName type = field.getType();
						boolean isLazy = false;

						if (field.getType() instanceof ParameterizedTypeName) {
							ParameterizedTypeName isLazyType = ((ParameterizedTypeName) field.getType());
							if (isLazyType.rawType.equals(SK_I_LAZY)) {
								type = isLazyType.typeArguments.get(0);
								isLazy = true;
							} else {
								type = isLazyType.rawType;
							}
						}
						TypeName methodTypeNameReturn = ClassName.get(skMethod.getReturnType());
						// 注入
						if (type.equals(methodTypeNameReturn)) {

							// 创建子类
							ClassName input = ClassName.get(skBind.packageName, skClass.getName() + "_Input");

							DeclaredType declared = (DeclaredType) skMethod.getReturnType();

							Element supertypeElement = declared.asElement();

							ClassName provider = ClassName.get(skBind.packageName + "." + lowerCase(className.simpleName()), "SK_" + supertypeElement.getSimpleName() + "_Provider");

							// 检查

							CodeBlock.Builder codeBlock = CodeBlock.builder();
							// 创建带参数的类
							for (VariableElement item : skMethod.getParameters()) {
								// 构造函数
								TypeName fieldTypeName = bestGuess(item.asType());
								ParameterizedTypeName fieldParameterizedTypeName = ParameterizedTypeName.get(SK_I_PROVIDER, fieldTypeName);

								String nameProvider = lowerCase(item.getSimpleName().toString()) + "Provider";

								// 创建属性
								if (nameSHashMap.get(nameProvider) == null) {
									result.addField(fieldParameterizedTypeName, nameProvider, PRIVATE);
									nameSHashMap.put(nameProvider, 1);
								}
								// 创建赋值
								createInitializeParams(result, builder, initialize, clazzInitialize, fieldTypeName, map, nameSHashMap, checkHashMap, initHashMap);
								codeBlock.add(",$N", nameProvider);
							}

							// 创建属性
							TypeName fieldTypeName = ParameterizedTypeName.get(SK_I_PROVIDER, methodTypeNameReturn).typeArguments.get(0);
							ClassName fieldClassName = (ClassName) fieldTypeName;
							String nameSProvider = lowerCase(fieldClassName.simpleName()) + "Provider";

							if (skMethod.isSingle()) {

								if (nameSHashMap.get(nameSProvider) == null) {
									result.addField(ParameterizedTypeName.get(SK_I_PROVIDER, methodTypeNameReturn), nameSProvider, PRIVATE);
									nameSHashMap.put(nameSProvider, 0);
								}

								if (initHashMap.get(nameSProvider) == null) {
									initialize.addStatement("this.$N = $T.provider($T.create(builder.$N$N))", nameSProvider, SK_DC, provider, lowerCase(className.simpleName()),
											codeBlock.build().toString());
									initHashMap.put(nameSProvider, 0);
								}
								checkHashMap.put(lowerCase(className.simpleName()), className);
							} else {

								if (nameSHashMap.get(nameSProvider) == null) {
									builder.addField(ParameterizedTypeName.get(SK_I_PROVIDER, methodTypeNameReturn), nameSProvider, PRIVATE);
									nameSHashMap.put(nameSProvider, 0);
								}

								if (initHashMap.get(nameSProvider) == null) {
									clazzInitialize.addStatement("this.$N = $T.provider($T.create(builder.$N$N))", nameSProvider, SK_DC, provider, lowerCase(className.simpleName()),
											codeBlock.build().toString());
									initHashMap.put(nameSProvider, 0);
								}

							}
							ClassName methodTypeName = (ClassName) methodTypeNameReturn;
							String nameSMethod = lowerCase(methodTypeName.simpleName()) + "Provider";

							if (nameSHashMap.get(nameSMethod) != null) {
								if (isLazy) {
									clazzInput.addStatement("$T.input$T(instance, $T.lazy($N.get())", input, type, SK_DC, nameSMethod);
								} else {
									clazzInput.addStatement("$T.input$T(instance, $N.get())", input, type, nameSMethod);
								}
							} else {
								if (isLazy) {
									clazzInput.addStatement("$T.input$T(instance, $T.lazy($T.create($N))", input, type, SK_DC, provider, lowerCase(className.simpleName()));
								} else {
									clazzInput.addStatement("$T.input$T(instance, $T.proxy$T_Provider($N))", input, field.getType(), provider, field.getType(), lowerCase(className.simpleName()));
								}
							}
							field.setBind(true);
							break;
						}
					}

					if (field.isBind()) {
						break;
					}
				}
			}

			// 来源
			for (TypeMirror typeMirror : skClass.getParameters()) {

				TypeName typeName = ClassName.get(typeMirror);

				ClassName className = (ClassName) typeName;

				String name = lowerCase(className.simpleName());

				if (checkHashMap.get(name) != null) {
					continue;
				}

				builder.addField(typeName, name, PRIVATE);

				clazzInitialize.addStatement("this.$N = new $T()", name, className);
			}
			// 初始化类
			// 创建属性
			result.addField(ParameterizedTypeName.get(SK_I_PROVIDER, implClassName), implClassName.simpleName(), PRIVATE);
			// 初始化属性
			TypeSpec.Builder comparatorImpl = TypeSpec.anonymousClassBuilder("");
			comparatorImpl.addSuperinterface(ParameterizedTypeName.get(SK_I_PROVIDER, implClassName));
			comparatorImpl.addMethod(
					MethodSpec.methodBuilder("get").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(implClassName).addStatement("return new $T()", implClassName).build());

			initialize.addStatement("this.$N = $L", implClassName.simpleName(), comparatorImpl.build());

			// 记录属性个数
			countClassName.put((ClassName) TypeName.get(skClass.getType()), implClassName.simpleName());

			builder.addMethod(clazzInitialize.build());

			clazzInput.addStatement("return instance");
			builder.addMethod(clazzInput.build());

			result.addType(builder.build());
		}

		// 创建内部类 builder
		TypeSpec.Builder builder = TypeSpec.classBuilder(builderName);
		builder.addModifiers(PUBLIC, FINAL, STATIC);

		// 构造方法
		MethodSpec.Builder builderConstructors = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE);
		builder.addMethod(builderConstructors.build());

		MethodSpec.Builder builderConstructorsMethod = MethodSpec.methodBuilder("build").addModifiers(Modifier.PUBLIC).returns(currentClassName);

		HashMap<String, String> singleMap = new HashMap<>();

		for (Map.Entry<String, ClassName> skCheckModelEntry : checkHashMap.entrySet()) {
			String name = skCheckModelEntry.getKey();

			if (singleMap.get(name) != null) {
				continue;
			}

			builder.addField(skCheckModelEntry.getValue(), name, PRIVATE);

			builderConstructorsMethod.addStatement("if($N == null)this.$N = new $T()", name, name, skCheckModelEntry.getValue());
			singleMap.put(name, name);
		}

		builderConstructorsMethod.addStatement("return new $T(this)", currentClassName);

		builder.addMethod(builderConstructorsMethod.build());

		result.addType(builder.build());

		// 创建内部方法 builder
		MethodSpec.Builder builderMethod = MethodSpec.methodBuilder("builder").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(builderName);
		builderMethod.addStatement("return new $T()", builderName);
		result.addMethod(builderMethod.build());

		MethodSpec.Builder createMethod = MethodSpec.methodBuilder("create").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(currentClassName);
		createMethod.addStatement("return new $T().build()", builderName);
		result.addMethod(createMethod.build());

		// 构造方法
		MethodSpec.Builder constructors = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE);
		constructors.addParameter(builderName, "builder");
		constructors.addStatement("initialize(builder)");

		// helper
		initialize.addStatement("$T.inputDispatching(newSKDispatchingInput())", SK_HELPER);

		result.addMethod(initialize.build());
		result.addMethod(constructors.build());

		// map集合
		TypeName wildcard = WildcardTypeName.subtypeOf(Object.class);
		TypeName classOfAny = ParameterizedTypeName.get(ClassName.get(Class.class), wildcard);
		TypeName interfaceSK = ParameterizedTypeName.get(SK_MANAGE_INTERFACE, wildcard);
		TypeName providerSK = ParameterizedTypeName.get(SK_I_PROVIDER, interfaceSK);
		String mapBuilderName = "getMapOfClassOfSKProvider";
		MethodSpec.Builder mapBuilder = MethodSpec.methodBuilder(mapBuilderName).addModifiers(Modifier.PRIVATE).returns(ParameterizedTypeName.get(MAP, classOfAny, providerSK));

		int size = countClassName.size();
		if (size == 1) {
			for (Map.Entry entry : countClassName.entrySet()) {
				mapBuilder.addStatement("$T.<$T,$T>singletonMap($T.class,($T)$N)", COLLECTIONS, classOfAny, providerSK, entry.getKey(), SK_I_PROVIDER, entry.getValue());
			}
		} else if (size > 1) {
			CodeBlock.Builder codeBlock = CodeBlock.builder().add("return $T.<$T,$T>newMapBuilder($L)", SK_MAP, classOfAny, providerSK, countClassName.size());

			for (Map.Entry entry : countClassName.entrySet()) {
				codeBlock.add(".put($T.class,($T)$T.this.$N)", entry.getKey(), SK_I_PROVIDER, currentClassName, entry.getValue());
			}
			mapBuilder.addStatement("$L.build()", codeBlock.build());
		}
		result.addMethod(mapBuilder.build());
		// 默认方法
		MethodSpec.Builder newSKDispatchingInputBuilder = MethodSpec.methodBuilder("newSKDispatchingInput").addModifiers(Modifier.PRIVATE).returns(SK_DI);
		newSKDispatchingInputBuilder.addStatement("return new $T($N())", SK_DI, mapBuilderName);
		result.addMethod(newSKDispatchingInputBuilder.build());

		return result.build();
	}

	private TypeSpec createFieldInput(String packageName) {
		ClassName currentClassName = ClassName.get(packageName, className.simpleName() + "_Input");

		TypeSpec.Builder result = TypeSpec.classBuilder(currentClassName);
		result.addJavadoc(WARNING_TIPS);
		result.addSuperinterface(ParameterizedTypeName.get(SK_I_INPUT, className));
		result.addModifiers(PUBLIC);

		// 构造方法
		MethodSpec.Builder constructors = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

		// 添加当前类create方法
		MethodSpec.Builder create = MethodSpec.methodBuilder("create").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(ParameterizedTypeName.get(SK_I_INPUT, className));
		StringBuilder createParameter = new StringBuilder("provider");
		// 重写方法
		MethodSpec.Builder input = MethodSpec.methodBuilder("input").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).addParameter(className, "instance");

		for (SkField skField : fields) {
			TypeName typeName = skField.getType();

			boolean isLazy = false;

			if (typeName instanceof ParameterizedTypeName) {
				ParameterizedTypeName isLazyType = ((ParameterizedTypeName) typeName);
				if (isLazyType.rawType.equals(SK_I_LAZY)) {
					typeName = isLazyType.typeArguments.get(0);
					isLazy = true;
				} else {
					typeName = isLazyType.rawType;
				}
			}

			ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(SK_I_PROVIDER, typeName);
			String name = skField.getName() + "Provider";

			result.addField(parameterizedTypeName, name, Modifier.PRIVATE, Modifier.FINAL);
			constructors.addParameter(parameterizedTypeName, name);
			constructors.addStatement("this.$N = $N", name, name);

			// create
			create.addParameter(parameterizedTypeName, name);
			if (createParameter == null) {
				createParameter = new StringBuilder(name);
			} else {
				createParameter.append(",");
				createParameter.append(name);
			}

			if (isLazy) {
				input.addStatement("input$T(instance,$T.lazy($N))", typeName, SK_DC, name);
			} else {
				input.addStatement("input$T(instance,$N.get())", typeName, name);
			}

			// 每个属性创建方法
			ClassName simpleName = (ClassName) typeName;
			MethodSpec methodSpec = MethodSpec.methodBuilder("input" + simpleName.simpleName()).addModifiers(Modifier.PUBLIC, Modifier.STATIC).addParameter(className, "instance")
					.addParameter(skField.getType(), skField.getName()).addStatement("instance.$N = $N", skField.getName(), skField.getName()).build();

			result.addMethod(methodSpec);
		}

		result.addMethod(input.build());

		// 添加当前类create方法
		create.addStatement("return new $T($N)", currentClassName, createParameter.toString());

		result.addMethod(constructors.build());

		return result.build();
	}

	private TypeSpec createProvider(String currentPack, SkMethod skMethod) {
		DeclaredType declared = (DeclaredType) skMethod.getReturnType(); // you should of course check this is possible first
		Element supertypeElement = declared.asElement();

		String simpleName = supertypeElement.getSimpleName() + "_Provider";

		ClassName currentClassName = ClassName.get(currentPack, "SK_" + simpleName);

		TypeSpec.Builder result = TypeSpec.classBuilder(currentClassName);

		result.addJavadoc(WARNING_TIPS);
		result.addSuperinterface(ParameterizedTypeName.get(SK_I_PROVIDER, bestGuess(skMethod.getReturnType())));
		result.addModifiers(PUBLIC);

		// 构造方法
		MethodSpec.Builder constructors = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addParameter(className, "provider").addStatement("this.$N = $N", "provider", "provider");
		result.addField(className, "provider", Modifier.PRIVATE, Modifier.FINAL);

		// 重写方法参数个数
		StringBuilder overrideParameter = new StringBuilder("provider");

		// 静态方法 proxy
		String currentName = "proxy" + simpleName;
		MethodSpec.Builder provideProxy = MethodSpec.methodBuilder(currentName).addModifiers(Modifier.PUBLIC, Modifier.STATIC).addParameter(className, "provider")
				.returns(bestGuess(skMethod.getReturnType()));
		StringBuilder proxyParameter = null;

		// 静态方法 Instance
		MethodSpec.Builder provideInstance = MethodSpec.methodBuilder("provideInstance").addModifiers(Modifier.PUBLIC, Modifier.STATIC).addParameter(className, "provider")
				.returns(bestGuess(skMethod.getReturnType()));
		StringBuilder instanceParameter = new StringBuilder("provider");
		// 添加当前类create方法
		MethodSpec.Builder create = MethodSpec.methodBuilder("create").addModifiers(Modifier.PUBLIC, Modifier.STATIC).addParameter(className, "provider").returns(currentClassName);
		StringBuilder createParameter = new StringBuilder("provider");

		// 参数
		for (VariableElement item : skMethod.getParameters()) {
			// 构造函数
			TypeName typeName = bestGuess(item.asType());
			ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(SK_I_PROVIDER, typeName);

			String name = lowerCase(item.getSimpleName().toString());
			String nameProvider = lowerCase(item.getSimpleName().toString()) + "Provider";

			// 构造函数
			result.addField(parameterizedTypeName, nameProvider, Modifier.PRIVATE, Modifier.FINAL);
			constructors.addParameter(parameterizedTypeName, nameProvider);
			constructors.addStatement("this.$N = $N", nameProvider, nameProvider);

			// 重发方法参数个数累加
			overrideParameter.append(",");
			overrideParameter.append(nameProvider);

			// proxy
			provideProxy.addParameter(typeName, lowerCase(item.getSimpleName().toString()));
			if (proxyParameter == null) {
				proxyParameter = new StringBuilder(name);
			} else {
				proxyParameter.append(",");
				proxyParameter.append(name);
			}

			// instance
			provideInstance.addParameter(parameterizedTypeName, nameProvider);
			instanceParameter.append(",");
			instanceParameter.append(nameProvider);
			instanceParameter.append(".get()");

			// create
			create.addParameter(parameterizedTypeName, nameProvider);
			createParameter.append(",");
			createParameter.append(nameProvider);
		}
		// 添加构造方法
		result.addMethod(constructors.build());

		// 添加重写方法
		MethodSpec get = MethodSpec.methodBuilder("get").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(bestGuess(skMethod.getReturnType()))
				.addStatement("return provideInstance($N)", overrideParameter.toString()).build();
		result.addMethod(get);

		// 添加 proxy
		provideProxy.addStatement("return $T.checkNotNull(provider.$N($N), \"Cannot return null from a non-@Nullable @Provides method\")", SK_PRECOND, skMethod.getName(),
				proxyParameter == null ? "" : proxyParameter.toString());
		result.addMethod(provideProxy.build());

		// 添加静态方法 Instance
		provideInstance.addStatement("return $N($N)", currentName, instanceParameter.toString());
		result.addMethod(provideInstance.build());

		// 添加当前类create方法
		create.addStatement("return new $T($N)", currentClassName, createParameter.toString());
		result.addMethod(create.build());

		return result.build();
	}

	private String upperCase(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	private String lowerCase(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	static Builder newBuilder(TypeElement enclosingElement) {
		TypeMirror typeMirror = enclosingElement.asType();
		TypeName targetType = TypeName.get(typeMirror);
		if (targetType instanceof ParameterizedTypeName) {
			targetType = ((ParameterizedTypeName) targetType).rawType;
		}
		String defaultPakageNameS = getPackage(enclosingElement).getQualifiedName().toString();
		String defaultClassNameS = enclosingElement.getSimpleName().toString();

		ClassName skyClassName = ClassName.get(defaultPakageNameS, defaultClassNameS);
		ClassName defaultClassName = ClassName.get(defaultPakageNameS, defaultClassNameS);
		return new Builder(targetType, defaultPakageNameS, skyClassName, defaultClassName);
	}

	public void inpuName(Map<String, SkBind> map) {
		map.put(className.simpleName(), this);
	}

	//
	static final class Builder {

		private final List<SkMethod>	methodBindings	= new ArrayList<>();

		private final List<SkField>		fields			= new ArrayList<>();

		private final List<SkClass>		classes			= new ArrayList<>();

		private final String			packageName;

		private final ClassName			className;

		private final ClassName			defaultClassName;

		private SkBind					parentBinding;

		private final TypeName			aParent;

		private Builder(TypeName aParent, String packageName, ClassName className, ClassName defaultClassName) {
			this.aParent = aParent;
			this.packageName = packageName;
			this.className = className;
			this.defaultClassName = defaultClassName;
		}

		SkBind build() {
			return new SkBind(aParent, packageName, className, defaultClassName, classes, methodBindings, fields, parentBinding);
		}

		public List<SkMethod> getMethodBindings() {
			return methodBindings;
		}

		public void setMethodViewBinding(SkMethod methodViewBinding) {
			methodBindings.add(methodViewBinding);
		}

		public void setFieldBinding(SkField skField) {
			fields.add(skField);
		}

		public void setClassBinding(SkClass skClass) {
			classes.add(skClass);
		}

		public ClassName getClassName() {
			return className;
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

	private void createInitializeParams(TypeSpec.Builder result, TypeSpec.Builder builder, MethodSpec.Builder initialize, MethodSpec.Builder clazzInitialize, TypeName fieldTypeName,
			Map<String, SkBind> map, HashMap<String, Integer> nameSHashMap, HashMap<String, ClassName> checkHashMap, HashMap<String, Integer> initHashMap) {

		for (SkClass skClass : classes) {

			for (TypeMirror typeMirror : skClass.getParameters()) {
				TypeName typeName = ClassName.get(typeMirror);
				ClassName className = (ClassName) typeName;

				SkBind skBind = map.get(className.simpleName());

				for (SkMethod skMethod : skBind.methodViewBindings) {

					TypeName methodTypeName = ClassName.get(skMethod.getReturnType());

					if (fieldTypeName.equals(methodTypeName)) {
						DeclaredType declared = (DeclaredType) skMethod.getReturnType(); // you should of course check this is possible first
						Element supertypeElement = declared.asElement();
						ClassName provider = ClassName.get(skBind.packageName + "." + lowerCase(className.simpleName()), "SK_" + supertypeElement.getSimpleName() + "_Provider");

						CodeBlock.Builder codeBlock = CodeBlock.builder();
						// 创建带参数的类
						for (VariableElement item : skMethod.getParameters()) {
							// 构造函数
							TypeName fieldTypeName1 = bestGuess(item.asType());
							ParameterizedTypeName fieldParameterizedTypeName1 = ParameterizedTypeName.get(SK_I_PROVIDER, fieldTypeName1);

							String nameProvider1 = lowerCase(item.getSimpleName().toString()) + "Provider";

							// 创建属性
							if (nameSHashMap.get(nameProvider1) == null) {
								result.addField(fieldParameterizedTypeName1, nameProvider1, PRIVATE);
								nameSHashMap.put(nameProvider1, 1);
							}
							// 创建赋值
							createInitializeParams(result, builder, initialize, clazzInitialize, fieldTypeName1, map, nameSHashMap, checkHashMap, initHashMap);
							codeBlock.add(",$N", nameProvider1);
						}

						// 创建属性
						TypeName fieldTypeName1 = ParameterizedTypeName.get(SK_I_PROVIDER, methodTypeName).typeArguments.get(0);
						ClassName fieldClassName1 = (ClassName) fieldTypeName1;
						String nameS = lowerCase(fieldClassName1.simpleName()) + "Provider";

						if (nameSHashMap.get(nameS) == null) {
							if (skMethod.isSingle()) {
								result.addField(ParameterizedTypeName.get(SK_I_PROVIDER, methodTypeName), nameS, PRIVATE);
							} else {
								builder.addField(ParameterizedTypeName.get(SK_I_PROVIDER, methodTypeName), nameS, PRIVATE);
							}
							nameSHashMap.put(nameS, 0);
						}
						if (initHashMap.get(nameS) == null) {
							if (skMethod.isSingle()) {
								initialize.addStatement("this.$N = $T.provider($T.create(builder.$N$N))", nameS, SK_DC, provider, lowerCase(className.simpleName()), codeBlock.build().toString());
							} else {
								initialize.addStatement("this.$N = $T.create(builder.$N$N)", nameS, provider, lowerCase(className.simpleName()), codeBlock.build().toString());
							}
							initHashMap.put(nameS, 1);
						}
						checkHashMap.put(lowerCase(className.simpleName()), className);
					}
				}
			}
		}
	}
}
