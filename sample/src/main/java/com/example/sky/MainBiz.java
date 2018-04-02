package com.example.sky;

import android.os.Bundle;

import sky.Background;
import sky.BackgroundType;
import sky.Interceptor;
import sky.core.L;
import sky.core.SKYBiz;
import sky.core.exception.SKYHttpException;

/**
 * @author sky
 * @date Created on 2017-11-19 下午5:58
 * @version 1.0
 * @Description MainBiz - 描述
 */
public class MainBiz extends SKYBiz<MainActivity> {

	@Override protected void initBiz(Bundle bundle) {
		super.initBiz(bundle);
	}

	@Background(BackgroundType.HTTP) public void load() {
		loading();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		closeLoading();
	}

	public void tip() {}

	@Interceptor(2) @Background(BackgroundType.HTTP) public void setShare(String value) {
		/*ui().setTextView2(value);*/

		throw  new SKYHttpException("哈哈哈");
	}

	@Interceptor(1) @Background(BackgroundType.WORK) public void errormethod() {
		Object obj = null;
		obj.toString();
	}

	@Override public boolean interceptBizError(int interceptor, Throwable throwable) {
		switch (interceptor) {
			case 0:
				L.i("默认的方法拦截");
				break;
			case 1:
				L.i("自定义" + interceptor);
				break;
			case 2:
				L.i("自定义" + interceptor);
				break;
		}
		return super.interceptBizError(interceptor, throwable);
	}

}