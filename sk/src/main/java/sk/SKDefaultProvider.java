package sk;

import android.app.Application;
import android.util.SparseArray;

import com.squareup.leakcanary.LeakCanary;

import retrofit2.Retrofit;
import retrofit2.SKCallAdapterFactory;
import retrofit2.SKRetrofit;
import sk.livedata.SKPaged;
import sk.methodModule.SKIMethodRun;
import sk.methodModule.SKIModule;
import sk.methodModule.SKModuleManage;
import sk.proxy.SKBizStore;
import sk.proxy.SKProxy;
import sk.screen.SKScreenManager;
import sky.SKSingleton;
import sky.SKProvider;

/**
 * @author sk
 * @version 1.0 on 2018-07-09 下午11:35
 * @see SKDefaultProvider
 */
public class SKDefaultProvider {

	final Application	application;

	final SKCommonView	skCommonView;

	final ISK			isk;

	public SKDefaultProvider(Application application, SKCommonView skCommonView, ISK isk) {
		this.application = application;
		this.skCommonView = skCommonView;
		this.isk = isk;
		if (this.isk.isLogOpen()) {
			L.plant(new L.DebugTree());
			// 内存泄露检测
			LeakCanary.install(application);
		}
	}

	@SKProvider @SKSingleton public Application provideApplication() {
		return application;
	}

	@SKProvider @SKSingleton public SKCommonView provideSKCommonView() {
		return skCommonView == null ? SKCommonView.NULL : skCommonView;
	}

	@SKProvider @SKSingleton public ISK provideISK() {
		return isk == null ? ISK.NONE : isk;
	}

	/**
	 * 线程
	 * 
	 * @return
	 */
	@SKProvider @SKSingleton public SKAppExecutors provideSKAppExecutors() {
		return new SKAppExecutors();
	}

	/**
	 * 管理器
	 * 
	 * @return
	 */
	@SKProvider @SKSingleton public SKScreenManager provideSKScreenManager() {
		return new SKScreenManager();
	}

	/**
	 * 吐丝
	 * 
	 * @return
	 */
	@SKProvider @SKSingleton public SKToast provideSKToast() {
		return new SKToast();
	}

	/**
	 * 插件编辑器
	 *
	 * @return 返回值
	 */
	@SKSingleton @SKProvider public SKInterceptor.Builder provideSKInterceptorBuilder() {
		return new SKInterceptor.Builder();
	}

	/**
	 * 插件
	 *
	 * @param builder
	 * @return 返回值
	 */
	@SKSingleton @SKProvider public SKInterceptor provideSKInterceptor(SKInterceptor.Builder builder) {
		return this.isk.pluginInterceptor(builder).build();
	}

	/**
	 * sk view model
	 *
	 * @return
	 */
	@SKSingleton @SKProvider public SKViewModelFactory provideSKViewModelFactory() {
		return new SKViewModelFactory();
	}

	/**
	 * sk biz store
	 *
	 * @return
	 */
	@SKSingleton @SKProvider public SKBizStore provideSKBizStore() {
		return new SKBizStore();
	}

	/**
	 * sk display
	 * 
	 * @return
	 */
	@SKSingleton @SKProvider public SKIDisplay provideSKDisplay(SKBizStore skBizStore) {
		SKDisplay skDisplay = new SKDisplay();
		SKProxy skProxy = skBizStore.createDisplay(SKIDisplay.class, skDisplay);
		return (SKIDisplay) skProxy.proxy;
	}

	/**
	 * 网络
	 * 
	 * @return 返回值
	 */
	@SKSingleton @SKProvider public SKRetrofit provideRetrofit() {
		Retrofit.Builder builder = new Retrofit.Builder();
		builder.addCallAdapterFactory(new SKCallAdapterFactory());
		SKRetrofit skRetrofit = new SKRetrofit(this.isk.httpAdapter(builder).build());
		return skRetrofit;
	}

	/**
	 * 缓存
	 *
	 * @return 返回值
	 */
	@SKSingleton @SKProvider public SKCacheManager provideCacheManager() {
		return new SKCacheManager();
	}

	@SKSingleton @SKProvider public SKPaged providePaged() {
		return new SKPaged();
	}

	/**
	 * sk架构 - 组件化biz
	 *
	 * @return 返回值
	 */
	@SKSingleton @SKProvider public SparseArray<SKIMethodRun> provideMethodRun(SKModuleManage skModuleManage) {
		SparseArray<SKIMethodRun> methodRunSparseArray = new SparseArray<>();
		int count = skModuleManage.getList().size();
		for (int i = 0; i < count; i++) {
			try {
				SKIModule skiModule = skModuleManage.getList().get(i).getConstructor(new Class[0]).newInstance(new Object[0]);
				skiModule.loadInto(methodRunSparseArray);
			} catch (Exception var8) {
				L.e("sk::加载组件时 出现了致命的异常. [" + var8.getMessage() + "]");
			}
		}
		// 加载数据
		return methodRunSparseArray;
	}

	/**
	 * sk架构 - 组件化biz
	 *
	 * @return 返回值
	 */
	@SKSingleton @SKProvider public SKModuleManage provideModule() {
		return new SKModuleManage();
	}
}
