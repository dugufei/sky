package sk.livedata.list.factory;

import android.arch.paging.DataSource;
import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

import sk.SKHelper;
import sk.livedata.list.SKSourceState;

import static sk.livedata.list.SKSourceState.AFTER;
import static sk.livedata.list.SKSourceState.INIT;

/**
 * @author sky
 * @version 1.0 on 2018-08-06 上午11:37
 * @see SKPositionFactory
 */
public abstract class SKPositionFactory<Value> extends DataSource.Factory<Integer, Value> {

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

}
