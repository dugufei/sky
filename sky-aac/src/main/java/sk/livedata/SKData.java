package sk.livedata;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 下午12:16
 * @see SKData 数据结构
 */
public class SKData<T> extends SKParentLD<T> {

	@Override public void postValue(T value) {
		super.postValue(value);
	}

	@Override public void setValue(T value) {
		super.setValue(value);
	}

	@Override public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
		super.observe(owner, observer);
	}

	@Override public void observeForever(@NonNull Observer<T> observer) {
		super.observeForever(observer);
	}

	@Override public void showLoading(Object... args) {
		action(3000, args);
	}

	@Override public void showContent(Object... args) {
		action(1000, args);

	}

	@Override public void showEmpty(Object... args) {
		action(2000, args);

	}

	@Override public void showError(Object... args) {
		action(4000, args);
	}

	@Override public void loading() {
		action(5000, null);

	}

	@Override public void closeloading() {
		action(6000, null);
	}

}
