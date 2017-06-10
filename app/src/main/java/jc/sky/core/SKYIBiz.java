package jc.sky.core;

import jc.sky.modules.structure.SKYStructureModel;

/**
 * @author sky
 * @version 版本
 */
public interface SKYIBiz {

	void initUI(SKYStructureModel SKYView);

	void initBundle();

	/**
	 * 清空
	 */
	void detach();

	/**
	 * 刷新适配器
	 *
	 * @param o
	 *            参数
	 * 
	 */
	<O> void refreshAdapter(O o);
}
