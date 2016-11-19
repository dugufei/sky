package jc.sky.modules.structure;

import android.support.v4.app.FragmentManager;

import java.util.List;

import jc.sky.core.SKYIBiz;
import jc.sky.core.SKYICommonBiz;
import jc.sky.display.SKYIDisplay;
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
	<B extends SKYIBiz> B biz(Class<B> bizClazz);

	/**
	 * 业务
	 * 
	 * @param bizClazz
	 *            参数
	 * @param position
	 *            参数
	 * @param <B>
	 *            参数
	 * @return 返回值
	 */
	<B extends SKYIBiz> B biz(Class<B> bizClazz, int position);

	/**
	 * @param bizClazz
	 *            参数
	 * @param <B>
	 *            参数
	 * @return 返回值
	 */
	<B extends SKYIBiz> boolean isExist(Class<B> bizClazz);

	/**
	 * @param biz
	 *            参数
	 * @param position
	 *            下标
	 * @param <B>
	 *            返回值
	 * @return 返回值
	 */
	<B extends SKYIBiz> boolean isExist(Class<B> biz, int position);

	/**
	 * @param service
	 *            参数
	 * @param <B>
	 *            参数
	 * @return 返回值
	 */
	<B extends SKYIBiz> List<B> bizList(Class<B> service);

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