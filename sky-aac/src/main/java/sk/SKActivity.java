package sk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 上午11:26
 * @see SKActivity
 */
public class SKActivity<M extends SKViewModel> extends AppCompatActivity {

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SKInputs.inject(this);
		initViewModel();
	}

	protected M model;

	private void initViewModel() {
		Class clazz = SKCoreUtils.getClassGenricType(this.getClass(), 0);
//		model = (M) ViewModelProviders.of(this).get(clazz);
	}
}
