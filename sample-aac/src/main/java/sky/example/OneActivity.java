package sky.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import sk.SKActivity;
import sk.SKData;
import sky.example.bean.User;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午1:52
 * @see OneActivity
 */
public class OneActivity extends SKActivity<OneViewModel> {

	@Inject User	user;

	@Inject String	string;

	@Inject SKData	skData;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one);
		((TextView) findViewById(R.id.tv_me)).setText(user.name + "是个大好人" + string+":"+skData);


//		model.load().observe(this, user -> {
//			((TextView) findViewById(R.id.tv_me)).setText(user.name);
//		});

	}
}
