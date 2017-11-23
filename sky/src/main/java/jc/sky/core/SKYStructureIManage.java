package jc.sky.core;

import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import jc.sky.view.SKYActivity;

/**
 * @author sky
 * @version 版本 版本
 */
public interface SKYStructureIManage {

	/**
	 * @param view
	 *            参数
	 */
	void attach(SKYStructureModel view);

	/**
	 * @param view
	 *            参数
	 */
	void detach(SKYStructureModel view);

	/**
	 * @param bizClazz
	 *            参数
	 * @param <B>
	 *            参数
	 * @return 返回值
	 */
	<B extends SKYBiz> B biz(Class<B> bizClazz);

	/**
	 * @param bizClazz
	 *            参数
	 * @param <B>
	 *            参数
	 * @return 返回值
	 */
	<B extends SKYBiz> boolean isExist(Class<B> bizClazz);

	/**
	 * @param service
	 *            参数
	 * @param <B>
	 *            参数
	 * @return 返回值
	 */
	<B extends SKYBiz> ArrayList<B> bizList(Class<B> service);

	/**
	 * @param service
	 *            参数
	 * @param ui
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	<T> T createMainLooper(final Class<T> service, Object ui);

	<T> T createMainLooperNotIntf(final Class<T> service, Object ui);

	/**
	 * @param service
	 *            参数
	 * @param <U>
	 *            参数
	 * @return 返回值
	 */
	<U> U createNullService(final Class<U> service);

	/**
	 * 拦截back 交给 fragment onKeyBack
	 *
	 * @param keyCode
	 *            参数
	 * @param fragmentManager
	 *            参数
	 * @param bSKYActivity
	 *            参数
	 * @return 返回值
	 */
	boolean onKeyBack(int keyCode, FragmentManager fragmentManager, SKYActivity bSKYActivity);

	/**
	 * 打印堆栈内容
	 *
	 * @param fragmentManager
	 *            参数
	 */
	void printBackStackEntry(FragmentManager fragmentManager);

}