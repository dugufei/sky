package jc.sky.view.adapter;

import android.view.View;

import jc.sky.view.model.SKYModelPager;

/**
 * @author sky
 * @version 版本
 */
public interface SKYTabsCustomListener {

	/**
	 * 初始化 TabHost - item 样式
	 *
	 * @return　 布局ID
	 */
	int getViewPagerItemLayout();

	/**
	 * 初始化 TabHost - item 值
	 *
	 * @param view
	 *            TabItem
	 * @param modelPager
	 *            Item对象
	 */
	void initTab(View view, SKYModelPager modelPager);
}