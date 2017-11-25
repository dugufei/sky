package com.example.sky;

import android.os.Bundle;

import sky.Background;
import sky.BackgroundType;
import sky.core.SKYBiz;

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

	public void setShare(String value) {
		ui().setTextView2(value);
	}
}