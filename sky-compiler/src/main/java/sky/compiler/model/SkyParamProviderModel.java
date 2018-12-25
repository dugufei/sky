package sky.compiler.model;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

/**
 * @author sky
 * @version 1.0 on 2018-07-04 下午5:10
 * @see SkyParamProviderModel
 */
public class SkyParamProviderModel {

	public String					packageName;

	public String					name;

	public TypeName					classType;

	public ParameterizedTypeName	providerType;

}
