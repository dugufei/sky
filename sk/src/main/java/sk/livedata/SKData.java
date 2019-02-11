package sk.livedata;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 下午12:16
 * @see SKData 数据结构
 */
public class SKData<T> extends SKParentLD<T> {

	SKRetryInterface skRetryInterface;

	public void retry() {
		if (skRetryInterface == null) {
			return;
		}
		skRetryInterface.retry();
	}

	@Override public void postValue(T value) {
		super.postValue(value);
	}

	@Override public void setValue(T value) {
		super.setValue(value);
	}

	@Override public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
		super.observe(owner, observer);
	}

	@Override public void observeForever(@NonNull Observer<? super T> observer) {
		super.observeForever(observer);
	}

	@Override public void layoutLoading() {
		viewState(SKViewState.LOAD);
	}

	@Override public void layoutContent() {
		viewState(SKViewState.CONTENT);
	}

	@Override public void layoutEmpty() {
		viewState(SKViewState.EMPTY);
	}

	@Override public void layoutError() {
		viewState(SKViewState.ERROR);
	}

	@Override public void loading() {
		viewState(SKViewState.LOADING);
	}

	@Override public void closeLoading() {
		viewState(SKViewState.CLOSE_LOADING);
	}

	@Override public void netWorkRunning() {
		networkState(SKNetworkState.RUNNING);
	}

	@Override public void netWorkSuccess() {
		networkState(SKNetworkState.SUCCESS);
	}

	@Override public void netWorkFailed(String message) {
		SKNetworkState skNetworkState = SKNetworkState.FAILED;
		skNetworkState.setMessage(message);
		networkState(skNetworkState);
	}

}
