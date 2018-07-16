package sky.example;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import sk.SKActivity;
import sk.SKBuilder;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午1:52
 * @see OneActivity
 */
public class OneActivity extends SKActivity<OneViewModel> {

	@BindView(R.id.tv_me) TextView	tvMe;

	@Override protected SKBuilder build(SKBuilder skBuilder) {
		skBuilder.layoutId(R.layout.activity_one);
		return skBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
//		tvMe.setText(user.name + "是个大好人" + string + ":" + skData);
		model.load().observe(this, user -> {
			((TextView) findViewById(R.id.tv_me)).setText(user.name);
		});
	}

	@OnClick(R.id.tv_me) public void onViewClicked() {}
}
