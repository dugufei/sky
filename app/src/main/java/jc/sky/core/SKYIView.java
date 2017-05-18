package jc.sky.core;

import jc.sky.view.adapter.recycleview.SKYRVAdapter;

/**
 * @author sky
 * @version 1.0 on 2016-11-30 下午3:11
 * @see SKYIView 视图
 */
public interface SKYIView {

	int	STATE_CONTENT		= 1;

	int	STATE_LOADING		= 2;

	int	STATE_EMPTY			= 3;

	int	STATE_BIZ_ERROR		= 4;

	int	STATE_HTTP_ERROR	= 5;

	void showContent();

	void showEmpty();

	void showBizError();

	void showLoading();

	void showHttpError();

	int showState();

	<T extends SKYRVAdapter> T getAdapter();
}
