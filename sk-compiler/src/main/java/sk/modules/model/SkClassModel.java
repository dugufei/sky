package sk.modules.model;

import com.squareup.javapoet.ClassName;

import java.util.List;

/**
 * @author sky
 * @version 1.0 on 2018-12-26 5:24 PM
 * @see SkClassModel
 */
public class SkClassModel {

	public ClassName			className;

	public String				packageName;

	public List<SkFieldModel>	skFieldModels;
}
