package sk;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * @author sky 15/7/17 上午10:58
 * @version 版本 适配器优化holder
 */
public abstract class SKHolder<T> extends RecyclerView.ViewHolder {

	private SKAdapter adapter;

	public SKHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	public abstract void bindData(T t, int position);

	public void setAdapter(SKAdapter adapter) {
		this.adapter = adapter;
	}

	public <T extends SKAdapter> T getAdapter() {
		return (T) adapter;
	}

	public <B extends SKBiz> B biz(Class<B> bizClass) {
		return SKHelper.biz(bizClass);
	}

	public T getItem(int position) {
		return (T) adapter.getItems().get(position);
	}

}