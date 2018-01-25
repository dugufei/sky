package sky.core;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static sky.core.SKYUtils.checkNotNull;

/**
 * @author sky 15/7/17 上午10:51
 * @version 版本 RecyclerView 适配器
 */
public abstract class SKYRVAdapter<T, V extends SKYHolder> extends RecyclerView.Adapter<V> {

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

	private SKYRVAdapter() {}

	/**
	 * 数据
	 */
	private List	mItems;

	private SKYView	skyView;

	private int		state;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public SKYRVAdapter(SKYActivity SKYActivity) {
		checkNotNull(SKYActivity, "View层不存在");
		this.skyView = SKYActivity.SKYView();
	}

	public SKYRVAdapter(SKYFragment SKYFragment) {
		checkNotNull(SKYFragment, "View层不存在");
		this.skyView = SKYFragment.SKYView();
	}

	public SKYRVAdapter(SKYDialogFragment SKYDialogFragment) {
		checkNotNull(SKYDialogFragment, "View层不存在");
		this.skyView = SKYDialogFragment.SKYView();
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
		V holder = (V) new SKYDefaultHolder<>(new View(viewGroup.getContext()));
		L.i("viewType = " + viewType + ", holder为空创建默认holder");
		return holder;
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

	public <V extends SKYFragment> V fragment() {
		return skyView.fragment();
	}

	public <A extends SKYActivity> A activity() {
		return skyView.activity();
	}

	public <D extends SKYDialogFragment> D dialogFragment() {
		return skyView.dialogFragment();
	}

	/**
	 * 获取适配器
	 *
	 * @return 返回值
	 */
	protected SKYRVAdapter getAdapter() {
		return this;
	}

	/**
	 * 获取fragment
	 *
	 * @param <T>
	 *            参数
	 * @param clazz
	 *            参数
	 * @return 返回值
	 */
	public <T> T findFragment(Class<T> clazz) {
		checkNotNull(clazz, "class不能为空");
		return (T) skyView.manager().findFragmentByTag(clazz.getSimpleName());
	}

	public SKYView getUI() {
		return skyView;
	}

	public <B extends SKYBiz> B biz(Class<B> service) {
		return skyView.biz(service);
	}

	/**
	 * 获取调度
	 *
	 * @param e
	 *            参数
	 * @param <E>
	 *            参数
	 * @return 返回值
	 */
	protected <E extends SKYIDisplay> E display(Class<E> e) {
		return skyView.display(e);
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