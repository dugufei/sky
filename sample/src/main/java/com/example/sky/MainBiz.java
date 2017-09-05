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
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		SKYHelper.ui(MainActivity.class).fff(1,000);


		ui().b();

	}

	public void ab(String vavav){

	}
}
