package com.example.sky;

import android.os.Bundle;

import sky.Background;
import sky.BackgroundType;
import sky.core.SKYBiz;
import sky.core.SKYHelper;

/**
 * @author sky
 * @date Created on 2017-11-23 下午5:55
 * @version 1.0
 * @Description ShareBiz - 描述
 */
public class ShareBiz extends SKYBiz {

	@Override protected void initBiz(Bundle bundle) {
		super.initBiz(bundle);
	}

	@Background(BackgroundType.HTTP) public void load() {
		SKYHelper.ui(ShareActivity.class).loading();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SKYHelper.ui(ShareActivity.class).closeLoading();
		SKYHelper.ui(ShareActivity.class).close();
		biz(MainBiz.class).load();
	}
}