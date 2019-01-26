package sk.di;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import sk.di.model.SKConstructorsModel;
import sk.di.model.SKDILibraryModel;
import sk.di.model.SKInputClassModel;
import sk.di.model.SKInputModel;
import sk.di.model.SKParamProviderModel;
import sk.di.model.SKProviderModel;
import sk.di.model.SKSourceModel;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static sk.di.SKUtils.lowerCase;
import static sk.di.SkConsts.COLLECTIONS;
import static sk.di.SkConsts.INPUT_IMPL;
import static sk.di.SkConsts.MAP;
import static sk.di.SkConsts.NAME_INPUT;
import static sk.di.SkConsts.NAME_INPUT_IMPL;
import static sk.di.SkConsts.NAME_PROVIDER;
import static sk.di.SkConsts.PROVIDER;
import static sk.di.SkConsts.SK_DC;
import static sk.di.SkConsts.SK_DI;
import static sk.di.SkConsts.SK_HELPER;
import static sk.di.SkConsts.SK_INPUTS;
import static sk.di.SkConsts.SK_I_PROVIDER;
import static sk.di.SkConsts.SK_MANAGE_INTERFACE;
import static sk.di.SkConsts.SK_MAP;
import static sk.di.SkConsts.WARNING_TIPS;

/**
 * @author sky
 * @version 1.0 on 2018-07-05 上午10:42
 * @see SKDICreate
 */
class SKDICreate {

	Map<String, SKProviderModel>		skProviderModels;

	Map<TypeElement, SKInputClassModel>	skInputModels;

	Map<String, SKSourceModel>			skSourceModelMap;

	List<SKDILibraryModel>				skdiLibraryModelList;

	Map<TypeElement, TypeElement>		skSupport;

	public JavaFile brewDI(Map<String, SKProviderModel> skProviderModels, Map<TypeElement, SKInputClassModel> skInputModels, Map<String, SKSourceModel> skSourceModelMap,
			List<SKDILibraryModel> skdiLibraryModelList, Map<TypeElement, TypeElement> skSupport) {
		String packageName = "sk";
		this.skProviderModels = skProviderModels;
		this.skInputModels = skInputModels;
		this.skSourceModelMap = skSourceModelMap;
		this.skdiLibraryModelList = skdiLibraryModelList;
		this.skSupport = skSupport;
		return JavaFile.builder(packageName, createClassDI(packageName)).addFileComment("Generated code from Sk. Do not modify!").build();
	}

	private TypeSpec createClassDI(String packageName) {
		ClassName currentClassName = ClassName.get(packageName, "SKDI");
		ClassName builderName = ClassName.get("", "Builder");

		TypeSpec.Builder skdiBuilder = TypeSpec.classBuilder(currentClassName);

		skdiBuilder.addJavadoc(WARNING_TIPS);
		skdiBuilder.addModifiers(PUBLIC, FINAL);
		// 创建属性 builder
		skdiBuilder.addField(builderName, "builder", PRIVATE);
		// 初始化方法
		MethodSpec.Builder initialize = MethodSpec.methodBuilder("initialize").addModifiers(Modifier.PRIVATE).addParameter(builderName, "builder");
		initialize.addStatement("this.builder = builder");

		// 构造方法
		MethodSpec.Builder constructors = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE);
		constructors.addParameter(builderName, "builder");
		constructors.addStatement("initialize(builder)");

		HashMap<String, ParameterizedTypeName> singleSKDIProvider = new HashMap<>();

		for (SKInputClassModel item : skInputModels.values()) {
			HashMap<String, ParameterizedTypeName> singleImplProvider = new HashMap<>();
			HashMap<String, ClassName> singleImplSource = new HashMap<>();

			String inputNameImpl = item.className.simpleName() + NAME_INPUT_IMPL;
			ClassName inputClassNameImpl = ClassName.get("", inputNameImpl);

			TypeSpec.Builder builderInput = TypeSpec.classBuilder(inputClassNameImpl);

			builderInput.addModifiers(PRIVATE, FINAL);
			builderInput.addSuperinterface(ParameterizedTypeName.get(SK_MANAGE_INTERFACE, item.className));

			// 构造函数
			MethodSpec.Builder clazzConstructors = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE);

			// 重写方法
			MethodSpec.Builder clazzOverride = MethodSpec.methodBuilder("input").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class);

			clazzOverride.addParameter(item.className, lowerCase(item.className.simpleName()));
			clazzOverride.addStatement("input$T($N)", item.className, lowerCase(item.className.simpleName()));
			builderInput.addMethod(clazzOverride.build());

			// 初始化方法
			MethodSpec.Builder initializeImpl = MethodSpec.methodBuilder("initialize").addModifiers(Modifier.PRIVATE);

			// 初始化方法2
			MethodSpec.Builder clazzInput = MethodSpec.methodBuilder("input" + item.className.simpleName()).addModifiers(Modifier.PRIVATE).returns(item.className);

			clazzInput.addParameter(item.className, "instance");

			// 注入
			for (SKInputModel skInputModel : item.skInputModels) {
				// 搜索library
				if (skInputModel.skProviderModel == null) {
					for (SKDILibraryModel skdiLibraryModel : skdiLibraryModelList) {

						if (skdiLibraryModel.skProviderModels != null) {
							skInputModel.skProviderModel = skdiLibraryModel.skProviderModels.get(skInputModel.providerKey);
						}
					}
				}
				if (skInputModel.skProviderModel == null) {
					continue;
				}
				// 初始化
				initProvider(skInputModel.skProviderModel, clazzConstructors, skdiBuilder, builderInput, initialize, initializeImpl, singleSKDIProvider, singleImplProvider, singleImplSource);

				// 注入类名
				String providerName = lowerCase(skInputModel.skProviderModel.getClassName(PROVIDER));
				String inputClass = skInputModel.className.simpleName() + NAME_INPUT;

				ClassName inputClassName = ClassName.get(skInputModel.packageName, inputClass);

				if (skInputModel.isLazy) {
					clazzInput.addStatement("$T.input$N(instance, $T.lazy($N))", inputClassName, skInputModel.methodName, SK_DC, providerName);
				} else {
					clazzInput.addStatement("$T.input$N(instance, $N.get())", inputClassName, skInputModel.methodName, providerName);
				}
			}
			// 构造函数
			clazzConstructors.addStatement("initialize()");
			builderInput.addMethod(clazzConstructors.build());

			clazzInput.addStatement("return instance");
			builderInput.addMethod(initializeImpl.build());
			builderInput.addMethod(clazzInput.build());

			skdiBuilder.addType(builderInput.build());

			// 初始化注入类
			String nameInputImpl = item.className.simpleName() + INPUT_IMPL;

			skdiBuilder.addField(ParameterizedTypeName.get(SK_I_PROVIDER, inputClassNameImpl), nameInputImpl, PRIVATE);

			TypeSpec.Builder comparatorImpl = TypeSpec.anonymousClassBuilder("");
			comparatorImpl.addSuperinterface(ParameterizedTypeName.get(SK_I_PROVIDER, inputClassNameImpl));
			comparatorImpl.addMethod(MethodSpec.methodBuilder("get").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(inputClassNameImpl)
					.addStatement("return new $T()", inputClassNameImpl).build());

			initialize.addStatement("this.$N = $L", nameInputImpl, comparatorImpl.build());

		}

		// 创建内部类 builder
		TypeSpec.Builder builder = TypeSpec.classBuilder(builderName);
		builder.addModifiers(PUBLIC, FINAL, STATIC);

		// 构造方法
		MethodSpec.Builder builderConstructors = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE);
		builder.addMethod(builderConstructors.build());

		MethodSpec.Builder builderConstructorsMethod = MethodSpec.methodBuilder("build").addModifiers(Modifier.PUBLIC).returns(currentClassName);

		HashMap<String, SKConstructorsModel> singleConsProvider = new HashMap<>();
		// 添加library
		for (SKDILibraryModel skdiLibraryModel : skdiLibraryModelList) {
			String name = lowerCase(skdiLibraryModel.name);
			builder.addField(skdiLibraryModel.className, name, PRIVATE);

			String libraryBuilderName = name + "builder";

			ClassName libraryBuilder = skdiLibraryModel.className.nestedClass("Builder");

			FieldSpec fieldBuilder = FieldSpec.builder(libraryBuilder, libraryBuilderName).addModifiers(Modifier.PRIVATE).initializer("$T.builder()", skdiLibraryModel.className).build();

			builder.addField(fieldBuilder);

			for (SKConstructorsModel skConstructorsModel : skdiLibraryModel.skConstructorsModelList) {

				SKConstructorsModel temp = singleConsProvider.get(skConstructorsModel.className.reflectionName());
				String consName = lowerCase(skConstructorsModel.className.simpleName());

				if (temp != null) {
					continue;
				}

				// 增加赋值方法
				builder.addField(skConstructorsModel.className, consName, PRIVATE);

				builder.addMethod(addSetMethod(builderName, libraryBuilderName, skConstructorsModel.className, skConstructorsModel.className.simpleName()));

				singleConsProvider.put(skConstructorsModel.className.reflectionName(), skConstructorsModel);
			}

			builderConstructorsMethod.addStatement("if($N == null)this.$N = $N.build()", name, name, libraryBuilderName);
			// 添加初始化
			constructors.addStatement("$T.init()", SK_HELPER);

			// 增加赋值方法
			builder.addMethod(addEqualMethod(builderName, skdiLibraryModel.className, skdiLibraryModel.className.simpleName()));
		}
		// 来源
		for (SKSourceModel item : skSourceModelMap.values()) {
			if (item.isSingle || item.isSingleGenerate) {
				String name = lowerCase(item.className.simpleName());

				// 构造函数
				if (item.skConstructorsModelList == null) {
					builder.addField(item.className, name, PRIVATE);

					if (item.isLibrary) {
						builderConstructorsMethod.addStatement("if($N == null)this.$N =  this.$N.builder.$N)", name, name, lowerCase(item.classNameLibrary.simpleName()), name);
					} else {
						builderConstructorsMethod.addStatement("if($N == null)this.$N = new $T()", name, name, item.className);
					}
				} else {
					builder.addField(item.className, name, PRIVATE);

					if (item.isLibrary) {
						builderConstructorsMethod.addStatement("if($N == null)this.$N =  this.$N.builder.$N", name, name, lowerCase(item.classNameLibrary.simpleName()), name);
						continue;
					}

					CodeBlock.Builder consCodeBlock = null;

					for (SKConstructorsModel skConstructorsModel : item.skConstructorsModelList) {

						SKConstructorsModel temp = singleConsProvider.get(skConstructorsModel.className.reflectionName());
						String consName = lowerCase(skConstructorsModel.className.simpleName());
						if (consCodeBlock == null) {
							consCodeBlock = CodeBlock.builder();
							consCodeBlock.add("$N", consName);
						} else {
							consCodeBlock.add(",$N", consName);
						}
						if (temp != null) {
							continue;
						}

						builder.addField(skConstructorsModel.className, consName, PRIVATE);

						// 增加赋值方法
						builder.addMethod(addEqualMethod(builderName, skConstructorsModel.className, skConstructorsModel.className.simpleName()));

						singleConsProvider.put(skConstructorsModel.className.reflectionName(), skConstructorsModel);
					}
					if (consCodeBlock == null) {
						builderConstructorsMethod.addStatement("if($N == null)this.$N = new $T()", name, name, item.className);
					} else {
						builderConstructorsMethod.addStatement("if($N == null)this.$N = new $T($N)", name, name, item.className, consCodeBlock.build().toString());
					}
				}

				// 增加赋值方法
				builder.addMethod(addEqualMethod(builderName, item.className, item.className.simpleName()));
			}
		}

		builderConstructorsMethod.addStatement("return new $T(this)", currentClassName);

		builder.addMethod(builderConstructorsMethod.build());

		skdiBuilder.addType(builder.build());

		// 创建内部方法 builder
		MethodSpec.Builder builderMethod = MethodSpec.methodBuilder("builder").addModifiers(Modifier.PUBLIC, STATIC).returns(builderName);
		builderMethod.addStatement("return new $T()", builderName);
		skdiBuilder.addMethod(builderMethod.build());

		MethodSpec.Builder createMethod = MethodSpec.methodBuilder("create").addModifiers(Modifier.PUBLIC, STATIC).returns(currentClassName);
		createMethod.addStatement("return new $T().build()", builderName);
		skdiBuilder.addMethod(createMethod.build());

		TypeName wildcard = WildcardTypeName.subtypeOf(Object.class);
		TypeName classOfAny = ParameterizedTypeName.get(ClassName.get(Class.class), wildcard);

		// support集合
		String supportBuilderName = "getMapOfClassAndSupportClass";
		MethodSpec.Builder supportBuilder = MethodSpec.methodBuilder(supportBuilderName).returns(ParameterizedTypeName.get(MAP, classOfAny, classOfAny));

		int supportSiz = skSupport.size();
		if (supportSiz == 1) {
			for (Map.Entry<TypeElement, TypeElement> entry : skSupport.entrySet()) {
				TypeName valueTypeName = TypeName.get(entry.getValue().asType());

				if (valueTypeName instanceof ParameterizedTypeName) {
					ParameterizedTypeName valueParameter = ((ParameterizedTypeName) valueTypeName);
					valueTypeName = valueParameter.rawType;
				}

				supportBuilder.addStatement("return $T.<$T,$T>singletonMap($T.class,$T.class)", COLLECTIONS, classOfAny, classOfAny, entry.getKey(), valueTypeName);
			}
		} else if (supportSiz > 1) {
			CodeBlock.Builder codeBlock = CodeBlock.builder().add("return $T.<$T,$T>newMapBuilder($L)", SK_MAP, classOfAny, classOfAny, supportSiz);

			for (Map.Entry<TypeElement, TypeElement> entry : skSupport.entrySet()) {
				TypeName valueTypeName = TypeName.get(entry.getValue().asType());

				if (valueTypeName instanceof ParameterizedTypeName) {
					ParameterizedTypeName valueParameter = ((ParameterizedTypeName) valueTypeName);
					valueTypeName = valueParameter.rawType;
				}

				codeBlock.add(".put($T.class,$T.class)", entry.getKey(), valueTypeName);
			}
			supportBuilder.addStatement("$L.build()", codeBlock.build());
		} else {
			supportBuilder.addStatement("return $T.EMPTY_MAP", COLLECTIONS);
		}
		skdiBuilder.addMethod(supportBuilder.build());

		// map集合
		TypeName interfaceSK = ParameterizedTypeName.get(SK_MANAGE_INTERFACE, wildcard);
		TypeName providerSK = ParameterizedTypeName.get(SK_I_PROVIDER, interfaceSK);
		TypeName returnTypeMap = ParameterizedTypeName.get(MAP, classOfAny, providerSK);
		String mapBuilderName = "getMapOfClassOfSKProvider";

		MethodSpec.Builder mapBuilder = MethodSpec.methodBuilder(mapBuilderName).addModifiers(Modifier.PRIVATE).returns(returnTypeMap);

		int size = skInputModels.values().size();
		if (size == 1) {
			for (SKInputClassModel item : skInputModels.values()) {
				String inputNameImpl = item.className.simpleName() + INPUT_IMPL;

				mapBuilder.addStatement("return $T.<$T,$T>singletonMap($T.class,($T)$N)", COLLECTIONS, classOfAny, providerSK, item.className, SK_I_PROVIDER, inputNameImpl);
			}
		} else if (size > 1) {
			CodeBlock.Builder codeBlock = CodeBlock.builder().add("return $T.<$T,$T>newMapBuilder($L)", SK_MAP, classOfAny, providerSK, size);

			for (SKInputClassModel item : skInputModels.values()) {
				String inputNameImpl = item.className.simpleName() + INPUT_IMPL;

				codeBlock.add(".put($T.class,($T)$T.this.$N)", item.className, SK_I_PROVIDER, currentClassName, inputNameImpl);
			}
			mapBuilder.addStatement("$L.build()", codeBlock.build());
		} else {
			mapBuilder.addStatement("return $T.EMPTY_MAP", COLLECTIONS);
		}
		skdiBuilder.addMethod(mapBuilder.build());

		// 动态注入方法
		MethodSpec.Builder newSKDispatchingInputBuilder = MethodSpec.methodBuilder("newSKDispatchingInput").addModifiers(Modifier.PRIVATE).returns(SK_DI);
		newSKDispatchingInputBuilder.addStatement("return new $T($N(),$N())", SK_DI, mapBuilderName, supportBuilderName);
		skdiBuilder.addMethod(newSKDispatchingInputBuilder.build());

		if (skdiLibraryModelList.size() < 1) {
			initialize.addStatement("$T.inputDispatching(newSKDispatchingInput())", SK_INPUTS);
		} else {

			initialize.addStatement("int count=0");

			// 循环
			for (SKDILibraryModel item : skdiLibraryModelList) {
				String name = lowerCase(item.name) + "_MAP";

				initialize.addStatement("$T $N = builder.$N.$N()", returnTypeMap, name, lowerCase(item.name), mapBuilderName);
				initialize.addStatement("count += $N.size()", name);
			}

			initialize.addStatement("$T $N = $N()", returnTypeMap, "MAP", mapBuilderName);
			initialize.addStatement("count += $N.size()", "MAP");

			initialize.addStatement("$T mapBuilder = $T.<$T,$T>newMapBuilder(count)", ParameterizedTypeName.get(SK_MAP, classOfAny, providerSK), SK_MAP, classOfAny, providerSK);

			// 循环
			for (SKDILibraryModel item : skdiLibraryModelList) {
				String name = lowerCase(item.name) + "_MAP";
				initialize.addStatement("mapBuilder.putAll($N)", name);
			}

			initialize.addStatement("mapBuilder.putAll(MAP)");
			initialize.addStatement("$T.inputDispatching(new $T(mapBuilder.build(),$N()))", SK_INPUTS, SK_DI, supportBuilderName);
		}

		skdiBuilder.addMethod(initialize.build());
		skdiBuilder.addMethod(constructors.build());

		return skdiBuilder.build();
	}

	private void initProvider(SKProviderModel skProviderModel, MethodSpec.Builder clazzConstructors, TypeSpec.Builder skdiBuilder, TypeSpec.Builder builderInput, MethodSpec.Builder initialize,
			MethodSpec.Builder initializeImpl, HashMap<String, ParameterizedTypeName> singleSKDIProvider, HashMap<String, ParameterizedTypeName> singleImplProvider,
			HashMap<String, ClassName> singleImplSource) {
		// 根据返回结果找到对应的类
		String providerName = lowerCase(skProviderModel.getClassName(PROVIDER));
		ClassName providerClassName = ClassName.get(skProviderModel.packageName, skProviderModel.getClassName(NAME_PROVIDER));

		// 创建属性
		ParameterizedTypeName fieldProvider = ParameterizedTypeName.get(SK_I_PROVIDER, skProviderModel.returnType);

		if (skProviderModel.isSingle) {
			if (singleSKDIProvider.get(providerName) == null) {
				skdiBuilder.addField(fieldProvider, providerName, PRIVATE);

				CodeBlock.Builder initProvider = generateParams(skProviderModel.parameters, clazzConstructors, skdiBuilder, skdiBuilder, initialize, initialize, singleSKDIProvider, singleSKDIProvider,
						singleImplSource);

				initialize.addStatement("this.$N = $T.provider($T.create(builder.$N$N))", providerName, SK_DC, providerClassName, lowerCase(skProviderModel.className.simpleName()),
						initProvider.build().toString());
				singleSKDIProvider.put(providerName, fieldProvider);
			}
		} else {
			if (singleImplProvider.get(providerName) == null) {

				if (singleSKDIProvider.get(providerName) == null) {
					builderInput.addField(fieldProvider, providerName, PRIVATE);

					CodeBlock.Builder initProvider = generateParams(skProviderModel.parameters, clazzConstructors, skdiBuilder, builderInput, initialize, initializeImpl, singleSKDIProvider,
							singleImplProvider, singleImplSource);
					// 来源
					SKSourceModel skSourceModel = skSourceModelMap.get(skProviderModel.className.reflectionName());

					// 添加来源属性 和 初始化
					String sourceName = lowerCase(skSourceModel.className.simpleName());

					if (skSourceModel.isSingle) {
						// 引用来源属性
						initializeImpl.addStatement("this.$N = $T.create(builder.$N$N)", providerName, providerClassName, lowerCase(skProviderModel.className.simpleName()),
								initProvider.build().toString());
					} else {
						if (initialize.equals(initializeImpl)) {
							skSourceModel.isSingleGenerate = true;
							initializeImpl.addStatement("this.$N = $T.create(builder.$N$N)", providerName, providerClassName, sourceName, initProvider.build().toString());

						} else {
							if (singleImplSource.get(skSourceModel.className.reflectionName()) == null) {
								builderInput.addField(skSourceModel.className, sourceName, PRIVATE);
								// 判断是否是library而来
								if (skProviderModel.isLibrary) {
									if (skSourceModel.isSingle) {
										clazzConstructors.addStatement("this.$N = builder.$N.builder.$N", sourceName, lowerCase(skProviderModel.classNameLibrary.simpleName()), sourceName);
									} else {
										clazzConstructors.addStatement("this.$N = new $T()", sourceName, skSourceModel.className);
									}
								} else {
									clazzConstructors.addStatement("this.$N = new $T()", sourceName, skSourceModel.className);
								}
								singleImplSource.put(skSourceModel.className.reflectionName(), skSourceModel.className);
							}
							initializeImpl.addStatement("this.$N = $T.create($N$N)", providerName, providerClassName, sourceName, initProvider.build().toString());
						}
					}
				}

				singleImplProvider.put(providerName, fieldProvider);
			}
		}
	}

	private CodeBlock.Builder generateParams(List<SKParamProviderModel> parameters, MethodSpec.Builder clazzConstructors, TypeSpec.Builder skdiBuilder, TypeSpec.Builder builderInput,
			MethodSpec.Builder initialize, MethodSpec.Builder initializeImpl, HashMap<String, ParameterizedTypeName> singleSKDIProvider, HashMap<String, ParameterizedTypeName> singleImplProvider,
			HashMap<String, ClassName> singleImplSource) {
		CodeBlock.Builder builder = CodeBlock.builder();

		for (SKParamProviderModel item : parameters) {
			// 寻找来源
			ClassName providerName = (ClassName) item.classType;
			SKProviderModel skProviderModel = skProviderModels.get(providerName.reflectionName());
			initProvider(skProviderModel, clazzConstructors, skdiBuilder, builderInput, initialize, initializeImpl, singleSKDIProvider, singleImplProvider, singleImplSource);

			builder.add(",$N", lowerCase(providerName.simpleName()) + PROVIDER);
		}

		return builder;
	}

	private MethodSpec addSetMethod(ClassName returnName, String fieldName, ClassName parameName, String name) {
		String methodSourceName = "set" + name;
		MethodSpec.Builder setSource = MethodSpec.methodBuilder(methodSourceName).addModifiers(Modifier.PUBLIC).returns(returnName);
		setSource.addParameter(parameName, lowerCase(name));
		setSource.addStatement("this.$N = $N", lowerCase(name), lowerCase(name));
		setSource.addStatement("this.$N.$N($N)", fieldName, methodSourceName, lowerCase(name));
		setSource.addStatement("return this");
		return setSource.build();
	}

	private MethodSpec addEqualMethod(ClassName returnName, ClassName parameName, String name) {
		String methodSourceName = "set" + name;
		MethodSpec.Builder setSource = MethodSpec.methodBuilder(methodSourceName).addModifiers(Modifier.PUBLIC).returns(returnName);
		setSource.addParameter(parameName, lowerCase(name));
		setSource.addStatement("this.$N = $N", lowerCase(name), lowerCase(name));
		setSource.addStatement("return this");
		return setSource.build();
	}
}
