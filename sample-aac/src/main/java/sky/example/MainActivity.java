package sky.example;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import sk.SKActivity;
import sk.SKBuilder;
import sky.example.helper.TextHelper;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:07
 * @see MainActivity
 */
public class MainActivity extends SKActivity<MainViewModel> {

	@BindView(R.id.tv_one) TextView textView;

	@Override protected SKBuilder build(SKBuilder skBuilder) {
		skBuilder.layoutId(R.layout.activity_main);
		return skBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
		model.load().observe(this, user -> {
			textView.setText(user.name);
		});
	}

	private void showFragment(Bundle savedInstanceState) {
		if (savedInstanceState == null) {

			MainFragment fragment = new MainFragment();

			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, null).commit();
		}

	}

	@OnClick(R.id.tv_one) public void onViewClicked() {
		textView.setText("我是谁111");
		model.change();
		TextHelper.toast().show("你好啊啊啊");
		TextHelper.toast().show(TextHelper.abc().MM);
	}
}
