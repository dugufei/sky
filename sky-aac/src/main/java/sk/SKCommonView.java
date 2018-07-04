package sk;

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

	SKCommonView NULL = new SKCommonView() {

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
