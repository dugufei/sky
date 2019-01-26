package sk.di.model;

import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

/**
 * @author sky
 * @version 1.0 on 2018-07-04 下午5:10
 * @see SKParamProviderModel
 */
public class SKParamProviderModel {

	public String					packageName;

	public String					name;

	public TypeName					classType;

	public ParameterizedTypeName	providerType;

}
