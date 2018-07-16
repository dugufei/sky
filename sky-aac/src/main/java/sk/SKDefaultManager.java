package sk;

import android.app.Application;

import sk.screen.SKScreenManager;
import sky.SKInput;

/**
 * @author sky
 * @version 1.0 on 2018-07-09 下午11:36
 * @see SKDefaultManager
 */
public class SKDefaultManager {

	@SKInput Application				application;

	@SKInput ISK						isk;

	@SKInput SKLazy<SKCommonView>		skCommonView;

	@SKInput SKLazy<SKAppExecutors>		skAppExecutors;

	@SKInput SKLazy<SKToast>			skToast;

	@SKInput SKLazy<SKScreenManager>	skyScreenManager;

	@SKInput SKLazy<SKInterceptor>		skInterceptorSKLazy;
}
