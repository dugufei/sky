package sk.livedata;

import android.arch.core.util.Function;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author sky
 * @version 1.0 on 2018-08-02 下午9:06
 * @see SKTransformations
 */
public class SKTransformations {

	@MainThread public static <X, Y> SKData<Y> map(@NonNull SKParentLD<X> source, @NonNull final Function<X, Y> func) {
		final SKData<Y> result = new SKData<>();
		result.addSource(source, new SKObserver<X>() {

			@Override public void onChanged(@Nullable X x) {
				result.setValue(func.apply(x));
			}

			@Override public void onAction(int id, Object... args) {
				result.action(id, args);
			}

			@Override public void onAction(SKViewState state) {
				result.viewState(state);
			}

			@Override public void onAction(SKNetworkState networkState) {
				result.networkState(networkState);
			}
		});
		return result;
	}

	@MainThread public static <X, Y> SKData<Y> switchMap(@NonNull SKData<X> trigger, @NonNull final Function<X, SKData<Y>> func) {
		final SKData<Y> result = new SKData<>();
		result.addSource(trigger, new Observer<X>() {

			SKParentLD<Y> mSource;

			@Override public void onChanged(@Nullable X x) {
				SKData<Y> newLiveData = func.apply(x);
				if (mSource == newLiveData) {
					return;
				}
				if (mSource != null) {
					result.removeSource(mSource);
				}
				mSource = newLiveData;
				if (mSource != null) {
					result.addSource(mSource, new SKObserver<Y>() {

						@Override public void onAction(int id, Object... args) {
							result.action(id, args);
						}

						@Override public void onAction(SKViewState state) {
							result.viewState(state);
						}

						@Override public void onAction(SKNetworkState networkState) {
							result.networkState(networkState);
						}

						@Override public void onChanged(@Nullable Y y) {
							result.setValue(y);
						}
					});
				}
			}
		});
		return result;
	}
}