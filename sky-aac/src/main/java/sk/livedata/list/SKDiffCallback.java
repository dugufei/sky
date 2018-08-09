package sk.livedata.list;

import android.support.v7.util.DiffUtil;

/**
 * @author sky
 * @version 1.0 on 2018-08-03 下午1:54
 * @see SKDiffCallback
 */
public class SKDiffCallback<T extends SKModel> extends DiffUtil.ItemCallback<T> {

	/**
	 * 先比较 viewholder 是否复用
	 *
	 * @return
	 */
	@Override public boolean areItemsTheSame(T oldItem, T newItem) {
		return oldItem.type == newItem.type;
	}

	/**
	 * 在比较内容是否复用 -- 默认不同
	 *
	 * @return
	 */
	@Override public boolean areContentsTheSame(T oldItem, T newItem) {
		return false;
	}
}