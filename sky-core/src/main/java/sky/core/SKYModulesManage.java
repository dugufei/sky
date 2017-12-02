package sky.core;

import android.app.Application;

import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import retrofit2.Retrofit;
import sky.core.methodModule.SKYIModule;
import sky.core.methodModule.SkyMethodModel;
import sky.core.screen.SKYScreenManager;

/**
 * @author sky
 * @version 版本
 */
public class SKYModulesManage{

	@Inject public Application												application;

	@Inject public SKYCacheManager											skyCacheManager;		// 缓存管理器

	@Inject public SKYScreenManager											skyScreenManager;		// Activity堆栈管理

	@Inject public SKYThreadPoolManager										skyThreadPoolManager;	// 线程池管理

	@Inject public SKYStructureManage										skyStructureManage;		// 结构管理器

	@Inject public SynchronousExecutor										synchronousExecutor;	// 主线程

	@Inject public SKYToast													skyToast;				// 提示信息

	@Inject public Retrofit													retrofit;				// 网络适配器

	@Inject public SKYPlugins												skyPlugins;				// 插件

	@Inject public SKYIViewCommon											skyiViewCommon;			// 全局视图

	@Inject public ISky														sky;					// 架构

	@Inject public ConcurrentHashMap<String, SkyMethodModel>					provideModuleBiz;		// 架构 组件化

	@Inject public ConcurrentHashMap<String, Class<? extends SKYIModule>>	provideModule;			// 架构组件化

	@Inject public ConcurrentHashMap<Integer, Boolean>						provideBizTypes;		// 架构组件化
}