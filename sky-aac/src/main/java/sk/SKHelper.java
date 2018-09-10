package sk;

import android.app.Application;
import android.arch.lifecycle.SKHolderFragment;
import android.arch.lifecycle.SKViewModelProviders;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.SKRetrofit;
import sk.proxy.SKBizStore;
import sk.screen.SKScreenHolder;
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
	 * 获取全局上下文
	 *
	 * @return 返回值
	 */
	public static final SKBizStore bizStore() {
		return getManage().skBizStoreSKLazy.get();
	}

	/**
	 * 获取业务
	 * 
	 * @param bClass
	 * @param <B>
	 * @return
	 */
	public static final <B extends SKBiz> B biz(Class<B> bClass) {
		return bizStore().biz(bClass);
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

	/**
	 * 跳转管理器
	 *
	 * @return 管理器
	 */
	public static final SKIDisplay display() {
		return getManage().skiDisplaySKLazy.get();
	}

	/**
	 * 获取网络适配器
	 *
	 * @return 返回值
	 */
	static final SKRetrofit httpAdapter() {
		return getManage().skRetrofitSKLazy.get();
	}

	/**
	 * 获取网络
	 *
	 * @param httpClazz
	 *            参数
	 * @param <H>
	 *            参数
	 * @return 返回值
	 */
	public static final <H> H http(Class<H> httpClazz) {
		return getManage().skCacheManagerSKLazy.get().http(httpClazz);
	}

	/**
	 * 搜索view model
	 * 
	 * @param modelClazz
	 * @param <T>
	 * @return
	 */
	public static final <T extends SKViewModel> T find(Class<T> modelClazz) {
		T viewModel = null;
		ArrayList<SKScreenHolder> skScreenHolders = screen().getActivities();
		for (int i = skScreenHolders.size() - 1; i >= 0; i--) {
			FragmentActivity skFragmentActivity = skScreenHolders.get(i).getActivity();

			viewModel = SKViewModelProviders.find(skFragmentActivity, modelClazz);

			if (viewModel != null) {
				break;
			}
			// 检查 fragment
			List<Fragment> fragments = skFragmentActivity.getSupportFragmentManager().getFragments();
			for (int j = fragments.size() - 1; j >= 0; j--) {
				Fragment fragment = fragments.get(j);
				if (fragment instanceof SKHolderFragment) {
					continue;
				}
				viewModel = SKViewModelProviders.find(fragment, modelClazz);
				if (viewModel != null) {
					break;
				}
				List<Fragment> childFragments = fragment.getChildFragmentManager().getFragments();
				for (int c = childFragments.size() - 1; c >= 0; c--) {
					Fragment fragmentC = childFragments.get(j);
					viewModel = SKViewModelProviders.find(fragmentC, modelClazz);
					if (viewModel != null) {
						break;
					}
				}
			}
		}
		return viewModel;
	}

}
