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
import sky.IUI;
import sky.IType;

public class MainActivity extends SKYActivity<MainBiz> {

	@BindView(R.id.tv_text) TextView tvText;

	@Override protected SKYBuilder build(SKYBuilder initialSKYBuilder) {
		initialSKYBuilder.layoutId(R.layout.activity_main);
		return initialSKYBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {

	}

	@OnClick(R.id.tv_text) public void login(View view) {
		display(SKYIDisplay.class).intent(AActivity.class);
	}

	@OnClick(R.id.button) public void btn(View view) {
		biz().aaaaa();
	}

	public void fff(int i, Object a) {
		tvText.setText("a.toStringe()");
		SKYHelper.toast().show(a.toString());
	}
}
