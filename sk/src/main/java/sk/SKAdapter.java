package sk;

import androidx.paging.PagedList;
import androidx.paging.SKAsyncPagedListDiffer;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import sk.livedata.SKLoadMoreCallBack;
import sk.livedata.SKLoadMoreHolder;
import sk.livedata.SKNetworkState;

/**
 * @author sky
 * @version 1.0 on 2018-07-12 下午11:36
 * @see SKAdapter
 */
public abstract class SKAdapter<T, V extends SKHolder> extends RecyclerView.Adapter<V> {

	private static final int									VIEW_TOP		= 10000;

	private static final int									VIEW_LOAD_MORE	= 20000;

	private SKNetworkState										oldNetworkState;

	private SKLoadMoreCallBack									skLoadMoreCallBack;

	private final SKAsyncPagedListDiffer<T>						mDiffer;

	private final SKAsyncPagedListDiffer.PagedListListener<T>	mListener		= new SKAsyncPagedListDiffer.PagedListListener<T>() {

																					@Override public void onCurrentListChanged(@Nullable PagedList<T> currentList) {
																						SKAdapter.this.onCurrentListChanged(currentList);
																					}
																				};

	public void onCurrentListChanged(@Nullable PagedList<T> currentList) {}

	final List<T> items;

	protected SKAdapter() {
		items = new ArrayList<>();
		mDiffer = null;
	}

	protected SKAdapter(@NonNull DiffUtil.ItemCallback diffCallback, @NonNull SKLoadMoreCallBack skLoadMoreCallBac) {
		mDiffer = new SKAsyncPagedListDiffer<T>(this, diffCallback);
		mDiffer.mListener = mListener;
		this.skLoadMoreCallBack = skLoadMoreCallBac;
		this.items = null;
	}

	protected SKAdapter(@NonNull AsyncDifferConfig config, @NonNull SKLoadMoreCallBack skLoadMoreCallBack) {
		mDiffer = new SKAsyncPagedListDiffer<>(new AdapterListUpdateCallback(this), config);
		mDiffer.mListener = mListener;
		this.skLoadMoreCallBack = skLoadMoreCallBack;
		this.items = null;
	}

	public abstract int layoutID(int position);

	public abstract V newHolder(int viewType, View view, Context context);

	protected V loadMore(LayoutInflater from, ViewGroup viewGroup, int viewType) {
		return null;
	}

	public V topHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int type) {
		return null;
	}

	public V itemUnknownType(LayoutInflater from, ViewGroup viewGroup, int viewType) {
		return null;
	}

	@Override public int getItemViewType(int position) {
		if (hasExtraRow() && position == getItemCount() - 1) {
			return VIEW_LOAD_MORE;
		}

		if (position == 0 && getItem(position) == null) {
			return VIEW_TOP;
		}
		return layoutID(position);
	}

	@Override public V onCreateViewHolder(ViewGroup viewGroup, int viewType) {

		V holder;
		switch (viewType) {
			case VIEW_LOAD_MORE:
				holder = loadMore(LayoutInflater.from(viewGroup.getContext()), viewGroup, viewType);
				if (holder == null) {
					holder = (V) SKHelper.commonView().adapterLoadMore(LayoutInflater.from(viewGroup.getContext()), viewGroup, viewType);
				}
				((SKLoadMoreHolder) holder).setCallBack(skLoadMoreCallBack);
				break;
			case VIEW_TOP:
				holder = topHolder(LayoutInflater.from(viewGroup.getContext()), viewGroup, viewType);
				break;
			default:
				holder = newHolder(viewType, LayoutInflater.from(viewGroup.getContext()).inflate(viewType, viewGroup, false), viewGroup.getContext());
				break;
		}

		if (holder != null) {
			holder.setAdapter(this);
		}

		if (holder == null) {
			holder = itemUnknownType(LayoutInflater.from(viewGroup.getContext()), viewGroup, viewType);
			if (holder == null) {
				holder = (V) SKHelper.commonView().adapterUnknownType(LayoutInflater.from(viewGroup.getContext()), viewGroup, viewType);
			}
		}

		return holder;
	}

	@Override public void onBindViewHolder(V v, int position) {
		switch (getItemViewType(position)) {
			case VIEW_LOAD_MORE:
				((SKLoadMoreHolder) v).bindData(oldNetworkState, position);
				break;
			default:
				v.bindData(getItem(position), position);
				break;
		}
	}

	public void setItems(PagedList<T> pagedList) {
		if (mDiffer == null) {
			return;
		}
		mDiffer.submitList(pagedList);
	}

	public void setItems(List<T> list) {
		this.items.clear();
		this.items.addAll(list);
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

	public boolean isHeaderAndFooter(int position) {
		return false;
	}

	private boolean hasExtraRow() {
		return oldNetworkState != null && oldNetworkState != SKNetworkState.SUCCESS;
	}

	/**
	 * 设置网络状态
	 * 
	 * @param newNetworkState
	 */
	public void setNetworkState(SKNetworkState newNetworkState) {
		SKNetworkState previousState = this.oldNetworkState;
		boolean hadExtraRow = hasExtraRow();
		this.oldNetworkState = newNetworkState;
		boolean hasExtraRow = hasExtraRow();
		if (hadExtraRow != hasExtraRow) {
			if (hadExtraRow) {
				notifyItemRemoved(itemCount());
			} else {
				notifyItemInserted(itemCount());
			}
		} else if (hasExtraRow && previousState != newNetworkState) {
			notifyItemChanged(getItemCount() - 1);
		}
	}

	@Nullable protected T getItem(int position) {
		return mDiffer == null ? items.get(position) : mDiffer.getItem(position);
	}

	@Override public int getItemCount() {
		int loadMore = hasExtraRow() ? 1 : 0;
		return itemCount() + loadMore;
	}

	private int itemCount() {
		return mDiffer == null ? items.size() : mDiffer.getItemCount();
	}

	@Nullable public List<T> getItems() {
		return mDiffer == null ? items : mDiffer.getCurrentList();
	}

}