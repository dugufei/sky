package com.example.sky;

import jc.sky.core.SKYBiz;
import android.os.Bundle;

/**
 * @author sky
 * @date Created on 2017-11-19 下午5:58
 * @version 1.0
 * @Description MainBiz - 描述
 */
public class MainBiz extends SKYBiz<MainActivity> {

	@Override protected void initBiz(Bundle bundle) {
		super.initBiz(bundle);
	}

	public void load() {
		ui().showLoading();
	}

	public void tip() {}

}