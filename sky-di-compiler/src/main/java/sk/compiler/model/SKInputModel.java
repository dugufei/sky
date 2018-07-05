package sk.compiler.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

/**
 * @author sky
 * @version 1.0 on 2018-07-04 下午11:07
 * @see SKInputModel
 */
public class SKInputModel {

	public ClassName	className;

	public String		packageName;

	public String		name;

	public TypeName		type;		// 数据类型

	public boolean		isLazy;		// 是否懒加载

	public SKProviderModel skProviderModel;//来源数据
}
