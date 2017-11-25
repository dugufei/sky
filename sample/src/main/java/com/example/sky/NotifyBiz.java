package com.example.sky;

import com.example.sky.oder.OderActivity;

import sky.Background;
import sky.BackgroundType;
import sky.OpenBiz;
import sky.core.SKYBiz;
import sky.core.SKYHelper;
import sky.core.SKYIDisplay;

/**
 * @author sky
 * @version 1.0 on 2017-09-27 上午10:40
 * @see NotifyBiz
 */
@OpenBiz
public class NotifyBiz extends SKYBiz {

	/**
	 * 修改
	 */
	@Background(BackgroundType.WORK) public void notifyTip() {

		SKYHelper.toast().show("执行了");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		biz(LoginBiz.class).tip();
		biz(TipBiz.class).tip();
		biz(MainBiz.class).tip();
	}

	public String aaa(){
		return "进餐时天才";
	}

	public void intentOder() {
		display(SKYIDisplay.class).intent(OderActivity.class);
	}
}
