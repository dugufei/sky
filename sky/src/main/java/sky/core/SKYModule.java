package sky.core;

import android.app.Application;
import android.support.annotation.NonNull;

import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sky.core.methodModule.SKYIModule;
import sky.core.methodModule.SkyMethodModel;
import sky.core.modules.download.SKYDownloadManager;
import sky.core.modules.file.SKYFileCacheManage;
import sky.core.modules.job.SKYJobService;
import sky.core.screen.SKYScreenManager;
import retrofit2.Retrofit;

/**
 * @author sky
 * @version 版本
 */
@Module
class SKYModule {

	Application		application;

	SKYIViewCommon	skyiViewCommon;

	ISky			sky;

	/**
	 * @param application
	 *            参数
	 */
	public SKYModule(@NonNull Application application) {
		this.application = application;
	}

	/**
	 * 设置全局布局
	 * 
	 * @param skyiViewCommon
	 *            参数
	 */
	void setSkyiViewCommon(@NonNull SKYIViewCommon skyiViewCommon) {
		this.skyiViewCommon = skyiViewCommon;
	}

	/**
	 * 设置sky接口
	 * 
	 * @param sky
	 */
	void setSky(@NonNull ISky sky) {
		this.sky = sky;
	}

	/**
	 * 全局上下文
	 * 
	 * @return 返回值
	 */

	@Provides @Singleton public Application provideApplication() {
		return application;
	}

	/**
	 * sky架构
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public ISky provideSky() {
		return this.sky;
	}

	/**
	 * sky架构 - 公共视图
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public SKYIViewCommon provideViewCommon() {
		return this.skyiViewCommon;
	}

	/**
	 * sky架构 - 管理器
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public SKYModulesManage provideSKYModulesManage() {
		return this.sky.modulesManage();
	}

	/**
	 * 缓存管理器
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public SKYCacheManager provideCacheManager() {
		return new SKYCacheManager();
	}

	/**
	 * 网络编辑器
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public Retrofit.Builder provideRetrofitBuilder() {
		return new Retrofit.Builder();
	}

	/**
	 * 网络
	 * 
	 * @param builder
	 * @return 返回值
	 */
	@Provides @Singleton public Retrofit provideRetrofit(Retrofit.Builder builder) {
		return this.sky.httpAdapter(builder).build();
	}

	/**
	 * 插件编辑器
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public SKYPlugins.Builder providePluginBuilder() {
		return new SKYPlugins.Builder();
	}

	/**
	 * 插件
	 * 
	 * @param builder
	 * @return 返回值
	 */
	@Provides @Singleton public SKYPlugins providePlugins(SKYPlugins.Builder builder) {
		return this.sky.pluginInterceptor(builder).build();
	}

	/**
	 * Activity堆栈管理
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public SKYScreenManager provideSKYScreenManager() {
		return new SKYScreenManager();
	}

	/**
	 * 线程池管理
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public SKYThreadPoolManager provideSKYThreadPoolManager() {
		return new SKYThreadPoolManager();
	}

	/**
	 * 结构管理器
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public SKYStructureManage provideSKYStructureManage() {
		return new SKYStructureManage();
	}

	/**
	 * 主线程
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public SynchronousExecutor provideSynchronousExecutor() {
		return new SynchronousExecutor();
	}

	/**
	 * 提示信息
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public SKYToast provideSKYToast() {
		return new SKYToast();
	}

	/**
	 * sky架构 - 组件化biz
	 * 
	 * @return 返回值
	 */
	@Provides @Singleton public ConcurrentHashMap<String, SkyMethodModel> provideModuleBiz() {
		return new ConcurrentHashMap<>();
	}

	/**
	 * sky架构 - 组件化biz
	 * 
	 * @return 返回值
	 */
	@Provides @Singleton public ConcurrentHashMap<String, Class<? extends SKYIModule>> provideModule() {
		return new ConcurrentHashMap<>();
	}

	/**
	 * sky架构 - 组件化biz
	 * 
	 * @return 返回值
	 */
	@Provides @Singleton public ConcurrentHashMap<Integer, Boolean> provideBizTypes() {
		return new ConcurrentHashMap<>();
	}

	/**
	 * sky架构 - 组件化biz
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public SKYFileCacheManage provideFileCachemanage() {
		SKYFileCacheManage skyFileCacheManage = new SKYFileCacheManage();
		skyFileCacheManage.configurePhoneCache(application);
		return skyFileCacheManage;
	}

	/**
	 * sky架构 - 组件化biz
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public SKYJobService provideJobService() {
		return new SKYJobService();
	}

	/**
	 * sky架构 - 组件化biz
	 *
	 * @return 返回值
	 */
	@Provides @Singleton public SKYDownloadManager provideDownLoadManage() {
		return new SKYDownloadManager();
	}
}
