package sky.compiler.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.List;

/**
 * @author sky
 * @version 1.0 on 2018-07-04 下午5:06
 * @see SkyModuleModel
 */
public class SkyModuleModel {

	public ClassName					className;

	public String						packageName;

	public String						name;

	public int							nameCode;

	public List<SkyParamProviderModel>	parameters;	// 参数

	public TypeName						returnType;	// 返回值

	public boolean						isStatic;

	public String						key;

	/**
	 * 生成
	 */
	public void buildKey() {
		StringBuilder stringBuilder = new StringBuilder(className.simpleName() + "$$" + name);
		key = stringBuilder.toString();
	}

	public String getClassName(String provider) {
		StringBuilder providerName = new StringBuilder();
		providerName.append(key);


		int count = parameters.size();
		if(count > 0){
			providerName.append("$$");
		}
		for (int i = 0; i < count; i++) {
			SkyParamProviderModel skyParamProviderModel = parameters.get(i);
			ClassName className = (ClassName)skyParamProviderModel.classType.box();
			providerName.append(className.simpleName());
		}
		providerName.append(provider);
		return providerName.toString();
	}
}
