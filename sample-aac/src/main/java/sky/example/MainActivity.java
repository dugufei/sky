package sky.example;

import android.os.Bundle;

import sk.SKActivity;
import sk.SKActivityBuilder;
import sky.example.fragment.HelloFragment;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:07
 * @see MainActivity
 */
public class MainActivity extends SKActivity<MainBiz> {
	@Override protected SKActivityBuilder build(SKActivityBuilder skBuilder) {
		skBuilder.layoutId(R.layout.activity_main);
		return skBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
		getSupportFragmentManager().beginTransaction().add(R.id.ll_main,HelloFragment.getInstance(), "aaaa").commit();

	}
}
