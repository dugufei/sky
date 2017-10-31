package com.example.sky;

import jc.sky.SKYApplication;
import jc.sky.core.SKYBiz;
import jc.sky.core.exception.SKYHttpException;
import jc.sky.core.plugin.SKYBizErrorInterceptor;
import jc.sky.core.plugin.SKYHttpErrorInterceptor;
import jc.sky.modules.methodProxy.SKYMethods;

/**
 * @author sky
 * @version 1.0 on 2017-09-25 下午8:46
 * @see MyApplication
 */
public class MyApplication extends SKYApplication {

	@Override public SKYMethods getMethodInterceptor(SKYMethods.Builder builder) {
		builder.addHttpErrorInterceptor(new SKYHttpErrorInterceptor() {

			@Override public void methodError(SKYBiz skyBiz, String method, SKYHttpException skyHttpException) {
				skyBiz.showHttpError();
			}

		});

		builder.addBizErrorInterceptor(new SKYBizErrorInterceptor() {

			@Override public void interceptorError(SKYBiz skyBiz, String method, Throwable throwable) {
				skyBiz.showBizError();
			}
		});

		return builder.build();
	}
}
