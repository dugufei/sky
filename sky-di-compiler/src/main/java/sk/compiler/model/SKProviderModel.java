package sk.compiler.model;

import com.squareup.javapoet.ClassName;
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

	public List<SKParamProviderModel>	parameters;	// 参数

	public TypeName						returnType;	// 返回值

	public boolean						isSingle;	// 是否单例
}
