package sk.compiler.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

/**
 * @author sky
 * @version 1.0 on 2018-07-11 下午1:46
 * @see SKConstructorsModel
 */
public class SKConstructorsModel {

	public ClassName	className;

	public String		packageName;

	public String		fieldName;

	public TypeName		type;		// 数据类型

	public TypeMirror	typeMirror;	// 数据类型

}
