package sky.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import sk.SKActivity;
import sk.SKData;
import sk.SKHelper;
import sky.SKInput;
import sky.SKSource;
import sky.example.bean.User;
import sky.example.textdi.hhh;
import sky.example.textdi2.CProvider;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:07
 * @see MainActivity
 */
@SKSource(CProvider.class)
public class MainActivity extends SKActivity<MainViewModel> {

	TextView		textView;

	@Inject User	user;

	@Inject SKData	skData;

	@SKInput hhh	hhh;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = findViewById(R.id.tv_one);
		textView.setOnClickListener(view -> {

			Intent intent = new Intent();
			intent.setClass(MainActivity.this, OneActivity.class);
			startActivity(intent);
		});
		Toast.makeText(this, user.name + " :" + skData, Toast.LENGTH_SHORT).show();

		model.load().observe(this, user -> {
			textView.setText(user.name + ":" + model.hashCode());
		});

		showFragment(savedInstanceState);

		SKHelper.input(this);

		hhh.init();
	}

	private void showFragment(Bundle savedInstanceState) {
		if (savedInstanceState == null) {

			MainFragment fragment = new MainFragment();

			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, null).commit();
		}

	}

}
