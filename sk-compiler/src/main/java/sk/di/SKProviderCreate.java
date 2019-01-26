package sk.di;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import sk.di.model.SKParamProviderModel;
import sk.di.model.SKProviderModel;

import static javax.lang.model.element.Modifier.PUBLIC;
import static sk.di.SKUtils.lowerCase;
import static sk.di.SkConsts.NAME_PROVIDER;
import static sk.di.SkConsts.SK_HELPER;
import static sk.di.SkConsts.SK_I_PROVIDER;
import static sk.di.SkConsts.SK_PRECOND;
import static sk.di.SkConsts.SK_PROXY;
import static sk.di.SkConsts.SK_REPOSITORY;
import static sk.di.SkConsts.WARNING_TIPS;

/**
 * @author sky
 * @version 1.0 on 2018-07-04 下午5:08
 * @see SKProviderCreate
 */
class SKProviderCreate {

	JavaFile brewProvider(SKProviderModel item) {
		return JavaFile.builder(item.packageName, createProvider(item)).addFileComment("Generated code from Sk. Do not modify!").build();
	}

	private TypeSpec createProvider(SKProviderModel item) {
		String providerName = item.getClassName(NAME_PROVIDER);

		ClassName providerClassName = ClassName.get(item.packageName, providerName);

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(providerClassName);

		classBuilder.addJavadoc(WARNING_TIPS);
		classBuilder.addSuperinterface(ParameterizedTypeName.get(SK_I_PROVIDER, item.returnType));
		classBuilder.addModifiers(PUBLIC);

		// 添加属性
		classBuilder.addField(item.className, "source", Modifier.PRIVATE, Modifier.FINAL);

		// 构造方法
		MethodSpec.Builder constructors = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addParameter(item.className, "source").addStatement("this.$N = $N", "source", "source");

		// 重写方法参数个数
		StringBuilder overrideParameter = new StringBuilder("source");

		// 静态方法 proxy
		String currentName = "proxy" + providerName;
		MethodSpec.Builder provideProxy = MethodSpec.methodBuilder(currentName).addModifiers(Modifier.PUBLIC, Modifier.STATIC).addParameter(item.className, "source").returns(item.returnType);
		StringBuilder proxyParameter = null;

		// 静态方法 Instance
		MethodSpec.Builder provideInstance = MethodSpec.methodBuilder("provideInstance").addModifiers(Modifier.PUBLIC, Modifier.STATIC).addParameter(item.className, "source").returns(item.returnType);

		StringBuilder instanceParameter = new StringBuilder("source");

		// 添加当前类create方法
		MethodSpec.Builder create = MethodSpec.methodBuilder("create").addModifiers(Modifier.PUBLIC, Modifier.STATIC).addParameter(item.className, "source").returns(providerClassName);
		StringBuilder createParameter = new StringBuilder("source");

		for (SKParamProviderModel skParamProviderModel : item.parameters) {

			String name = lowerCase(skParamProviderModel.name);
			String nameProvider = lowerCase(skParamProviderModel.name) + "Provider";

			// 添加属性
			classBuilder.addField(skParamProviderModel.providerType, nameProvider, Modifier.PRIVATE, Modifier.FINAL);

			// 构造函数
			constructors.addParameter(skParamProviderModel.providerType, nameProvider);
			constructors.addStatement("this.$N = $N", nameProvider, nameProvider);

			// 重发方法参数个数累加
			overrideParameter.append(",");
			overrideParameter.append(nameProvider);

			// proxy
			provideProxy.addParameter(skParamProviderModel.classType, name);
			if (proxyParameter == null) {
				proxyParameter = new StringBuilder(name);
			} else {
				proxyParameter.append(",");
				proxyParameter.append(name);
			}

			// instance
			provideInstance.addParameter(skParamProviderModel.providerType, nameProvider);
			instanceParameter.append(",");
			instanceParameter.append(nameProvider);
			instanceParameter.append(".get()");

			// create
			create.addParameter(skParamProviderModel.providerType, nameProvider);
			createParameter.append(",");
			createParameter.append(nameProvider);
		}

		// 添加构造方法
		classBuilder.addMethod(constructors.build());

		// 添加重写方法
		MethodSpec get = MethodSpec.methodBuilder("get").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(item.returnType)
				.addStatement("return provideInstance($N)", overrideParameter.toString()).build();
		classBuilder.addMethod(get);

		// 添加 proxy
		TypeName returnClassName= item.returnType;
		String returnName;
		if (item.returnType instanceof ParameterizedTypeName) {
			ParameterizedTypeName returnTypeName = ((ParameterizedTypeName) item.returnType);
			returnName = returnTypeName.rawType.simpleName();
		}else {
			ClassName className = (ClassName) item.returnType;
			returnName = lowerCase(className.simpleName());
		}


		if (item.isProxy) {

			if(item.isClass){
				provideProxy.addStatement("$T $N = $T.checkNotNull(new $T(), \"Cannot return null from a non-@Nullable @Provides method\")", returnClassName, returnName, SK_PRECOND, returnClassName);
			}else {
				provideProxy.addStatement("$T $N = $T.checkNotNull(source.$N($N), \"Cannot return null from a non-@Nullable @Provides method\")", returnClassName, returnName, SK_PRECOND, item.name,
						proxyParameter == null ? "" : proxyParameter.toString());
			}

			provideProxy.beginControlFlow("if($N instanceof $T)", returnName, SK_REPOSITORY);

			provideProxy.addStatement("$T $N = $T.bizStore().createProxy($T.class,$N)", SK_PROXY, lowerCase(SK_PROXY.simpleName()), SK_HELPER, returnClassName, returnName);
			provideProxy.addStatement("$N.repository = ($T) $N.proxy", returnName, returnClassName, lowerCase(SK_PROXY.simpleName()));
			provideProxy.endControlFlow();
			provideProxy.addStatement("return $N", returnName);
		} else {
			if(item.isClass){
				provideProxy.addStatement("return $T.checkNotNull(new $T(), \"Cannot return null from a non-@Nullable @Provides method\")", SK_PRECOND,returnClassName);
			}else {
				provideProxy.addStatement("return $T.checkNotNull(source.$N($N), \"Cannot return null from a non-@Nullable @Provides method\")", SK_PRECOND, item.name,
						proxyParameter == null ? "" : proxyParameter.toString());
			}

		}
		classBuilder.addMethod(provideProxy.build());

		// 添加静态方法 Instance
		provideInstance.addStatement("return $N($N)", currentName, instanceParameter.toString());
		classBuilder.addMethod(provideInstance.build());

		// 添加当前类create方法
		create.addStatement("return new $T($N)", providerClassName, createParameter.toString());
		classBuilder.addMethod(create.build());

		return classBuilder.build();
	}

}
