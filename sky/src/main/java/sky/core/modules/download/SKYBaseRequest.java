package sky.core.modules.download;


import sky.core.L;

/**
 * @author sky
 * @version 版本
 */
public class SKYBaseRequest implements Comparable<SKYBaseRequest> {

	/**
	 * 请求优先级
	 */
	public enum Priority {
		/**
		 * 最低
		 */
		LOW,
		/**
		 * 正常
		 */
		NORMAL,
		/**
		 * 高
		 */
		HIGH,
		/**
		 * 及时
		 */
		IMMEDIATE
	}

	/**
	 * 状态
	 */
	private int				mDownloadState;

	/**
	 * 请求ID
	 */
	int						mRequestId;

	/**
	 * 请求TAG
	 */
	private String			mReuestTag;

	/**
	 * 是否取消
	 */
	boolean					mCanceled	= false;

	/**
	 * 请求队列
	 */
	SKYDownloadRequestQueue mRequestQueue;

	/**
	 * 获取请求Id
	 *
	 * @return 请求ID
	 */
	public final int getRequestId() {
		return mRequestId;
	}

	/**
	 * 设置请求Id
	 *
	 * @param requestId
	 *            请求Id
	 */
	final void setRequestId(int requestId) {
		mRequestId = requestId;
	}

	/**
	 * 获取请求TAG
	 *
	 * @return 返回值
	 */
	public final String getRequestTag() {
		return mReuestTag;
	}

	/**
	 * 设置请求TAG
	 *
	 * @param requestTag
	 */
	final void setRequestTag(String requestTag) {
		this.mReuestTag = requestTag;
	}

	/**
	 * 初始化优先级 - 默认正常
	 */
	protected Priority mPriority = Priority.NORMAL;

	/**
	 * 获取优先级
	 *
	 * @return 优先级
	 */
	public Priority getPriority() {
		return mPriority;
	}

	/**
	 * 设置队列
	 *
	 * @param downloadQueue
	 *            队列
	 */
	void setDownloadRequestQueue(SKYDownloadRequestQueue downloadQueue) {
		mRequestQueue = downloadQueue;
	}

	/**
	 *
	 * @return 返回值
	 */
	public boolean isCanceled() {
		return mCanceled;
	}

	/**
	 * 取消请求
	 */
	public void cancel() {
		mCanceled = true;
	}

	/**
	 * 获取状态
	 *
	 * @return 返回值
	 */
	int getDownloadState() {
		return mDownloadState;
	}

	/**
	 * 设置状态
	 *
	 * @param mDownloadState
	 */
	void setDownloadState(int mDownloadState) {
		switch (mDownloadState) {
			case SKYIDownloadMagnager.STATUS_PENDING:
				L.tag("SKYIDownloadMagnager");
				L.d("目前正在等待状态");
				break;
			case SKYIDownloadMagnager.STATUS_STARTED:
				L.tag("SKYIDownloadMagnager");
				L.d("开始状态");
				break;
			case SKYIDownloadMagnager.STATUS_CONNECTING:
				L.tag("SKYIDownloadMagnager");
				L.d("联网状态");
				break;
			case SKYIDownloadMagnager.STATUS_RUNNING:
				L.tag("SKYIDownloadMagnager");
				L.d("运行状态");
				break;
			case SKYIDownloadMagnager.STATUS_SUCCESSFUL:
				L.tag("SKYIDownloadMagnager");
				L.d("完成状态");
				break;
			case SKYIDownloadMagnager.STATUS_FAILED:
				L.tag("SKYIDownloadMagnager");
				L.d("失败状态");
				break;
			case SKYIDownloadMagnager.STATUS_NOT_FOUND:
				L.tag("SKYIDownloadMagnager");
				L.d("失败状态 - 没有找到");
				break;
		}
		this.mDownloadState = mDownloadState;
	}

	/**
	 * 从队列中删除
	 */
	public void finish() {
		mRequestQueue.finish(this);
	}

	/**
	 * 排序
	 *
	 * @param another
	 *            参数
	 * @return 返回值
	 */
	@Override public int compareTo(SKYBaseRequest another) {
		Priority left = this.getPriority();
		Priority right = another.getPriority();
		return left == right ? this.mRequestId - another.mRequestId : right.ordinal() - left.ordinal();
	}
}
