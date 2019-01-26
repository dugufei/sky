package sk.di.model;

import com.squareup.javapoet.ClassName;

import java.util.List;

/**
 * @author sky
 * @version 1.0 on 2018-07-05 上午9:50
 * @see SKInputClassModel
 */
public class SKInputClassModel {

	public ClassName			className;

	public String				packageName;

	public List<SKInputModel>	skInputModels;
}
