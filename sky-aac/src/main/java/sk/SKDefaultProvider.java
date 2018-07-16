package sk;

import android.app.Application;

import sk.screen.SKScreenManager;
import sky.SKProvider;
import sky.SKSingleton;

/**
 * @author sky
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
		if(this.isk.isLogOpen()){
			SKLog.plant(new SKLog.DebugTree());
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

	@SKProvider @SKSingleton public SKAppExecutors provideSKAppExecutors() {
		return new SKAppExecutors();
	}

	@SKProvider @SKSingleton public SKScreenManager provideSKScreenManager() {
		return new SKScreenManager();
	}

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
}
