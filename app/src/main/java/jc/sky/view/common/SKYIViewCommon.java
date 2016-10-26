package jc.sky.view.common;

/**
 * @author sky
 * @version 版本
 */
public interface SKYIViewCommon {

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
	 * 网络业务错误
	 *
	 * @return 返回值
	 */
	int layoutBizError();

	/**
	 * 网络错误
	 * 
	 * @return 返回值
	 */
	int layoutHttpError();

	SKYIViewCommon SKYI_VIEW_COMMON = new SKYIViewCommon() {

		@Override public int layoutLoading() {
			return 0;
		}

		@Override public int layoutEmpty() {
			return 0;
		}

		@Override public int layoutBizError() {
			return 0;
		}

		@Override public int layoutHttpError() {
			return 0;
		}
	};
}
