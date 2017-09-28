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
		initialSKYBuilder.layoutBizErrorId(R.layout.layout_biz_error);
		initialSKYBuilder.layoutHttpErrorId(R.layout.layout_http_error);
		return initialSKYBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
		biz().init("我被点了啊啊啊");
	}

	@OnClick(R.id.button) public void btn(View view) {
//		biz().login();
		biz(NotifyBiz.class).updateData();
	}

	@OnClick(R.id.tv_error) public void error(View view) {
		showContent();
	}

	@OnClick(R.id.tv_http_error) public void httpError(View view) {
		showContent();
	}

	public void fff(int i, Object a) {
		tvText.setText("a.toStringe()");
	}

}
