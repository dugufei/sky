package com.example.sky;

import jc.sky.core.SKYBiz;
import sky.Background;
import sky.BackgroundType;

import android.os.Bundle;

/**
 * @author sky
 * @date Created on 2017-11-23 下午5:55
 * @version 1.0
 * @Description ShareBiz - 描述
 */
public class ShareBiz extends SKYBiz<ShareActivity> {

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
		ui().close();
		biz(MainBiz.class).load();
	}
}