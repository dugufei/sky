package com.example.sky;

import jc.sky.core.SKYBiz;
import sky.Background;
import sky.BackgroundType;

import android.os.Bundle;

/**
 * @author sky
 * @date Created on 2017-11-19 下午10:43
 * @version 1.0
 * @Description LoginBiz - 描述
 */
public class LoginBiz extends SKYBiz<LoginFragment> {

	@Override protected void initBiz(Bundle bundle) {
		super.initBiz(bundle);
	}

	public void tip() {}

	@Background(BackgroundType.HTTP) public void login(String s, String s1) {

		// 模拟网络请求
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ui().setText(s + ":" + s1);
	}
}