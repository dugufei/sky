package com.example.sky;

import jc.sky.core.SKYBiz;
import sky.Background;
import sky.BackgroundType;

/**
 * @author sky
 * @version 1.0 on 2017-09-27 上午10:40
 * @see NotifyBiz
 */
public class NotifyBiz extends SKYBiz {

	/**
	 * 修改
	 */
	@Background(BackgroundType.WORK) public void updateData() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
