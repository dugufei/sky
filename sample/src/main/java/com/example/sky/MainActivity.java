package com.example.sky;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import jc.sky.SKYHelper;
import jc.sky.view.SKYActivity;
import jc.sky.view.SKYBuilder;
import sky.IUI;
import sky.IType;

public class MainActivity extends SKYActivity<IMainBiz> implements IMainActivity {

	@BindView(R.id.tv_text) TextView tvText;

	@Override protected SKYBuilder build(SKYBuilder initialSKYBuilder) {
		initialSKYBuilder.layoutId(R.layout.activity_main);
		return initialSKYBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {

	}

	@OnClick(R.id.tv_text) public void login(View view) {
		biz().login(view.getContext());
	}

	@IUI(IType.HTTP) public void aaa(int i, Object a) {}

	@IUI(IType.WORK) public void bbb(int i, float a) {}

	@IUI public void ccc(int i, Object a) {}

	@IUI public void ddd(int i, Object a) {}

	@IUI public void fff(int i, Object a) {
		SKYHelper.toast().show(a.toString());
	}
}
