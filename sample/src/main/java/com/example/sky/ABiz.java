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
	@Repeat(true) @Background(BackgroundType.HTTP) public void login(Context context) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		SKYHelper.ui(MainActivity.class).fff(1,000);

//		ui().fff(1, 000);

	}
}
