package sk.compiler.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import static org.apache.commons.lang3.StringUtils.lowerCase;

/**
 * @author sky
 * @version 1.0 on 2018-07-04 下午11:07
 * @see SKInputModel
 */
public class SKInputModel {

	public ClassName		className;

	public String			packageName;

	public String			fieldName;

	public String			name;

	public String			methodName;

	public TypeName			type;					// 数据类型

	public TypeMirror		typeMirror;				// 数据类型

	public boolean			isLazy;					// 是否懒加载

	public boolean			isAutoInput;	// 是否自动加载

	public sk.compiler.model.SKProviderModel skProviderModel;		// 来源数据

	public String			providerKey;

	/**
	 * 生成
	 */
	public void build(ClassName lazyClassName) {
		StringBuilder providerKeyBuilder = new StringBuilder();

		if (type instanceof ParameterizedTypeName) {
			ParameterizedTypeName returnTypeName = ((ParameterizedTypeName) type);

			if (returnTypeName.rawType.equals(lazyClassName)) {
				DeclaredType declaredType = (DeclaredType) typeMirror;
				typeMirror = declaredType.getTypeArguments().get(0);

				type = returnTypeName.typeArguments.get(0);
				isLazy = true;
				name = lowerCase(((ClassName) type).simpleName());
				methodName = ((ClassName) type).simpleName();
				providerKeyBuilder.append(((ClassName) type).reflectionName());
			} else {
				// 组合
				StringBuilder nameBuilder = new StringBuilder();
				StringBuilder methodNameBuilder = new StringBuilder();
				nameBuilder.append(lowerCase(returnTypeName.rawType.simpleName()));
				methodNameBuilder.append(returnTypeName.rawType.simpleName());

				providerKeyBuilder.append(returnTypeName.rawType.reflectionName());
				for (TypeName itemGeneric : returnTypeName.typeArguments) {
					providerKeyBuilder.append("-");
					providerKeyBuilder.append(((ClassName) itemGeneric).reflectionName());

					nameBuilder.append((lowerCase(((ClassName) itemGeneric).simpleName())));
					methodNameBuilder.append(((ClassName) itemGeneric).simpleName());
				}

				name = nameBuilder.toString();
				methodName = methodNameBuilder.toString();

			}
		} else {
			name = lowerCase(((ClassName) type).simpleName());
			methodName = ((ClassName) type).simpleName();

			providerKeyBuilder.append(((ClassName) type).reflectionName());
		}

		providerKey = providerKeyBuilder.toString();
	}
}
