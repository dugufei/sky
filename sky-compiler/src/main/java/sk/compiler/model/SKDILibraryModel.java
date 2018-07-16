package sk.compiler.model;

import com.squareup.javapoet.ClassName;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;

/**
 * @author sky
 * @version 1.0 on 2018-07-09 下午4:45
 * @see SKDILibraryModel
 */
public class SKDILibraryModel {

	public String						packageName;

	public String						name;

	public ClassName					className;

	public boolean						isSKDefaultLibrary;

	public Map<String, SKProviderModel>	skProviderModels;

	public List<SKConstructorsModel>	skConstructorsModelList;

}
