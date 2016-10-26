package jc.sky.view.adapter.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * @author sky
 *  15/7/17 上午10:58
 * @version 版本 适配器优化holder
 */
public abstract class SKYHolder<T> extends RecyclerView.ViewHolder {

	public SKYHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	public abstract void bindData(T t, int count);

}