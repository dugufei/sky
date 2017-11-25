package sky.core;

import android.view.View;

/**
 * @author sky
 * @version 1.0 on 2017-11-20 上午12:07
 * @see SKYDefaultHolder
 */
public class SKYDefaultHolder<T> extends SKYHolder<T> {

	public SKYDefaultHolder(View itemView) {
		super(itemView);
	}

	@Override public void bindData(Object o, int position) {

	}
}
