package sky.example;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sk.SKFragment;

/**
 * @author sky
 * @version 1.0 on 2018-04-29 下午2:06
 * @see MainFragment
 */
public class MainFragment extends SKFragment<MainViewModel> {

	TextView textView;

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		textView = view.findViewById(R.id.tv_one);
		textView.setOnClickListener(view1 -> {

			// Intent intent = new Intent();
			// intent.setClass(MainActivity.this, OneActivity.class);
			// startActivity(intent);
		});

		model.load1().observe(this, user -> {
			textView.setText(user.name+"1" + model.hashCode());
		});
		return view;
	}
}
