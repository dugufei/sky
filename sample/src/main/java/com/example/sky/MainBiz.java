package com.example.sky;

import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;

import com.example.sky.helper.SampleHelper;
import com.example.sky.test.User;

import sky.Background;
import sky.BackgroundType;
import sky.Interceptor;
import sky.core.L;
import sky.core.SKYBiz;

/**
 * @author sky
 * @date Created on 2017-11-19 下午5:58
 * @version 1.0
 * @Description MainBiz - 描述
 */
public class MainBiz extends SKYBiz<MainActivity> {

	MutableLiveData<User> mutableLiveData = new MutableLiveData<>();

	@Override protected void initBiz(Bundle bundle) {
		super.initBiz(bundle);
	}

	public User aa(String a, String b, int c, User user) {
		return null;
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
		/* ui().setTextView2(value); */
		User user = new User();
		user.name = "我被点击啦";
		mutableLiveData.postValue(user);
	}

	@Interceptor(1) @Background(BackgroundType.WORK) public void errormethod() {

		SampleHelper.api().show();
		// Object obj = null;
		// obj.toString();
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
			default:
				break;
		}
		return super.interceptBizError(interceptor, throwable);
	}

}