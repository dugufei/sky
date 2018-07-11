package sk;

import android.app.Application;

import sky.SKInput;
import sky.di.SKLazy;

/**
 * @author sky
 * @version 1.0 on 2018-07-09 下午11:36
 * @see SKDefaultManager
 */
public class SKDefaultManager {

	@SKInput Application			application;

	@SKInput ISK					isk;

	@SKInput SKLazy<SKCommonView>	skCommonView;

	@SKInput SKLazy<SKAppExecutors>	skAppExecutors;

	@SKInput SKLazy<SKToast>		skToast;

}
