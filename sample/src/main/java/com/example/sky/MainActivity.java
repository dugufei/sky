package com.example.sky;

import jc.sky.core.SKYHelper;
import jc.sky.display.SKYIDisplay;
import android.os.Bundle;
import jc.sky.view.SKYActivity;
import jc.sky.view.SKYBuilder;

/**
 * @author sky
 * @date Created on 2017-11-19 下午5:58
 * @version 1.0
 * @Description MainActivity - 描述
 */
public class MainActivity extends SKYActivity<MainBiz> {

    public static final void intent() {
		SKYHelper.display(SKYIDisplay.class).intent(MainActivity.class);
	}

    @Override protected SKYBuilder build(SKYBuilder initialSKYBuilder) {
        initialSKYBuilder.layoutId(R.layout.activity_main);
        initialSKYBuilder.layoutLoadingId(R.layout.layout_loading);
        return initialSKYBuilder;
    }
    @Override protected void initData(Bundle savedInstanceState) {
        biz().load();
    }
    
}