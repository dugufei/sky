package jc.sky.view.adapter;

/**
 * @author sky
 * @version 版本
 */
public interface SKYListViewMultiLayout {

	/**
	 * 类型
	 * 
	 * @param position
	 *            参数
	 * @return 返回值
	 */
	int getSKYViewType(int position);

	/**
	 * 类型数量
	 * 
	 * @return 返回值
	 */
	int getSKYViewTypeCount();

	/**
	 * 根据类型获取适配器Item
	 * 
	 * @param type
	 *            参数
	 * @return 返回值
	 */
	SKYAdapterItem getSKYAdapterItem(int type);
}