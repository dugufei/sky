package com.example.sky;

import android.support.v4.app.Fragment;

import com.example.sky.dialog.LoadingDialogFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jc.sky.SKYApplication;
import jc.sky.core.SKYBiz;
import jc.sky.core.exception.SKYHttpException;
import jc.sky.core.plugin.SKYActivityInterceptor;
import jc.sky.core.plugin.SKYBizErrorInterceptor;
import jc.sky.core.plugin.SKYFragmentInterceptor;
import jc.sky.core.plugin.SKYHttpErrorInterceptor;
import jc.sky.modules.methodProxy.SKYMethods;
import jc.sky.view.SKYActivity;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author sky
 * @version 1.0 on 2017-09-25 下午8:46
 * @see MyApplication
 */
public class MyApplication extends SKYApplication {

	@Override public SKYMethods getMethodInterceptor(SKYMethods.Builder builder) {
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

		return builder.build();
	}

	@Override public Retrofit getRestAdapter(Retrofit.Builder builder) {
		builder.baseUrl("https://api.github.com");

		Gson gson = new GsonBuilder().setLenient().create();
		builder.addConverterFactory(GsonConverterFactory.create(gson));
		return builder.build();
	}
}
