package sky.example;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import sk.SKActivity;
import sk.SKActivityBuilder;
import sk.SKHelper;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午1:52
 * @see OneActivity
 */
public class OneActivity extends SKActivity<OneViewModel> {

	@BindView(R.id.tv_me) TextView		tvMe;

	@BindView(R.id.tv_me_two) TextView	tvMeTwo;

	public static final void intent() {
		SKActivity skActivity = SKHelper.screen().getCurrentActivity();
		Intent intent = new Intent();
		intent.setClass(skActivity, OneActivity.class);
		skActivity.startActivity(intent);
	}

	@Override protected SKActivityBuilder build(SKActivityBuilder skBuilder) {
		skBuilder.layoutId(R.layout.activity_one);
		return skBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
		// tvMe.setText(user.name + "是个大好人" + string + ":" + skData);
//		model.skData.observe(this, user -> {
//			tvMe.setText(user.name);
//		});
	}

	@OnClick(R.id.tv_me) public void onViewClicked() {
//		model.change("我是第二页");
		model.update();
	}
}
