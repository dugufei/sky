package jc.sky.modules;

import android.app.Application;

import javax.inject.Inject;

import jc.sky.ISKYBind;
import jc.sky.core.SynchronousExecutor;
import jc.sky.modules.cache.CacheManager;
import jc.sky.modules.cache.ICacheManager;
import jc.sky.modules.download.SKYDownloadManager;
import jc.sky.modules.file.SKYFileCacheManage;
import jc.sky.modules.job.SKYJobService;
import jc.sky.modules.methodProxy.SKYMethods;
import jc.sky.modules.screen.SKYScreenManager;
import jc.sky.modules.structure.SKYStructureManage;
import jc.sky.modules.threadpool.SKYThreadPoolManager;
import jc.sky.modules.toast.SKYToast;
import jc.sky.view.common.SKYIViewCommon;
import retrofit2.Retrofit;

/**
 * @author sky
 * @version 版本
 */
public class SKYModulesManage {

	@Inject public Application			application;

	@Inject public CacheManager			cacheManager;			// 缓存管理器

	@Inject public SKYScreenManager		SKYScreenManager;		// Activity堆栈管理

	@Inject public SKYThreadPoolManager	SKYThreadPoolManager;	// 线程池管理

	@Inject public SKYStructureManage	SKYStructureManage;		// 结构管理器

	@Inject public SynchronousExecutor	synchronousExecutor;	// 主线程

	@Inject public SKYToast				SKYToast;				// 提示信息

	@Inject public SKYFileCacheManage	SKYFileCacheManage;		// 文件缓存管理器

	@Inject public SKYMethods.Builder	skyMethodsBuilder;		// 方法代理编辑器

	@Inject public SKYJobService		skyJobService;			// 任务管理器

	@Inject public Retrofit.Builder		retrofitBuilder;		// 网络编辑器

	@Inject public SKYDownloadManager	SKYDownloadManager;		// 下载和上传管理

	public Retrofit						mSKYRestAdapter;		// 网络适配器

	public SKYMethods					SKYMethods;				// 方法代理

	public boolean						isLog;

	public SKYIViewCommon				skyiViewCommon;

	public SKYModulesManage() {}

	public void init(ISKYBind iskyBind, SKYIViewCommon skyiViewCommon) {
		this.skyiViewCommon = skyiViewCommon;
		isLog = iskyBind.isLogOpen();
		// 初始化 HTTP
		this.mSKYRestAdapter = iskyBind.getRestAdapter(retrofitBuilder);
		// 初始化 代理方法
		this.SKYMethods = iskyBind.getMethodInterceptor(skyMethodsBuilder);
	}

	public ICacheManager getCacheManager() {
		return this.cacheManager;
	}

	public Application getApplication() {
		return application;
	}

	public boolean isLog() {
		return isLog;
	}

	public SKYIViewCommon getSkyiViewCommon() {
		return skyiViewCommon;
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

	public SKYStructureManage getSKYStructureManage() {
		return SKYStructureManage;
	}

	public SKYToast getSKYToast() {
		return SKYToast;
	}

	public SKYFileCacheManage getSKYFileCacheManage() {
		return SKYFileCacheManage;
	}

	public SKYJobService getSkyJobService() {
		return skyJobService;
	}
}