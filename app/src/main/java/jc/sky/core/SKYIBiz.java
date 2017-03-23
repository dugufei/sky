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

	/**
	 * 刷新适配器
	 * 
	 * @param t
	 *            类型
	 * @param <T>
	 *            类型
	 */
	<T> void notifyRecyclerAdatper(T t);
}
