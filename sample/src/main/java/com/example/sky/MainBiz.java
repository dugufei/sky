package com.example.sky;

import android.content.Context;

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
public class MainBiz extends CommenBiz<MainActivity> {

	/**
	 * 登录
	 */
	@Repeat(true) @Background(BackgroundType.HTTP) public void login(Context context) {
		ui().fff(1, 000);
	}
}
