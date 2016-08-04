package jc.sky.view.adapter.recycleview;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.core.SKYIBiz;
import jc.sky.display.SKYIDisplay;
import jc.sky.view.SKYActivity;
import jc.sky.view.SKYDialogFragment;
import jc.sky.view.SKYFragment;
import jc.sky.view.SKYView;

/**
 * @创建人 sky
 * @创建时间 15/7/17 上午10:51
 * @类描述 RecyclerView 适配器
 */
public abstract class SKYRVAdapter<T, V extends SKYHolder> extends RecyclerView.Adapter<V> {

	public abstract V newViewHolder(ViewGroup viewGroup, int type);

	private SKYRVAdapter() {}

	/**
	 * 数据
	 */
	private List	mItems;

	private SKYView SKYView;

	public SKYRVAdapter(SKYActivity SKYActivity) {
		SKYCheckUtils.checkNotNull(SKYActivity, "View层不存在");
		this.SKYView = SKYActivity.SKYView();
	}

	public SKYRVAdapter(SKYFragment SKYFragment) {
		SKYCheckUtils.checkNotNull(SKYFragment, "View层不存在");
		this.SKYView = SKYFragment.SKYView();
	}

	public SKYRVAdapter(SKYDialogFragment SKYDialogFragment) {
		SKYCheckUtils.checkNotNull(SKYDialogFragment, "View层不存在");
		this.SKYView = SKYDialogFragment.SKYView();
	}

	@Override public V onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		V holder = newViewHolder(viewGroup, viewType);
		return holder;
	}

	@Override public void onBindViewHolder(V v, int position) {
		v.bindData(getItem(position), getItemCount());
	}

	public List<T> getItems() {
		return mItems;
	}

	public void setItems(List items) {
		mItems = items;
		notifyDataSetChanged();
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

	public <V extends SKYFragment> V fragment() {
		return SKYView.fragment();
	}

	public <A extends SKYActivity> A activity() {
		return SKYView.activity();
	}

	public <D extends SKYDialogFragment> D dialogFragment() {
		return SKYView.dialogFragment();
	}

	/**
	 * 获取适配器
	 * 
	 * @return
	 */
	protected SKYRVAdapter getAdapter() {
		return this;
	}

	/**
	 * 获取fragment
	 *
	 * @param clazz
	 * @return
	 */
	public <T> T findFragment(Class<T> clazz) {
		SKYCheckUtils.checkNotNull(clazz, "class不能为空");
		return (T) SKYView.manager().findFragmentByTag(clazz.getSimpleName());
	}

	public SKYView getUI() {
		return SKYView;
	}

	public <B extends SKYIBiz> B biz(Class<B> service) {
		return SKYView.biz(service);
	}

	/**
	 * 获取调度
	 *
	 * @param e
	 * @param <E>
	 * @return
	 */
	protected <E extends SKYIDisplay> E display(Class<E> e) {
		return SKYView.display(e);
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

	public void clearCache() {
		if (mItems != null) {
			mItems.clear();
			mItems = null;
		}
	}
}