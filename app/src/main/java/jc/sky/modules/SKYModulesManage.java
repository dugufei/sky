package jc.sky.modules;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import jc.sky.SKYApplication;
import jc.sky.core.SynchronousExecutor;
import jc.sky.core.exception.SKYNullPointerException;
import jc.sky.modules.contact.ContactManage;
import jc.sky.modules.download.SKYDownloadManager;
import jc.sky.modules.file.SKYFileCacheManage;
import jc.sky.modules.log.L;
import jc.sky.modules.methodProxy.SKYMethods;
import jc.sky.modules.screen.SKYScreenManager;
import jc.sky.modules.structure.SKYStructureManage;
import jc.sky.modules.systemuihider.SKYSystemUiHider;
import jc.sky.modules.threadpool.SKYThreadPoolManager;
import jc.sky.modules.toast.SKYToast;
import retrofit2.Retrofit;

/**
 * @创建人 sky
 * @创建时间 15/8/5 下午3:17
 * @类描述 Modules管理
 */
public class SKYModulesManage {

	private final SKYApplication		mSKYApplication;		// 全局上下文

	private final SKYScreenManager		SKYScreenManager;		// Activity堆栈管理

	private final SKYThreadPoolManager	SKYThreadPoolManager;	// 线程池管理

	private final SKYStructureManage	SKYStructureManage;		// 结构管理器

	private final SynchronousExecutor	synchronousExecutor;	// 主线程

	private final SKYToast				SKYToast;				// 提示信息

	private final ContactManage			contactManage;			// 通讯录

	private SKYSystemUiHider			SKYSystemUiHider;		// 标题栏和状态栏控制

	private L.DebugTree					debugTree;				// 打印信息

	private SKYMethods					SKYMethods;				// 方法代理

	private SKYDownloadManager			SKYDownloadManager;		// 下载和上传管理

	private Retrofit					mSKYRestAdapter;		// 网络适配器

	private SKYFileCacheManage			SKYFileCacheManage;		// 文件缓存管理器

	public SKYModulesManage(SKYApplication SKYApplication) {
		this.mSKYApplication = SKYApplication;
		this.SKYScreenManager = new SKYScreenManager();
		this.SKYStructureManage = new SKYStructureManage();
		this.SKYThreadPoolManager = new SKYThreadPoolManager();
		this.synchronousExecutor = new SynchronousExecutor();
		this.SKYDownloadManager = new SKYDownloadManager();
		this.SKYToast = new SKYToast();
		this.contactManage = new ContactManage(mSKYApplication);
		this.SKYFileCacheManage = new SKYFileCacheManage();
	}

	public SKYApplication getSKYApplication() {
		return this.mSKYApplication;
	}

	public void initSKYRestAdapter(Retrofit SKYRestAdapter) {
		this.mSKYRestAdapter = SKYRestAdapter;
	}

	public void initLog(boolean logOpen) {
		if (logOpen) {
			if (debugTree == null) {
				debugTree = new L.DebugTree();
			}
			L.plant(debugTree);
		}
	}

	public void initMehtodProxy(SKYMethods methodInterceptor) {
		SKYMethods = methodInterceptor;
	}

	public SKYMethods getSKYMethods() {
		return SKYMethods;
	}

	public Retrofit getSKYRestAdapter() {
		return this.mSKYRestAdapter;
	}

	public SKYScreenManager getSKYScreenManager() {
		return SKYScreenManager;
	}

	public SKYThreadPoolManager getSKYThreadPoolManager() {
		return SKYThreadPoolManager;
	}

	public SynchronousExecutor getSynchronousExecutor() {
		return synchronousExecutor;
	}

	public SKYDownloadManager getSKYDownloadManager() {
		return SKYDownloadManager;
	}

	public SKYDownloadManager getSKYDownloadManager(int threadPoolSize) {
		if (SKYDownloadManager == null) {
			synchronized (this) {
				if (SKYDownloadManager == null) {
					SKYDownloadManager = new SKYDownloadManager(threadPoolSize);
				}
			}
		}

		return SKYDownloadManager;
	}

	public SKYStructureManage getSKYStructureManage() {

		if(SKYStructureManage == null){
			throw new SKYNullPointerException("Application没有继承SKYApplication");
		}
		return SKYStructureManage;
	}

	public SKYToast getSKYToast() {
		return SKYToast;
	}

	public ContactManage getContactManage() {
		return contactManage;
	}

	public SKYSystemUiHider getSKYSystemUiHider(AppCompatActivity activity, View anchorView, int flags) {
		if (SKYSystemUiHider == null) {
			synchronized (this) {
				if (SKYSystemUiHider == null) {
					SKYSystemUiHider = SKYSystemUiHider.getInstance(activity, anchorView, flags);
				}
			}
		}
		return SKYSystemUiHider;
	}

	public SKYFileCacheManage getSKYFileCacheManage() {
		return SKYFileCacheManage;
	}
}