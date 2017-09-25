package com.example.sky;

import android.content.Context;

import jc.sky.SKYHelper;
import jc.sky.core.SKYBiz;
import sky.Background;
import sky.BackgroundType;
import sky.IBIZ;
import sky.IParent;
import sky.IType;
import sky.Repeat;

/**
 * @author sky
 * @version 1.0 on 2017-07-27 上午9:36
 * @see MainBiz
 */
public class MainBiz extends SKYBiz<MainActivity> {

	/**
	 * 登录
	 */
	@Background(BackgroundType.WORK) public void login() {
		Object o = null;
		o.toString();

	}

	@Override public boolean interceptBizError(Throwable throwable) {
		SKYHelper.toast().show("这里处理了");
		return false;
	}

	public void ab(String vavav) {

	}
}
