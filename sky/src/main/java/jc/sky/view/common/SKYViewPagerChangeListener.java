package jc.sky.view.common;

import android.view.View;

/**
 * @author sky
 * @version 版本
 */
public interface SKYViewPagerChangeListener {

	/**
	 * ViewPager 滑动事件 - 滑动过程
	 *
	 * @param position
	 *            参数
	 * @param left
	 *            左视图
	 * @param right
	 *            右视图
	 * @param v
	 *            数值
	 * @param i2
	 *            偏移量
	 */
	void onExtraPageScrolled(int position, View left, View right, float v, int i2);

	/**
	 * ViewPager 滑动事件 - 滑动完成
	 *
	 * @param current
	 *            当前
	 * @param old
	 *            过去
	 * @param currentPosition
	 *            当前坐标
	 * @param oldPosition
	 *            过去坐标
	 */
	void onExtraPageSelected(View current, View old, int currentPosition, int oldPosition);

	/**
	 * ViewPager 滑动事件 - 滑动改变
	 *
	 * @param i
	 *            参数
	 */
	void onExtraPageScrollStateChanged(int i);
}