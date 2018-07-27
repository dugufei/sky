package sky.example;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import sk.SKActivity;
import sk.SKActivityBuilder;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:07
 * @see MainActivity
 */
public class MainActivity extends SKActivity<MainViewModel> {

	@BindView(R.id.tv_one) TextView		textView;

	@BindView(R.id.tv_two) TextView		textTwo;

	@BindView(R.id.tv_three) TextView	tvThree;

	@Override protected SKActivityBuilder build(SKActivityBuilder skBuilder) {
		skBuilder.layoutId(R.layout.activity_main);
		return skBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
		model.userSKData.observe(this, user -> textView.setText(user.name));
		model.stringSKData.observe(this, string -> textTwo.setText(string));
		model.userHomeSKData.observe(this, user -> tvThree.setText(user.name));
	}

	private void showFragment(Bundle savedInstanceState) {
		if (savedInstanceState == null) {

			MainFragment fragment = new MainFragment();

			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, null).commit();
		}

	}

	@OnClick(R.id.tv_one) public void onViewClicked() {
		model.change("改改改");
		// OneActivity.intent();
	}
}
