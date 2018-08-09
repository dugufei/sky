package sk;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sk.livedata.list.SKLoadMoreCallBack;
import sk.livedata.list.SKLoadMoreHolder;
import sk.livedata.list.SKNetworkState;

/**
 * @author sky
 * @version 1.0 on 2018-07-12 下午11:36
 * @see SKAdapter
 */
public abstract class SKAdapter<T, V extends SKHolder> extends PagedListAdapter<T, V> {

	private static final int	VIEW_TOP		= 10000;

	private static final int	VIEW_LOAD_MORE	= 20000;

	private SKNetworkState		oldNetworkState;

	private SKLoadMoreCallBack	skLoadMoreCallBack;

	protected SKAdapter(@NonNull DiffUtil.ItemCallback diffCallback, @NonNull SKLoadMoreCallBack skLoadMoreCallBac) {
		super(diffCallback);
		this.skLoadMoreCallBack = skLoadMoreCallBac;
	}

	protected SKAdapter(@NonNull AsyncDifferConfig config, @NonNull SKLoadMoreCallBack skLoadMoreCallBack) {
		super(config);
		this.skLoadMoreCallBack = skLoadMoreCallBack;
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

	@Override public int getItemCount() {
		int loadMore = hasExtraRow() ? 1 : 0;
		return super.getItemCount() + loadMore;
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

	@Nullable public List<T> getItems() {
		return getCurrentList();
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
				notifyItemRemoved(super.getItemCount());
			} else {
				notifyItemInserted(super.getItemCount());
			}
		} else if (hasExtraRow && previousState != newNetworkState) {
			notifyItemChanged(getItemCount() - 1);
		}
	}
}