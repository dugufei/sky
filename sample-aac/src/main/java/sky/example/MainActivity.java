package sky.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import sk.SKActivity;
import sk.SKData;
import sk.SKHelper;
import sky.SKInput;
import sky.example.bean.User;
import sky.example.textdi.A;
import sky.example.textdi.B;
import sky.example.textdi.D;
import sky.example.textdi.hhh;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:07
 * @see MainActivity
 */
public class MainActivity extends SKActivity<MainViewModel> {

	TextView		textView;

	@Inject User	user;

	@Inject SKData	skData;

	@SKInput hhh	hhh;

	@SKInput A		a;

	@SKInput B		b;

	@SKInput D		d;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = findViewById(R.id.tv_one);
		textView.setOnClickListener(view -> {

			hhh.init(a, b);

			// Intent intent = new Intent();
			// intent.setClass(MainActivity.this, OneActivity.class);
			// startActivity(intent);
		});
		Toast.makeText(this, user.name + " :" + skData, Toast.LENGTH_SHORT).show();

		model.load().observe(this, user -> {
			textView.setText(user.name + ":" + model.hashCode());
		});

		showFragment(savedInstanceState);
	}

	private void showFragment(Bundle savedInstanceState) {
		if (savedInstanceState == null) {

			MainFragment fragment = new MainFragment();

			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, null).commit();
		}

	}

}
