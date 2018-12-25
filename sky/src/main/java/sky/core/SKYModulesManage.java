package sky.core;

import android.app.Application;
import android.util.SparseArray;

import java.util.ArrayList;
import javax.inject.Inject;

import dagger.Lazy;
import retrofit2.Retrofit;
import sky.core.methodModule.SKYIMethodRun;
import sky.core.methodModule.SKYIModule;
import sky.core.modules.download.SKYDownloadManager;
import sky.core.modules.file.SKYFileCacheManage;
import sky.core.modules.job.SKYJobService;
import sky.core.screen.SKYScreenManager;

/**
 * @author sky
 * @version 版本
 */
public class SKYModulesManage {

	@Inject public Application									application;

	@Inject public ISky											sky;					// 架构

	@Inject public Lazy<SKYCacheManager>						skyCacheManager;		// 缓存管理器

	@Inject public Lazy<SKYScreenManager>						skyScreenManager;		// Activity堆栈管理

	@Inject public Lazy<SKYThreadPoolManager>					skyThreadPoolManager;	// 线程池管理

	@Inject public Lazy<SKYStructureManage>						skyStructureManage;		// 结构管理器

	@Inject public Lazy<SynchronousExecutor>					synchronousExecutor;	// 主线程

	@Inject public Lazy<SKYToast>								skyToast;				// 提示信息

	@Inject public Lazy<Retrofit>								retrofit;				// 网络适配器

	@Inject public Lazy<SKYPlugins>								skyPlugins;				// 插件

	@Inject public Lazy<SKYIViewCommon>							skyiViewCommon;			// 全局视图

	@Inject public Lazy<SparseArray<SKYIMethodRun>>				provideMethodRun;		// 组件化

	@Inject public Lazy<ArrayList<Class<? extends SKYIModule>>>	provideModule;			// 架构组件化

	@Inject public Lazy<SKYFileCacheManage>						skyFileCacheManage;		// 文件缓存管理器

	@Inject public Lazy<SKYJobService>							skyJobService;			// 任务管理器

	@Inject public Lazy<SKYDownloadManager>						skyDownloadManager;		// 下载和上传管理
}