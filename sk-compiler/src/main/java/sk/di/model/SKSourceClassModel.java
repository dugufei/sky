package sk.di.model;

import java.util.List;

import com.squareup.javapoet.ClassName;

/**
 * @author sky
 * @version 1.0 on 2018-07-05 下午3:36
 * @see SKSourceClassModel
 */
public class SKSourceClassModel {

	public ClassName					className;

	public ClassName					classNameLibrary;

	public boolean						isSingle;

	public boolean						isLibrary;

	public boolean						isSingleGenerate;

	public List<SKConstructorsModel>	skConstructorsModelList;
}
