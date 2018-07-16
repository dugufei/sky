package sk;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author sky
 * @version 1.0 on 2018-07-12 下午11:36
 * @see SKAdapter
 */
public abstract class SKAdapter<T, V extends SKHolder> extends RecyclerView.Adapter<V> {

	private static final int	VIEW_ITEM	= 0;

	private static final int	VIEW_PROG	= 9999;

	private static final int	VIEW_TOP	= 10000;

	public abstract V newViewHolder(ViewGroup viewGroup, int type);

	public V newLoadMoreHolder(ViewGroup viewGroup, int type) {
		return null;
	}

	public V newTopHolder(ViewGroup viewGroup, int type) {
		return null;
	}

	/**
	 * 数据
	 */
	private List mItems;

	public void setActivity(SKActivity skActivity) {

	}

	@Override public int getItemViewType(int position) {
		if (mItems == null || mItems.size() < 1) {
			return getCustomViewType(position);
		}
		if (position == 0) {
			return mItems.get(position) != null ? getCustomViewType(position) : VIEW_TOP;
		} else {
			return mItems.get(position) != null ? getCustomViewType(position) : VIEW_PROG;
		}
	}

	protected View inflate(ViewGroup viewGroup, int layout) {
		return LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
	}

	/**
	 * 自定义类型
	 *
	 * @param position
	 *            下标
	 * @return 类型
	 */
	public int getCustomViewType(int position) {
		return VIEW_ITEM;
	}

	@Override public V onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		V holder;
		if (viewType == VIEW_PROG) {
			holder = newLoadMoreHolder(viewGroup, viewType);
			holder.setAdapter(this);
		} else if (viewType == VIEW_TOP) {
			holder = newTopHolder(viewGroup, viewType);
			holder.setAdapter(this);
		} else {
			holder = newViewHolder(viewGroup, viewType);
			holder.setAdapter(this);
		}
		if (holder == null) {
			holder = holderIsNullHandel(viewGroup, viewType);
		}
		return holder;
	}

	public V holderIsNullHandel(ViewGroup viewGroup, int viewType) {
		// V holder = (V) new SKYDefaultHolder<>(new View(viewGroup.getContext()));
		// L.i("viewType = " + viewType + ", holder为空创建默认holder");
		// return holder;
		return null;
	}

	@Override public void onBindViewHolder(V v, int position) {
		v.bindData(getItem(position), position);
	}

	public List<T> getItems() {
		return mItems;
	}

	public void setItems(List items) {
		mItems = items;
		notifyDataSetChanged();
	}

	public void initItems(List items) {
		mItems = items;
	}

	public void add(int position, Object object) {
		if (object == null || getItems() == null || position < 0 || position > getItems().size()) {
			return;
		}
		mItems.add(position, object);
		notifyItemInserted(position);
	}

	public void add(Object object) {
		if (object == null || getItems() == null) {
			return;
		}
		mItems.add(object);
		notifyItemInserted(mItems.size());

	}

	public void addList(int position, List list) {
		if (list == null || list.size() < 1 || getItems() == null || position < 0 || position > getItems().size()) {
			return;
		}
		mItems.addAll(position, list);
		notifyItemRangeInserted(position, list.size());

	}

	public void addList(List list) {
		if (list == null || list.size() < 1 || getItems() == null) {
			return;
		}
		int postion = getItemCount();
		mItems.addAll(list);
		notifyItemRangeInserted(postion, list.size());
	}

	public void delete(int position) {
		if (getItems() == null || position < 0 || getItems().size() < position) {
			return;
		}
		mItems.remove(position);
		notifyItemRemoved(position);
	}

	public void delete(List list) {
		if (list == null || list.size() < 1 || getItems() == null) {
			return;
		}
		int position = getItemCount();
		mItems.removeAll(list);
		notifyItemRangeRemoved(position, list.size());
	}

	public void delete(int position, List list) {
		if (list == null || list.size() < 1 || getItems() == null) {
			return;
		}
		mItems.removeAll(list);
		notifyItemRangeRemoved(position, list.size());
	}

	public void clear() {
		mItems.clear();
		notifyDataSetChanged();
	}

	public T getItem(int position) {
		return (T) mItems.get(position);
	}

	public void updateData() {
		notifyDataSetChanged();
	}

	/**
	 * 获取适配器
	 *
	 * @return 返回值
	 */
	protected SKAdapter getAdapter() {
		return this;
	}

	@Override public int getItemCount() {
		if (mItems == null) {
			return 0;
		}
		return mItems.size();
	}

	public boolean isHeaderAndFooter(int position) {
		return false;
	}

	public void clearCache() {}
}