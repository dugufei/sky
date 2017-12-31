package com.example.sky;

import android.support.v4.app.Fragment;

import com.example.sky.dialog.LoadingDialogFragment;
import com.example.sky.helper.SampleHelper;
import com.example.sky.helper.SampleManage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sky.SKYApplication;
import sky.core.ISky;
import sky.core.SKYActivity;
import sky.core.SKYModulesManage;
import sky.core.SKYPlugins;
import sky.core.exception.SKYHttpException;
import sky.core.plugins.SKYActivityInterceptor;
import sky.core.plugins.SKYBizErrorInterceptor;
import sky.core.plugins.SKYFragmentInterceptor;
import sky.core.plugins.SKYHttpErrorInterceptor;

/**
 * @author sky
 * @version 1.0 on 2017-09-25 下午8:46
 * @see MyApplication
 */
public class MyApplication extends SKYApplication implements ISky {

	@Override public void initSky() {
		SampleHelper.newSky().setSky(this).Inject(this);
	}

	@Override public boolean isLogOpen() {
		return true;
	}

	@Override public SKYModulesManage modulesManage() {
		return new SampleManage();
	}

	@Override public Retrofit.Builder httpAdapter(Retrofit.Builder builder) {
		builder.baseUrl("https://api.github.com");

		Gson gson = new GsonBuilder().setLenient().create();
		builder.addConverterFactory(GsonConverterFactory.create(gson));
		return builder;
	}

	@Override public SKYPlugins.Builder pluginInterceptor(SKYPlugins.Builder builder) {
		builder.addHttpErrorInterceptor(new SKYHttpErrorInterceptor() {

			@Override public void methodError(Class view, String method, SKYHttpException skyHttpException) {

			}
		});

		builder.addBizErrorInterceptor(new SKYBizErrorInterceptor() {

			@Override public void interceptorError(Class view, String method, Throwable throwable) {

			}

		});

		builder.setActivityInterceptor(new SKYActivityInterceptor.AdapterInterceptor() {

			@Override public void onShowLoading(SKYActivity skyActivity) {
				super.onShowLoading(skyActivity);

				skyActivity.getSupportFragmentManager().beginTransaction().add(LoadingDialogFragment.getInstance(), "loading_dialog").commitAllowingStateLoss();
			}

			@Override public void onCloseLoading(SKYActivity skyActivity) {
				super.onCloseLoading(skyActivity);
				LoadingDialogFragment loadingDialogFragment = (LoadingDialogFragment) skyActivity.getSupportFragmentManager().findFragmentByTag("loading_dialog");
				if (loadingDialogFragment != null) {
					loadingDialogFragment.dismissAllowingStateLoss();
				}
			}
		});

		builder.setFragmentInterceptor(new SKYFragmentInterceptor.AdapterInterceptor() {

			@Override public void onShowLoading(Fragment skyFragment) {
				super.onShowLoading(skyFragment);
				skyFragment.getChildFragmentManager().beginTransaction().add(LoadingDialogFragment.getInstance(), "loading_dialog").commitAllowingStateLoss();
			}

			@Override public void onCloseLoading(Fragment skyFragment) {
				super.onCloseLoading(skyFragment);
				LoadingDialogFragment loadingDialogFragment = (LoadingDialogFragment) skyFragment.getChildFragmentManager().findFragmentByTag("loading_dialog");
				if (loadingDialogFragment != null) {
					loadingDialogFragment.dismissAllowingStateLoss();
				}
			}
		});

		return builder;
	}

}
