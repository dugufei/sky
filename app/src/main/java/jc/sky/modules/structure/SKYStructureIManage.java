package jc.sky.modules.structure;

import android.support.v4.app.FragmentManager;

import java.util.List;

import jc.sky.core.J2WIBiz;
import jc.sky.core.J2WICommonBiz;
import jc.sky.display.J2WIDisplay;
import jc.sky.view.SKYActivity;

/**
 * @创建人 sky
 * @创建时间 15/9/10 下午3:57
 * @类描述 结构管理器
 */
public interface SKYStructureIManage {

	void attach(SKYStructureModel view);

	void detach(SKYStructureModel view);

	<D extends J2WIDisplay> D display(Class<D> displayClazz);

	<B extends J2WIBiz> B biz(Class<B> bizClazz);

	<B extends J2WIBiz> boolean isExist(Class<B> bizClazz);

	<B extends J2WICommonBiz> B common(Class<B> service);

	<B extends J2WIBiz> List<B> bizList(Class<B> service);

	<H> H http(Class<H> httpClazz);

	<P> P impl(Class<P> implClazz);

	<T> T createMainLooper(final Class<T> service, Object ui);

	<U> U createNullService(final Class<U> service);
	/**
	 * 拦截back 交给 fragment onKeyBack
	 *
	 * @param keyCode
	 * @param fragmentManager
	 * @param bj2WActivity
	 * @return
	 */
	boolean onKeyBack(int keyCode, FragmentManager fragmentManager, SKYActivity bj2WActivity);

	/**
	 * 打印堆栈内容
	 *
	 * @param fragmentManager
	 */
	void printBackStackEntry(FragmentManager fragmentManager);

}