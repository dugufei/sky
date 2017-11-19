package com.example.sky;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jc.sky.SKYApplication;
import jc.sky.core.SKYBiz;
import jc.sky.core.exception.SKYHttpException;
import jc.sky.core.plugin.SKYBizErrorInterceptor;
import jc.sky.core.plugin.SKYHttpErrorInterceptor;
import jc.sky.modules.methodProxy.SKYMethods;
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

		return builder.build();
	}

	@Override public Retrofit getRestAdapter(Retrofit.Builder builder) {
		builder.baseUrl("https://api.github.com");

		Gson gson = new GsonBuilder().setLenient().create();
		builder.addConverterFactory(GsonConverterFactory.create(gson));
		return builder.build();
	}
}
