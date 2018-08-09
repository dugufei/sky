package sk.livedata.list;

import android.support.v7.util.DiffUtil;

/**
 * @author sky
 * @version 1.0 on 2018-08-04 上午11:33
 * @see SKDIFF
 */
public abstract class SKDIFF<T> extends DiffUtil.ItemCallback<T> {

	@Override public boolean areItemsTheSame(T oldItem, T newItem) {
		return oldItem == newItem;
	}
}
