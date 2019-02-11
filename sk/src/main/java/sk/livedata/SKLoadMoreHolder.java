package sk.livedata;

import android.view.View;

import sk.SKHolder;

/**
 * @author sky 15/7/17 上午10:58
 * @version 版本 适配器优化holder
 */
public abstract class SKLoadMoreHolder extends SKHolder<SKNetworkState> {

	protected SKLoadMoreCallBack callBack;

	public SKLoadMoreHolder(View itemView) {
		super(itemView);
	}

	public abstract void bindData(SKNetworkState state, int position);

	public void setCallBack(SKLoadMoreCallBack callBack) {
		this.callBack = callBack;
	}
}