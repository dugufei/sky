package sk;

import android.app.Application;
import android.os.Looper;

import sk.screen.SKScreenManager;

/**
 * @author sky
 * @version 1.0 on 2018-06-13 下午10:53
 * @see SKHelper
 */
public class SKHelper {

	static SKDefaultManager skDefaultManager;

	/**
	 * 初始化
	 */
	static void init() {
		SKInputs.input(skDefaultManager);
	}

	/**
	 * 获取管理
	 *
	 * @param <M>
	 *            参数
	 * @return 返回值
	 */
	protected static final <M extends SKDefaultManager> M getManage() {
		return (M) skDefaultManager;
	}

	/**
	 * 公共视图
	 *
	 * @return 返回值
	 */
	public static final SKCommonView commonView() {
		return getManage().skCommonView.get();
	}

	/**
	 * 是否打印日志
	 *
	 * @return 返回值
	 */
	public static final boolean isLogOpen() {
		return getManage().isk.isLogOpen();
	}

	/**
	 * 获取全局上下文
	 *
	 * @return 返回值
	 */
	public static final Application getInstance() {
		return getManage().application;
	}

	/**
	 * 判断是否是主线程
	 *
	 * @return true 主线程 false 子线程
	 */
	public static final boolean isMainLooperThread() {
		return Looper.getMainLooper().getThread() == Thread.currentThread();
	}

	/**
	 * MainLooper 主线程中执行
	 *
	 * @return 返回值
	 */
	public static final SKAppExecutors executors() {
		return getManage().skAppExecutors.get();
	}

	/**
	 * Toast 提示信息
	 *
	 * @return 返回值
	 */
	public static final SKToast toast() {
		return getManage().skToast.get();
	}

	/**
	 * activity管理器
	 *
	 * @return 管理器
	 */
	public static final SKScreenManager screen() {
		return getManage().skyScreenManager.get();
	}

	/**
	 * 拦截器管理器
	 *
	 * @return 管理器
	 */
	public static final SKInterceptor interceptor() {
		return getManage().skInterceptorSKLazy.get();
	}
}
