package sk;

import android.app.Application;

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
	}

	@SKProvider @SKSingleton public Application provideApplication() {
		return application;
	}

	@SKProvider @SKSingleton public SKCommonView provideSKCommonView() {
		return skCommonView;
	}

	@SKProvider @SKSingleton public ISK provideISK() {
		return isk == null ? ISK.NONE : isk;
	}

	@SKProvider @SKSingleton public SKAppExecutors provideSKAppExecutors() {
		return new SKAppExecutors();
	}

	@SKProvider @SKSingleton public SKToast provideSKToast() {
		return new SKToast();
	}
}
