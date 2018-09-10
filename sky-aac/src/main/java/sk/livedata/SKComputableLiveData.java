package sk.livedata;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import sk.SKHelper;

/**
 * @author sky
 * @version 1.0 on 2018-08-03 下午9:50
 * @see SKComputableLiveData
 */
public abstract class SKComputableLiveData<T> {

	private final Executor	mExecutor;

	private final SKData<T>	mLiveData;

	private AtomicBoolean	mInvalid	= new AtomicBoolean(true);

	private AtomicBoolean	mComputing	= new AtomicBoolean(false);

	/**
	 *
	 * Creates a computable live data that computes values on the specified
	 * executor.
	 *
	 * @param executor
	 *            Executor that is used to compute new LiveData values.
	 * @param skData
	 */
	@SuppressWarnings("WeakerAccess") public SKComputableLiveData(@NonNull Executor executor, @NonNull SKData<T> skData) {
		mExecutor = executor;
		this.mLiveData = skData;
		this.mLiveData.setOnActiveRunnable(new Runnable() {

			@Override public void run() {
				mExecutor.execute(mRefreshRunnable);
			}
		});
	}

	/**
	 * Returns the LiveData managed by this class.
	 *
	 * @return A LiveData that is controlled by SKComputableLiveData.
	 */
	@SuppressWarnings("WeakerAccess") @NonNull public SKData<T> getLiveData() {
		return mLiveData;
	}

	@VisibleForTesting final Runnable	mRefreshRunnable		= new Runnable() {

																	@WorkerThread @Override public void run() {
																		boolean computed;
																		do {
																			computed = false;
																			// compute can happen only in 1 thread but no reason to lock others.
																			if (mComputing.compareAndSet(false, true)) {
																				// as long as it is invalid, keep computing.
																				try {
																					T value = null;
																					while (mInvalid.compareAndSet(true, false)) {
																						computed = true;
																						value = compute();
																					}
																					if (computed) {
																						mLiveData.postValue(value);
																					}
																				} finally {
																					// release compute lock
																					mComputing.set(false);
																				}
																			}
																			// check invalid after releasing compute lock to avoid the following scenario.
																			// Thread A runs compute()
																			// Thread A checks invalid, it is false
																			// Main thread sets invalid to true
																			// Thread B runs, fails to acquire compute lock and skips
																			// Thread A releases compute lock
																			// We've left invalid in set state. The check below recovers.
																		} while (computed && mInvalid.get());
																	}
																};

	// invalidation check always happens on the main thread
	@VisibleForTesting final Runnable	mInvalidationRunnable	= new Runnable() {

																	@MainThread @Override public void run() {
																		boolean isActive = mLiveData.hasActiveObservers();
																		if (mInvalid.compareAndSet(false, true)) {
																			if (isActive) {
																				mExecutor.execute(mRefreshRunnable);
																			}
																		}
																	}
																};

	/**
	 * Invalidates the LiveData.
	 * <p>
	 * When there are active observers, this will trigger a call to
	 * {@link #compute()}.
	 */
	public void invalidate() {
		SKHelper.executors().mainThread().execute(mInvalidationRunnable);
	}

	@SuppressWarnings("WeakerAccess") @WorkerThread protected abstract T compute();
}
