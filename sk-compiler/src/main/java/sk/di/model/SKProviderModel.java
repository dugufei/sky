package sk.di.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.List;

/**
 * @author sky
 * @version 1.0 on 2018-07-04 下午5:06
 * @see SKProviderModel
 */
public class SKProviderModel {

	public ClassName					className;

	public String						packageName;

	public String						name;

	public List<SKParamProviderModel>	parameters;			// 参数

	public TypeName						returnType;			// 返回值

	public List<TypeName>				returnTypeGenerics;	// 返回值 - 泛型

	public boolean						isSingle;			// 是否单例

	public boolean						isLibrary;			// 是否是外部

	public boolean						isProxy;			// 是否代理

	public boolean						isClass;			// 是否是类

	public ClassName					classNameLibrary;	// 外部 class

	public String						key;

	/**
	 * 生成
	 */
	public void buildKey() {
		StringBuilder stringBuilder = new StringBuilder();

		if (returnType instanceof ParameterizedTypeName) {
			ParameterizedTypeName returnTypeName = ((ParameterizedTypeName) returnType);
			returnTypeGenerics = returnTypeName.typeArguments;
			// 组合
			stringBuilder.append((returnTypeName.rawType).reflectionName());
			for (TypeName itemGeneric : returnTypeGenerics) {
				stringBuilder.append("-");
				stringBuilder.append(((ClassName) itemGeneric).reflectionName());
			}
		} else {
			stringBuilder.append(((ClassName) returnType).reflectionName());
		}

		key = stringBuilder.toString();
	}

	public String getClassName(String provider) {
		StringBuilder providerName = new StringBuilder();

		if (returnType instanceof ParameterizedTypeName) {
			ParameterizedTypeName returnTypeName = ((ParameterizedTypeName) returnType);
			providerName.append(returnTypeName.rawType.simpleName());
			for (TypeName itemGeneric : returnTypeGenerics) {
				providerName.append("_");
				providerName.append(((ClassName) itemGeneric).simpleName());
			}
			providerName.append(provider);
		} else {
			ClassName type = (ClassName) returnType;
			providerName.append(type.simpleName());
			providerName.append(provider);
		}

		return providerName.toString();
	}
}
