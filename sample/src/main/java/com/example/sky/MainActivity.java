package com.example.sky;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import jc.sky.SKYHelper;
import jc.sky.display.SKYIDisplay;
import jc.sky.view.SKYActivity;
import jc.sky.view.SKYBuilder;

public class MainActivity extends SKYActivity<MainBiz> {

	@BindView(R.id.tv_text) TextView tvText;


	@Override protected SKYBuilder build(SKYBuilder initialSKYBuilder) {
		initialSKYBuilder.layoutId(R.layout.activity_main);
		return initialSKYBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {

	}


	@OnClick(R.id.tv_text) public void login(View view) {
		SKYHelper.toast().show("点我");

		display(SKYIDisplay.class).intent(AActivity.class);
	}



	@OnClick(R.id.button) public void btn(View view) {
		biz().login();

		SKYHelper.biz(ABiz.class).login(view.getContext());
	}

	public void fff(int i, Object a) {
		tvText.setText("a.toStringe()");
		SKYHelper.toast().show(a.toString());
	}


	public void b(){
		tvText.setText("我被调用了");

		tvText.post(new Runnable() {
			@Override
			public void run() {

			}
		});


	}
}
