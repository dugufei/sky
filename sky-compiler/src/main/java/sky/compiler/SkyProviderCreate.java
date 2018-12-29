package sky.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import javax.lang.model.element.Modifier;

import sky.compiler.model.SkyModuleModel;
import sky.compiler.model.SkyParamProviderModel;

import static com.squareup.javapoet.TypeName.VOID;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static sky.compiler.SkyConsts.NAME_METHOD;
import static sky.compiler.SkyConsts.SKY_HELPER;
import static sky.compiler.SkyConsts.SKY_I_METHOD;
import static sky.compiler.SkyConsts.SKY_L;
import static sky.compiler.SkyConsts.WARNING_TIPS;

/**
 * @author sky
 * @version 1.0 on 2018-07-04 下午5:08
 * @see SkyProviderCreate
 */
class SkyProviderCreate {

	JavaFile brewProvider(SkyModuleModel item) {
		return JavaFile.builder(item.packageName, createProvider(item)).addFileComment("Generated code from Sk. Do not modify!").build();
	}

	private TypeSpec createProvider(SkyModuleModel item) {
		String providerName = item.getClassName(NAME_METHOD);

		ClassName providerClassName = ClassName.get(item.packageName, providerName);

		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(providerClassName);

		classBuilder.addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
				.addMember("value", "$L", "{\"unchecked\",\"rawtypes\"}").build());
		classBuilder.addJavadoc(WARNING_TIPS);
		classBuilder.addSuperinterface(SKY_I_METHOD);
		classBuilder.addModifiers(PUBLIC,FINAL);

		//添加属性
		FieldSpec number = FieldSpec.builder(int.class, "METHOD_NUMBER")
				.addModifiers(Modifier.PUBLIC, FINAL)
				.initializer(String.valueOf(item.nameCode))
				.build();

		classBuilder.addField(number);

		// 添加方法
		MethodSpec.Builder defaultMethodBuilder = MethodSpec.methodBuilder(item.name).addModifiers(Modifier.PRIVATE).returns(item.returnType);

		// 方法参数
		StringBuilder parameter = new StringBuilder();

		int count = item.parameters.size();
		for (int i = 0; i < count; i++) {
			SkyParamProviderModel skyParamProviderModel = item.parameters.get(i);
			defaultMethodBuilder.addParameter(skyParamProviderModel.classType, skyParamProviderModel.name);
			parameter.append(skyParamProviderModel.name);
			parameter.append(",");
		}
		if (count > 0) {
			parameter.deleteCharAt(parameter.length() - 1);
		}
		if(item.isStatic){
			if(item.returnType == VOID){
				if(count > 0) {
					defaultMethodBuilder.addStatement("$T.$N($N)", item.className, item.name, parameter.toString());
				}else {
					defaultMethodBuilder.addStatement("$T.$N()", item.className, item.name);
				}
			}else {
				if(count > 0) {
					defaultMethodBuilder.addStatement("return $T.$N($N)", item.className, item.name, parameter.toString());
				}else {
					defaultMethodBuilder.addStatement("return $T.$N()", item.className, item.name);
				}
			}
		}else {
			if(item.returnType == VOID){
				if(count > 0){
					defaultMethodBuilder.addStatement("$T.biz($T.class).$N($N)", SKY_HELPER, item.className, item.name, parameter.toString());
				}else {
					defaultMethodBuilder.addStatement("$T.biz($T.class).$N()", SKY_HELPER, item.className, item.name);
				}
			}else {
				if(count > 0){
					defaultMethodBuilder.addStatement("return $T.biz($T.class).$N($N)", SKY_HELPER, item.className, item.name, parameter.toString());
				}else {
					defaultMethodBuilder.addStatement("return $T.biz($T.class).$N()", SKY_HELPER, item.className, item.name);
				}
			}
		}

		classBuilder.addMethod(defaultMethodBuilder.build());

		// 添加重写方法
		MethodSpec.Builder runMethod = MethodSpec.methodBuilder("run").addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).addTypeVariable(TypeVariableName.get("T"))
				.returns(TypeVariableName.get("T"));

		runMethod.addParameter(Object[].class, "params", FINAL).varargs(true);
		// 添加内容
		CodeBlock.Builder codeBlock = CodeBlock.builder();

		// 多0判断
		if (count > 0) {
			//限制条件
			codeBlock.beginControlFlow("if(params.length < 1)");
			codeBlock.add("$T.i(\"参数个数有问题,请确认数量\");\n",SKY_L);
			codeBlock.add("return null;\n");
			codeBlock.endControlFlow();

			for (int i = 0; i < count; i++) {
				SkyParamProviderModel skyParamProviderModel = item.parameters.get(i);
				codeBlock.beginControlFlow("if (!(params[$L] instanceof $T))",i, skyParamProviderModel.classType.box());
				codeBlock.add("$T.i(\"第%d个参数类型错误,应该是 %s\", $L, \"$T\");\n", SKY_L, i, skyParamProviderModel.classType.box());
				codeBlock.add("return null;\n");
				codeBlock.endControlFlow();
			}
			//调用方法
			StringBuilder runParameter = new StringBuilder();

			for (int i = 0; i < count; i++) {
				SkyParamProviderModel skyParamProviderModel = item.parameters.get(i);
				// 调用参数
				if (i == 0) {
					if(item.returnType == VOID){
						codeBlock.add("$N(($T) params[$L]", item.name, skyParamProviderModel.classType.box(), i);
					}else {
						codeBlock.add("Object obj = $N(($T) params[$L]", item.name, skyParamProviderModel.classType.box(), i);
					}
				} else {
					codeBlock.add(",($T) params[$L]", skyParamProviderModel.classType.box(), i);
				}
				runParameter.append("($T) params[$L]");
			}

			if(item.returnType != VOID){
				codeBlock.add(");\nreturn (T)obj");
			}else {
				codeBlock.add(")");
			}
			runMethod.addStatement(codeBlock.build());

			if(item.returnType == VOID){
				runMethod.addStatement("return null");
			}
		} else {
			if(item.returnType == VOID){
				runMethod.addStatement("$N()", item.name);
				runMethod.addStatement("return null");
			}else {
				runMethod.addStatement("Object obj = $N()", item.name);
				runMethod.addStatement("return (T)obj");
			}
		}
		classBuilder.addMethod(runMethod.build());

		return classBuilder.build();
	}

}
