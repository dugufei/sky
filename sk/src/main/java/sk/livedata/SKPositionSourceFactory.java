package sk.livedata;

import androidx.paging.DataSource;
import androidx.paging.PositionalDataSource;
import androidx.annotation.NonNull;

import sk.SKHelper;

import static sk.livedata.SKSourceState.AFTER;
import static sk.livedata.SKSourceState.INIT;

/**
 * @author sky
 * @version 1.0 on 2018-08-06 上午11:37
 * @see SKPositionSourceFactory
 */
public abstract class SKPositionSourceFactory<Value> extends DataSource.Factory<Integer, Value> {

	final SKData skData;

	public SKPositionSourceFactory() {
		this.skData = new SKData();
	}

	@Override public DataSource<Integer, Value> create() {
		SKPositionDataSource positionalDataSource = new SKPositionDataSource<Value>() {

			Runnable runnable;

			@Override public void loadInitial(@NonNull final LoadInitialParams params, @NonNull final LoadInitialCallback<Value> callback) {
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

			@Override public void loadRange(@NonNull final LoadRangeParams params, @NonNull final LoadRangeCallback<Value> callback) {
				try {
					range(params, callback);
				} catch (Throwable throwable) {
					if (SKHelper.isLogOpen()) {
						throwable.printStackTrace();
					}
					runnable = new Runnable() {

						@Override public void run() {
							loadRange(params, callback);
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

		return positionalDataSource;
	}

	public abstract void init(@NonNull PositionalDataSource.LoadInitialParams params, @NonNull PositionalDataSource.LoadInitialCallback<Value> callback);

	public abstract void range(@NonNull PositionalDataSource.LoadRangeParams params, @NonNull PositionalDataSource.LoadRangeCallback<Value> callback);

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
