package jc.sky.core;


import jc.sky.modules.structure.SKYStructureModel;

/**
 * Created by sky on 15/2/7. 业务
 */
public interface J2WIBiz {

	void initUI(SKYStructureModel j2WView);

	/**
	 * 清空
	 */
	void detach();

}
