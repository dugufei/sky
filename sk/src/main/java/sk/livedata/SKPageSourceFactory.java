package sk.livedata;

import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import androidx.annotation.NonNull;

import sk.SKHelper;

import static sk.livedata.SKSourceState.AFTER;
import static sk.livedata.SKSourceState.BEFORE;
import static sk.livedata.SKSourceState.INIT;

/**
 * @author sky
 * @version 1.0 on 2018-08-06 上午11:21
 * @see SKPageSourceFactory
 */
public abstract class SKPageSourceFactory<Key, Value> extends DataSource.Factory<Key, Value> {

	final SKData skData;

	public SKPageSourceFactory() {
		this.skData = new SKData();
	}

	@Override public DataSource<Key, Value> create() {
		SKPageDataSource pageKeyedDataSource = new SKPageDataSource<Key, Value>() {

			Runnable runnable;

			@Override public void loadInitial(@NonNull final LoadInitialParams<Key> params, @NonNull final LoadInitialCallback<Key, Value> callback) {
				try {
					init(params, callback);
				} catch (Throwable throwable) {
					if (SKHelper.isLogOpen()) {
						throwable.printStackTrace();
					}
					runnable = new Runnable() {

						@Override public void run() {
							loadInitial(params, callback);
						}
					};
					error(INIT);
				}
			}

			@Override public void loadBefore(@NonNull final LoadParams<Key> params, @NonNull final LoadCallback<Key, Value> callback) {
				try {
					before(params, callback);
				} catch (Throwable throwable) {
					if (SKHelper.isLogOpen()) {
						throwable.printStackTrace();
					}
					runnable = new Runnable() {

						@Override public void run() {
							loadBefore(params, callback);
						}
					};
					error(BEFORE);
				}
			}

			@Override public void loadAfter(@NonNull final LoadParams<Key> params, @NonNull final LoadCallback<Key, Value> callback) {
				try {
					after(params, callback);
				} catch (Throwable throwable) {
					if (SKHelper.isLogOpen()) {
						throwable.printStackTrace();
					}
					runnable = new Runnable() {

						@Override public void run() {
							loadAfter(params, callback);
						}
					};
					error(AFTER);
				}
			}

			@Override public void retry() {
				runnable.run();
				runnable = null;
			}
		};
		return pageKeyedDataSource;
	}

	public abstract void init(@NonNull PageKeyedDataSource.LoadInitialParams<Key> params, @NonNull PageKeyedDataSource.LoadInitialCallback<Key, Value> callback);

	public abstract void before(@NonNull PageKeyedDataSource.LoadParams<Key> params, @NonNull PageKeyedDataSource.LoadCallback<Key, Value> callback);

	public abstract void after(@NonNull PageKeyedDataSource.LoadParams<Key> params, @NonNull PageKeyedDataSource.LoadCallback<Key, Value> callback);

	public abstract void error(@NonNull SKSourceState skSourceState);

	public void layoutLoading() {
		skData.layoutLoading();
	}

	public void layoutContent() {
		skData.layoutContent();
	}

	public void layoutEmpty() {
		skData.layoutEmpty();
	}

	public void layoutError() {
		skData.layoutError();
	}

	public void loading() {
		skData.loading();
	}

	public void closeLoading() {
		skData.closeLoading();
	}

	public void netWorkRunning() {
		skData.netWorkRunning();
	}

	public void netWorkSuccess() {
		skData.netWorkSuccess();
	}

	public void netWorkFailed(String message) {
		skData.netWorkFailed(message);
	}
}
