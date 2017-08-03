package com.example.sky;

import android.content.Context;
import sky.IBIZ;
import sky.IParent;
import sky.IType;
import sky.Repeat;

/**
 * @author sky
 * @version 1.0 on 2017-07-27 上午9:36
 * @see MainBiz
 */
@IParent("com.example.sky.ICommenBiz")
public class MainBiz extends CommenBiz<IMainActivity> implements IMainBiz {

	/**
	 * 登录
	 */
	@Repeat(true) @IBIZ(IType.WORK) public void login(Context context) {
		ui().fff(1, 000);
	}
}
