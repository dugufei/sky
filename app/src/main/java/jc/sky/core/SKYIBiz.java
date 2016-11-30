package jc.sky.core;

import jc.sky.modules.structure.SKYStructureModel;

/**
 * @author sky
 * @version 版本
 */
public interface SKYIBiz {

	void initUI(SKYStructureModel SKYView);

	/**
	 * 清空
	 */
	void detach();
}
