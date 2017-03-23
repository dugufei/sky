package jc.sky.core;

import jc.sky.view.adapter.recycleview.SKYRVAdapter;

/**
 * @author sky
 * @version 1.0 on 2016-11-30 下午3:11
 * @see SKYIView 视图
 */
public interface SKYIView {

	void showContent();

	void showEmpty();

	void showBizError();

	void showLoading();

	void showHttpError();

    <T extends SKYRVAdapter> T getAdapter();
}
