package jc.sky.modules;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import jc.sky.SKYApplication;
import jc.sky.core.SynchronousExecutor;
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

	private final SKYApplication		mJ2WApplication;		// 全局上下文

	private final EventBus				bus;					// 事件总线

	private final SKYScreenManager		j2WScreenManager;		// Activity堆栈管理

	private final SKYThreadPoolManager	j2WThreadPoolManager;	// 线程池管理

	private final SKYStructureManage	j2WStructureManage;		// 结构管理器

	private final SynchronousExecutor	synchronousExecutor;	// 主线程

	private final SKYToast				j2WToast;				// 提示信息

	private final ContactManage			contactManage;			// 通讯录

	private SKYSystemUiHider			j2WSystemUiHider;		// 标题栏和状态栏控制

	private L.DebugTree					debugTree;				// 打印信息

	private SKYMethods					j2WMethods;				// 方法代理

	private SKYDownloadManager			j2WDownloadManager;		// 下载和上传管理

	private Retrofit					mJ2WRestAdapter;		// 网络适配器

	private SKYFileCacheManage			j2WFileCacheManage;		// 文件缓存管理器

	public SKYModulesManage(SKYApplication j2WApplication) {
		this.mJ2WApplication = j2WApplication;
		this.bus = EventBus.getDefault();
		this.j2WScreenManager = new SKYScreenManager();
		this.j2WStructureManage = new SKYStructureManage();
		this.j2WThreadPoolManager = new SKYThreadPoolManager();
		this.synchronousExecutor = new SynchronousExecutor();
		this.j2WDownloadManager = new SKYDownloadManager();
		this.j2WToast = new SKYToast();
		this.contactManage = new ContactManage(mJ2WApplication);
		this.j2WFileCacheManage = new SKYFileCacheManage();
	}

	public SKYApplication getJ2WApplication() {
		return this.mJ2WApplication;
	}

	public void initJ2WRestAdapter(Retrofit j2WRestAdapter) {
		this.mJ2WRestAdapter = j2WRestAdapter;
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
		j2WMethods = methodInterceptor;
	}

	public SKYMethods getJ2WMethods() {
		return j2WMethods;
	}

	public Retrofit getJ2WRestAdapter() {
		return this.mJ2WRestAdapter;
	}

	public EventBus getBus() {
		return bus;
	}

	public SKYScreenManager getJ2WScreenManager() {
		return j2WScreenManager;
	}

	public SKYThreadPoolManager getJ2WThreadPoolManager() {
		return j2WThreadPoolManager;
	}

	public SynchronousExecutor getSynchronousExecutor() {
		return synchronousExecutor;
	}

	public SKYDownloadManager getJ2WDownloadManager() {
		return j2WDownloadManager;
	}

	public SKYDownloadManager getJ2WDownloadManager(int threadPoolSize) {
		if (j2WDownloadManager == null) {
			synchronized (this) {
				if (j2WDownloadManager == null) {
					j2WDownloadManager = new SKYDownloadManager(threadPoolSize);
				}
			}
		}

		return j2WDownloadManager;
	}

	public SKYStructureManage getJ2WStructureManage() {
		return j2WStructureManage;
	}

	public SKYToast getJ2WToast() {
		return j2WToast;
	}

	public ContactManage getContactManage() {
		return contactManage;
	}

	public SKYSystemUiHider getJ2WSystemUiHider(AppCompatActivity activity, View anchorView, int flags) {
		if (j2WSystemUiHider == null) {
			synchronized (this) {
				if (j2WSystemUiHider == null) {
					j2WSystemUiHider = SKYSystemUiHider.getInstance(activity, anchorView, flags);
				}
			}
		}
		return j2WSystemUiHider;
	}

	public SKYFileCacheManage getJ2WFileCacheManage() {
		return j2WFileCacheManage;
	}
}