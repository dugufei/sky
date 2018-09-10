package com.example.sky;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import sky.core.SKYBuilder;
import sky.core.SKYFragment;

/**
 * @author sky
 * @date Created on 2017-11-19 下午10:43
 * @version 1.0
 * @Description LoginFragment - 描述
 */
public class LoginFragment extends SKYFragment<LoginBiz> {

	@BindView(R.id.editText) EditText	editText;

	@BindView(R.id.editText2) EditText	editText2;

	@BindView(R.id.textView) TextView	textView;

	public static final LoginFragment getInstance() {
		LoginFragment loginfragment = new LoginFragment();
		Bundle bundle = new Bundle();
		loginfragment.setArguments(bundle);
		return loginfragment;
	}

	@Override protected SKYBuilder build(SKYBuilder initialSKYBuilder) {
		initialSKYBuilder.layoutId(R.layout.fragment_login);
		return initialSKYBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {

	}

	@OnClick(R.id.button) public void onViewClicked() {
		loading();

		biz().load();
//		biz().login(editText.getText().toString(), editText2.getText().toString());
//		SKYHelper.moduleBiz("MainBiz").method("load").run();
	}

	@OnClick(R.id.textView) public void onClose() {
		close();
	}

	public void setText(String value) {
		textView.setText(value);
	}

}