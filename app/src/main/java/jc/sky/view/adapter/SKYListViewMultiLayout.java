package jc.sky.view.adapter;

/**
 * @创建人 sky
 * @创建时间 15/7/13 上午9:39
 * @类描述 ListView 多布局接口
 */
public interface SKYListViewMultiLayout {

	/**
	 * 类型
	 * 
	 * @param position
	 * @return
	 */
	int getSKYViewType(int position);

	/**
	 * 类型数量
	 * 
	 * @return
	 */
	int getSKYViewTypeCount();

	/**
	 * 根据类型获取适配器Item
	 * 
	 * @param type
	 * @return
	 */
	SKYAdapterItem getSKYAdapterItem(int type);
}