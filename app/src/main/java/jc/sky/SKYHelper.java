package jc.sky;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import jc.sky.core.SKYIBiz;
import jc.sky.core.SKYICommonBiz;
import jc.sky.core.SynchronousExecutor;
import jc.sky.display.SKYIDisplay;
import jc.sky.modules.SKYModulesManage;
import jc.sky.modules.contact.SKYIContact;
import jc.sky.modules.download.SKYDownloadManager;
import jc.sky.modules.file.SKYFileCacheManage;
import jc.sky.modules.methodProxy.SKYMethods;
import jc.sky.modules.screen.SKYScreenManager;
import jc.sky.modules.structure.SKYStructureIManage;
import jc.sky.modules.systemuihider.SKYSystemUiHider;
import jc.sky.modules.threadpool.SKYThreadPoolManager;
import jc.sky.modules.toast.SKYToast;
import retrofit2.Retrofit;

/**
 * Created by sky on 15/1/28. helper 管理
 */
public class SKYHelper {

	protected volatile static SKYModulesManage mSKYModulesManage	= null;

	/**
	 * 单例模式-初始化SKYHelper
	 *
	 * @param SKYModulesManage
	 *            Modules
	 */
	public static void with(SKYModulesManage SKYModulesManage) {
		mSKYModulesManage = SKYModulesManage;
	}

	/**
	 * 获取管理
	 * 
	 * @param <M>
	 * @return
	 */
	protected static <M> M getManage() {
		return (M) mSKYModulesManage;
	}

	/**
	 * 获取启动管理器
	 * 
	 * @param eClass
	 * @param <D>
	 * @return
	 */
	public static <D extends SKYIDisplay> D display(Class<D> eClass) {
		return structureHelper().display(eClass);
	}

	/**
	 * 获取业务
	 * 
	 * @param service
	 * @param <B>
	 * @return
	 */
	public static final <B extends SKYIBiz> B biz(Class<B> service) {
		return structureHelper().biz(service);
	}

	/**
	 * 业务是否存在
	 * 
	 * @param service
	 * @param <B>
	 * @return true 存在 false 不存在
	 */
	public static final <B extends SKYIBiz> boolean isExist(Class<B> service) {
		return structureHelper().isExist(service);
	}

	/**
	 * 获取业务
	 * 
	 * @param service
	 * @param <B>
	 * @return
	 */
	public static final <B extends SKYIBiz> List<B> bizList(Class<B> service) {
		return structureHelper().bizList(service);
	}

	/**
	 * 公用
	 * 
	 * @param service
	 * @param <B>
	 * @return
	 */
	public static final <B extends SKYICommonBiz> B common(Class<B> service) {
		return structureHelper().common(service);
	}

	/**
	 * 获取网络
	 * 
	 * @param httpClazz
	 * @param <H>
	 * @return
	 */
	public static final <H> H http(Class<H> httpClazz) {
		return structureHelper().http(httpClazz);

	}

	/**
	 * 获取实现类
	 * 
	 * @param implClazz
	 * @param <P>
	 * @return
	 */
	public static final <P> P impl(Class<P> implClazz) {
		return structureHelper().impl(implClazz);
	}

	/**
	 * 获取方法代理
	 * 
	 * @return
	 */
	public static final SKYMethods methodsProxy() {
		return mSKYModulesManage.getSKYMethods();
	}

	/**
	 * 获取全局上下文
	 *
	 * @return
	 */
	public static SKYApplication getInstance() {
		return mSKYModulesManage.getSKYApplication();
	}

	/**
	 * 获取EventBus
	 *
	 * @return
	 */
	public static final EventBus eventBus() {
		return mSKYModulesManage.getBus();
	}

	/**
	 * 获取网络适配器
	 *
	 * @return
	 */
	public static final Retrofit httpAdapter() {
		return mSKYModulesManage.getSKYRestAdapter();
	}

	/**
	 * 结构管理器
	 * 
	 * @return 管理器
	 */
	public static final SKYStructureIManage structureHelper() {
		return mSKYModulesManage.getSKYStructureManage();
	}

	/**
	 * activity管理器
	 *
	 * @return 管理器
	 */
	public static final SKYScreenManager screenHelper() {
		return mSKYModulesManage.getSKYScreenManager();
	}

	/**
	 * SKYThreadPoolManager 线程池管理器
	 */

	public static final SKYThreadPoolManager threadPoolHelper() {
		return mSKYModulesManage.getSKYThreadPoolManager();
	}

	/**
	 * MainLooper 主线程中执行
	 *
	 * @return
	 */
	public static final SynchronousExecutor mainLooper() {
		return mSKYModulesManage.getSynchronousExecutor();
	}

	/**
	 * 下载器工具
	 *
	 * @return
	 */
	public static final SKYDownloadManager downloader() {
		return mSKYModulesManage.getSKYDownloadManager();
	}

	/**
	 * 下载器工具 - 控制线程数量
	 *
	 * @return
	 */
	public static final SKYDownloadManager downloader(int threadPoolSize) {
		return mSKYModulesManage.getSKYDownloadManager(threadPoolSize);
	}

	/**
	 * 控制状态栏和标题栏
	 * 
	 * @param activity
	 * @param anchorView
	 * @param flags
	 * @return
	 */
	public static final SKYSystemUiHider systemHider(AppCompatActivity activity, View anchorView, int flags) {
		return mSKYModulesManage.getSKYSystemUiHider(activity, anchorView, flags);
	}

	/**
	 * Toast 提示信息
	 * 
	 * @return
	 */
	public static final SKYToast toast() {
		return mSKYModulesManage.getSKYToast();
	}

	/**
	 * 通讯录管理器
	 * 
	 * @return
	 */
	public static final SKYIContact contact() {
		return mSKYModulesManage.getContactManage();
	}

	/**
	 * 提交Event
	 *
	 * @param object
	 */
	public static final void eventPost(final Object object) {
		boolean isMainLooper = isMainLooperThread();

		if (isMainLooper) {
			mainLooper().execute(new Runnable() {

				@Override public void run() {
					eventBus().post(object);
				}
			});
		} else {
			eventBus().post(object);
		}
	}

	/**
	 * 判断是否是主线程
	 * 
	 * @return true 子线程 false 主线程
	 */
	public static final boolean isMainLooperThread() {
		return Looper.getMainLooper().getThread() != Thread.currentThread();
	}

	/**
	 * 文件缓存管理器
	 * 
	 * @return
	 */
	public static final SKYFileCacheManage fileCacheManage() {
		return mSKYModulesManage.getSKYFileCacheManage();
	}

}
