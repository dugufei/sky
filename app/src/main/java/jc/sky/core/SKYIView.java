package jc.sky.core;

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
}
