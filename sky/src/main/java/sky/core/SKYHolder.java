package sky.core;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

/**
 * @author sky 15/7/17 上午10:58
 * @version 版本 适配器优化holder
 */
public abstract class SKYHolder<T> extends RecyclerView.ViewHolder {

	private SKYRVAdapter adapter;

	public SKYHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	public abstract void bindData(T t, int position);

	public void setAdapter(SKYRVAdapter adapter) {
		this.adapter = adapter;
	}

	public <T extends SKYRVAdapter> T getAdapter() {
		return (T) adapter;
	}

	public <B extends SKYBiz> B biz(Class<B> service) {
		return (B) adapter.biz(service);
	}

	protected <E extends SKYIDisplay> E display(Class<E> e) {
		return (E) adapter.display(e);
	}

	public T getItem(int position) {
		return (T) adapter.getItem(position);
	}

}