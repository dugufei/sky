package jc.sky.view.common;

/**
 * @创建人 sky
 * @创建时间 15/5/14 下午5:56
 * @类描述 公共视图接口
 */
public interface SKYIViewCommon {

	/**
	 * 进度布局
	 *
	 * @return
	 */
	int layoutLoading();

	/**
	 * 空布局
	 *
	 * @return
	 */
	int layoutEmpty();

	/**
	 * 网络业务错误
	 *
	 * @return
	 */
	int layoutBizError();

	/**
	 * 网络错误
	 * 
	 * @return
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
