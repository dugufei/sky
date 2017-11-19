package jc.sky.modules;

import android.app.Application;
import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.inject.Inject;

import jc.sky.core.ISKYBind;
import jc.sky.core.SKYHelper;
import jc.sky.core.SKYStructureManage;
import jc.sky.core.SKYWareHouseManage;
import jc.sky.common.SKYPackUtils;
import jc.sky.common.SKYAppUtil;
import jc.sky.core.SKYIModule;
import jc.sky.core.SynchronousExecutor;
import jc.sky.core.exception.SKYNullPointerException;
import jc.sky.modules.cache.CacheManager;
import jc.sky.modules.cache.ICacheManager;
import jc.sky.modules.download.SKYDownloadManager;
import jc.sky.modules.file.SKYFileCacheManage;
import jc.sky.modules.job.SKYJobService;
import jc.sky.modules.log.L;
import jc.sky.modules.methodProxy.SKYMethods;
import jc.sky.modules.screen.SKYScreenManager;
import jc.sky.modules.threadpool.SKYThreadPoolManager;
import jc.sky.modules.toast.SKYToast;
import jc.sky.core.SKYIViewCommon;
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

	@Inject public SKYStructureManage skyStructureManage;		// 结构管理器

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

	public synchronized void initModule(Context context) {
		try {
			long e = System.currentTimeMillis();
			Object skyMap;
			if (!SKYHelper.isLogOpen() && !SKYAppUtil.isNewVersion(context)) {
				L.i("Sky::", "加载缓存.");
				skyMap = new HashSet(context.getSharedPreferences("SP_SKY_CACHE", 0).getStringSet("SKY_MAP", new HashSet()));
			} else {
				L.i("Sky::", "获取运行时，指定包目录下的所有信息并缓存到skyMap");
				skyMap = SKYPackUtils.getFileNameByPackageName(context, "com.sky.android.module.biz");
				if (!((Set) skyMap).isEmpty()) {
					context.getSharedPreferences("SP_SKY_CACHE", 0).edit().putStringSet("SKY_MAP", (Set) skyMap).apply();
				}
			}

			L.i("Sky::", "Find router map finished, map size = " + ((Set) skyMap).size() + ", cost " + (System.currentTimeMillis() - e) + " ms.");
			e = System.currentTimeMillis();
			Iterator var5 = ((Set) skyMap).iterator();

			while (var5.hasNext()) {
				String className = (String) var5.next();
				String bizName = StringUtils.removeStart(className,"com.sky.android.module.biz.Sky$$Biz$$");
				SKYWareHouseManage.modules.put(bizName, (Class<? extends SKYIModule>) Class.forName(className));
			}

			L.i("Sky::", "加载完毕, cost " + (System.currentTimeMillis() - e) + " ms.");
		} catch (Exception var7) {
			throw new SKYNullPointerException("Sky::Sky init logistics center exception! [" + var7.getMessage() + "]");
		}
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
		return skyStructureManage;
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