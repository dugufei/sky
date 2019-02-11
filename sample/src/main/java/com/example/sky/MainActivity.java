package com.example.sky;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.sky.test.MainVM;
import com.example.sky.test.User;

import butterknife.BindView;
import butterknife.OnClick;
import sky.OpenMethod;
import sky.core.SKYActivity;
import sky.core.SKYBuilder;
import sky.core.SKYHelper;
import sky.core.SKYIDisplay;

/**
 * @author sky
 * @date Created on 2017-11-19 下午5:58
 * @version 1.0
 * @Description MainActivity - 描述
 */
public class MainActivity extends SKYActivity<MainBiz> implements TipDialogFragment.ITipDialog {

	@BindView(R.id.textView2) TextView textView2;

	@OpenMethod(Api.FFFF) public static final int intent(String a, User user) {
		SKYHelper.display(SKYIDisplay.class).intent(MainActivity.class);
		return 1;
	}

	public static final void intent(String a) {
		SKYHelper.display(SKYIDisplay.class).intent(MainActivity.class);
	}

	public static final void intent() {
		SKYHelper.display(SKYIDisplay.class).intent(MainActivity.class);
	}

	@Deprecated @Override protected SKYBuilder build(SKYBuilder initialSKYBuilder) {
		initialSKYBuilder.layoutId(R.layout.activity_main);
		initialSKYBuilder.layoutLoadingId(R.layout.layout_loading);
		initialSKYBuilder.layoutEmptyId(R.layout.layout_empty);
		initialSKYBuilder.layoutHttpErrorId(R.layout.layout_http);
		initialSKYBuilder.layoutBizErrorId(R.layout.layout_error);
		return initialSKYBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
		// biz().load();
		biz(MainBiz.class).tip();
		biz(NotifyBiz.class).aaa();

		MainVM mainVM = ViewModelProviders.of(this).get(MainVM.class);

		// mainVM.userMutableLiveData.observe(this, user -> {
		// textView2.setText(user.name);
		// });

	}

	@OnClick({ R.id.button2, R.id.button3, R.id.button6, R.id.tv_reload, R.id.tv_reload_a }) public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.button2:// 登录
				getSupportFragmentManager().beginTransaction().add(R.id.linearLayout, LoginFragment.getInstance(), "login").addToBackStack(null).commitAllowingStateLoss();
				break;
			case R.id.button3:
				// TipDialogFragment.getInstance().show(getSupportFragmentManager(), this, 100);
				// display(SKYIDisplay.class).intent(ShareActivity.class);
				biz().setShare("");
				break;
			case R.id.button6:
				// biz().errormethod();
//				SKYHelper.moduleBiz(555).run();
				// SKYHelper.moduleDisplay("ShareActivity").method("intent").run("sky", 1);
				// display(SKYIDisplay.class).intent(MainActivity.class);
				break;
			case R.id.tv_reload:
			case R.id.tv_reload_a:
				showContent();
				break;
			default:
				break;
		}
	}

	@Override public void showContent() {
		super.showContent();
	}

	@Override public void onCancelled(int requestCode) {
		textView2.setText("弹框回来:" + requestCode);
	}

	@Override public void ok() {
		textView2.setText("弹框回来: 成功啦");
	}

	public void setTextView2(String value) {
		textView2.setText(value);
	}
}