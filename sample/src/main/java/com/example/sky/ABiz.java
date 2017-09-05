package com.example.sky;

import android.content.Context;

import jc.sky.SKYHelper;
import sky.Background;
import sky.BackgroundType;
import sky.Repeat;

/**
 * @author sky
 * @version 1.0 on 2017-07-27 上午9:36
 * @see ABiz
 */
public class ABiz extends CommenBiz<AActivity> {

	/**
	 * 登录
	 */
	@Background(BackgroundType.HTTP) public void login(Context context) {

		// SKYHelper.biz(MainBiz.class).login();
		//
		//// SKYHelper.ui(MainActivity.class).fff(1,000);
		//
		//
		//
		// ui().fff(1,000);

		Object object = null;

		object.hashCode();

	}

	@Override public boolean interceptBizError(Throwable throwable) {




		return false;

	}
}
