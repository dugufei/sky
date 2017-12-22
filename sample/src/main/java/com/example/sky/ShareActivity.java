package com.example.sky;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sky.helper.SampleHelper;

import butterknife.BindView;
import butterknife.OnClick;
import sky.OpenDisplay;
import sky.core.SKYActivity;
import sky.core.SKYBuilder;
import sky.core.SKYHelper;
import sky.core.SKYIDisplay;

/**
 * @author sky
 * @date Created on 2017-11-23 下午5:55
 * @version 1.0
 * @Description ShareActivity - 描述
 */
public class ShareActivity extends SKYActivity {

	@BindView(R.id.btn) Button			btn;

	@BindView(R.id.btn_loading) Button	btnLoading;

	@BindView(R.id.btn_close) Button	btnClose;

	@OpenDisplay public static final void intent(String name,int number) {
		Bundle bundle =new Bundle();
		bundle.putString("name",name);
		bundle.putInt("number",number);
		SKYHelper.display(SKYIDisplay.class).intent(ShareActivity.class,bundle);
	}

	@Override protected sky.core.SKYBuilder build(SKYBuilder initialSKYBuilder) {
		initialSKYBuilder.layoutId(R.layout.activity_share);
		return initialSKYBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
		if(savedInstanceState == null){
			return;
		}
		btn.setText(savedInstanceState.getString("name"));
		btnLoading.setText(String.valueOf(savedInstanceState.getInt("number")));
	}

	@OnClick(R.id.btn) public void onViewClicked() {
		// SKYHelper.bizList(MainBiz.class);
		// int i=0;
		// for(MainBiz mainBiz : SKYHelper.bizList(MainBiz.class)){
		// mainBiz.setShare("我被分享了" + i);
		// i++;
		// }

		// biz(MainBiz.class).setShare("我被分享了");
		// biz(TipBiz.class).tip();
		// String aaaa= SKYHelper.moduleBiz("NotifyBiz").method("aaa").run();
		// SKYHelper.toast().show(aaaa);

		SampleHelper.api().show();
	}

	@OnClick({ R.id.btn_loading, R.id.btn_close }) public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_loading:
				SKYHelper.biz(ShareBiz.class).load();
				// close();
				// biz(MainBiz.class).load();
				// loading();
				break;
			case R.id.btn_close:
				closeLoading();
				break;
		}
	}
}