package jc.sky.core;


import jc.sky.modules.structure.SKYStructureModel;

/**
 * Created by sky on 15/2/7. 业务
 */
public interface SKYIBiz {

	void initUI(SKYStructureModel SKYView);

	/**
	 * 清空
	 */
	void detach();

}
