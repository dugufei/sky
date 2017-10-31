package com.example.sky;

import jc.sky.SKYHelper;
import jc.sky.core.SKYBiz;
import sky.Background;
import sky.BackgroundType;
import sky.OpenBiz;

/**
 * @author sky
 * @version 1.0 on 2017-07-27 上午9:36
 * @see MainBiz
 */
@OpenBiz
public class MainBiz extends SKYBiz<MainActivity> {

	/**
	 * 登录
	 */
	@Background(BackgroundType.WORK) public void login(int a, String b) {
		// Object o = null;
		// o.toString();
		SKYHelper.toast().show(a + ":b");
		// ui().showBizError();
	}

	@Override public boolean interceptBizError(String method, Throwable throwable) {
		SKYHelper.toast().show("这里处理了");

		return true;
	}

	public void ab(String vavav) {
		SKYHelper.toast().show(vavav);
	}

	String a = "aaa";

	public void init(String asdfasdf) {
		a = asdfasdf;

		SKYHelper.toast().show(a);

	}
}
