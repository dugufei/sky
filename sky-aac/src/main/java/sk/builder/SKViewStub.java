package sk.builder;

import android.view.View;

import butterknife.ButterKnife;

/**
 * @author sky
 * @version 1.0 on 2018-08-03 上午11:25
 * @see SKViewStub
 */
public class SKViewStub {

	void init(View view) {
		ButterKnife.bind(this, view);
	}
}
