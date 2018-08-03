package sky.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import sk.L;
import sk.SKActivity;
import sk.SKActivityBuilder;
import sk.builder.SKViewStub;
import sk.livedata.SKObserver;
import sky.example.bean.User;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:07
 * @see MainActivity
 */
public class MainActivity extends SKActivity<MainBiz> {

	@BindView(R.id.tv_one) TextView		textView;

	@BindView(R.id.tv_two) TextView		textTwo;

	@BindView(R.id.tv_three) TextView	tvThree;

	@Override protected SKActivityBuilder build(SKActivityBuilder skBuilder) {
		skBuilder.layoutId(R.layout.activity_main);
		skBuilder.layoutErrorViewSub(new Error());
		return skBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
		biz().getUserSKData().observe(this, new SKViewObserver<User>() {

			@Override public void onChanged(@Nullable User user) {
				L.i("执行onChanged");
				textView.setText(user.name);
			}
		});

		biz().getStringSKData().observe(this, string -> textTwo.setText(string));
		biz().getUserHomeSKData().observe(this, user -> tvThree.setText(user.name));
	}

	@OnClick(R.id.tv_one) public void onViewClicked() {
		biz().change("改改改");
		// OneActivity.intent();
	}

	public class Error extends SKViewStub {

		@OnClick(R.id.tv_error) public void onError() {
			biz().load();
		}
	}
}
