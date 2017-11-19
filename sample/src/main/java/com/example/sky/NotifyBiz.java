package com.example.sky;

import com.example.sky.oder.OderActivity;

import jc.sky.core.SKYBiz;
import jc.sky.display.SKYIDisplay;
import sky.Background;
import sky.BackgroundType;
import sky.OpenBiz;

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

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		biz(LoginBiz.class).tip();
		biz(TipBiz.class).tip();
		biz(MainBiz.class).tip();
	}

	public void intentOder() {
		display(SKYIDisplay.class).intent(OderActivity.class);
	}
}
