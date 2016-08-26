package jc.sky.modules.structure;

import android.support.v4.app.FragmentManager;

import java.util.List;

import jc.sky.core.SKYIBiz;
import jc.sky.core.SKYICommonBiz;
import jc.sky.display.SKYIDisplay;
import jc.sky.view.SKYActivity;

/**
 * @创建人 sky
 * @创建时间 15/9/10 下午3:57
 * @类描述 结构管理器
 */
public interface SKYStructureIManage {

	void attach(SKYStructureModel view);

	void detach(SKYStructureModel view);

	<B extends SKYIBiz> B biz(Class<B> bizClazz);

	<B extends SKYIBiz> boolean isExist(Class<B> bizClazz);

	<B extends SKYIBiz> List<B> bizList(Class<B> service);

	<T> T createMainLooper(final Class<T> service, Object ui);

	<U> U createNullService(final Class<U> service);
	/**
	 * 拦截back 交给 fragment onKeyBack
	 *
	 * @param keyCode
	 * @param fragmentManager
	 * @param bSKYActivity
	 * @return
	 */
	boolean onKeyBack(int keyCode, FragmentManager fragmentManager, SKYActivity bSKYActivity);

	/**
	 * 打印堆栈内容
	 *
	 * @param fragmentManager
	 */
	void printBackStackEntry(FragmentManager fragmentManager);

}