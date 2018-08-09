package sk;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import sk.livedata.list.SKLoadMoreHolder;

/**
 * @author sky
 * @version 1.0 on 2018-06-13 下午9:03
 * @see SKCommonView
 */
public interface SKCommonView {

	/**
	 * 进度布局
	 *
	 * @return 返回值
	 */
	int layoutLoading();

	/**
	 * 空布局
	 *
	 * @return 返回值
	 */
	int layoutEmpty();

	/**
	 * 错误布局
	 *
	 * @return 返回值
	 */
	int layoutError();

	/**
	 * 适配器 加载更多
	 * 
	 * @return 返回值
	 * @param layoutInflater
	 * @param viewGroup
	 * @param viewType
	 */
	SKLoadMoreHolder adapterLoadMore(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType);

	/**
	 * 适配器未知类型
	 * 
	 * @return
	 */
	SKHolder adapterUnknownType(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType);

	SKCommonView NULL = new SKCommonView() {

		@Override public int layoutLoading() {
			return 0;
		}

		@Override public int layoutEmpty() {
			return 0;
		}

		@Override public int layoutError() {
			return 0;
		}

		@Override public SKLoadMoreHolder adapterLoadMore(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType) {
			return null;
		}

		@Override public SKHolder adapterUnknownType(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType) {
			return null;
		}

	};
}
