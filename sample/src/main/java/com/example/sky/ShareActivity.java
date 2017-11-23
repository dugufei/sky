package com.example.sky;

import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jc.sky.core.SKYHelper;
import jc.sky.display.SKYIDisplay;
import jc.sky.view.SKYActivity;
import jc.sky.view.SKYBuilder;

/**
 * @author sky
 * @date Created on 2017-11-23 下午5:55
 * @version 1.0
 * @Description ShareActivity - 描述
 */
public class ShareActivity extends SKYActivity<ShareBiz> {

	@BindView(R.id.btn) Button btn;

	public static final void intent() {
		SKYHelper.display(SKYIDisplay.class).intent(ShareActivity.class);
	}

	@Override protected SKYBuilder build(SKYBuilder initialSKYBuilder) {
		initialSKYBuilder.layoutId(R.layout.activity_share);
		return initialSKYBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {

	}

	@OnClick(R.id.btn) public void onViewClicked() {
//		 SKYHelper.bizList(MainBiz.class);
//		 int i=0;
//		for(MainBiz mainBiz : SKYHelper.bizList(MainBiz.class)){
//			mainBiz.setShare("我被分享了" + i);
//			i++;
//		}

	    biz(MainBiz.class).setShare("我被分享了");
	    biz(TipBiz.class).tip();
    }
}