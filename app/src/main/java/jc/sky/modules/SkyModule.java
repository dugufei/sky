package jc.sky.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jc.sky.core.SynchronousExecutor;
import jc.sky.modules.cache.CacheManager;
import jc.sky.modules.contact.ContactManage;
import jc.sky.modules.download.SKYDownloadManager;
import jc.sky.modules.file.SKYFileCacheManage;
import jc.sky.modules.methodProxy.SKYMethods;
import jc.sky.modules.screen.SKYScreenManager;
import jc.sky.modules.structure.SKYStructureManage;
import jc.sky.modules.threadpool.SKYThreadPoolManager;
import jc.sky.modules.toast.SKYToast;
import retrofit2.Retrofit;

/**
 * @创建人 sky
 * @创建时间 16/9/23 下午5:11
 * @类描述
 */
@Module
public class SKYModule {

	Application application;

	public SKYModule(Application application) {
		this.application = application;
	}

	@Provides @Singleton public Application provideApplication() {
		return application;
	}

	/**
	 * 网络编辑器
	 * 
	 * @return
	 */
	@Provides @Singleton public Retrofit.Builder provideRetrofit() {
		return new Retrofit.Builder();
	}

	/**
	 * 方法编辑器
	 * 
	 * @return
	 */
	@Provides @Singleton public SKYMethods.Builder provideSKYMethods() {
		return new SKYMethods.Builder();
	}

	/**
	 * 缓存管理器
	 * 
	 * @return
	 */
	@Provides @Singleton public CacheManager provideCacheManager() {
		return new CacheManager();
	}

	/**
	 * Activity堆栈管理
	 * 
	 * @return
	 */
	@Provides @Singleton public SKYScreenManager provideSKYScreenManager() {
		return new SKYScreenManager();
	}

	/**
	 * 线程池管理
	 * 
	 * @return
	 */
	@Provides @Singleton public SKYThreadPoolManager provideSKYThreadPoolManager() {
		return new SKYThreadPoolManager();
	}

	/**
	 * 结构管理器
	 * 
	 * @return
	 */
	@Provides @Singleton public SKYStructureManage provideSKYStructureManage() {
		return new SKYStructureManage();
	}

	/**
	 * 主线程
	 * 
	 * @return
	 */
	@Provides @Singleton public SynchronousExecutor provideSynchronousExecutor() {
		return new SynchronousExecutor();
	}

	/**
	 * 提示信息
	 * 
	 * @return
	 */
	@Provides @Singleton public SKYToast provideSKYToast() {
		return new SKYToast();
	}

	/**
	 * 通讯录管理器
	 * 
	 * @return
	 */
	@Provides @Singleton public ContactManage provideContactManage(Application application) {
		return new ContactManage(application);
	}

	/**
	 * 下载管理
	 * 
	 * @return
	 */
	@Provides @Singleton public SKYDownloadManager provideSKYDownloadManager() {
		return new SKYDownloadManager();
	}

	/**
	 * 文件缓存管理器
	 * 
	 * @return
	 */
	@Provides @Singleton public SKYFileCacheManage provideSKYFileCacheManage() {
		return new SKYFileCacheManage();
	}

}
