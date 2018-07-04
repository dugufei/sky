package sk;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 下午4:24
 * @see SKApp
 */
public class SKApp extends Application implements HasActivityInjector {

	@Override public void onCreate() {
		super.onCreate();
	}

	@Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

	@Override public AndroidInjector<Activity> activityInjector() {
		return dispatchingAndroidInjector;
	}

}
