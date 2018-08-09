package sk.livedata.list.factory;

import android.arch.paging.DataSource;
import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import sk.SKHelper;
import sk.SKHolder;
import sk.livedata.list.SKSourceState;

import static sk.livedata.list.SKSourceState.AFTER;
import static sk.livedata.list.SKSourceState.BEFORE;
import static sk.livedata.list.SKSourceState.INIT;

/**
 * @author sky
 * @version 1.0 on 2018-08-06 下午1:53
 * @see SKItemFactory
 */
public abstract class SKItemFactory<Key, Value> extends DataSource.Factory<Key, Value> {

	@Override public DataSource<Key, Value> create() {
		SKItemDataSource itemKeyedDataSource = new SKItemDataSource<Key, Value>() {

			@Override public void retry() {
				SKHelper.executors().network().execute(runnable);
			}

			Runnable runnable;

			@Override public void loadInitial(@NonNull final LoadInitialParams<Key> params, @NonNull final LoadInitialCallback<Value> callback) {
				try {
					init(params, callback);
				} catch (Throwable throwable) {
					if (SKHelper.isLogOpen()) {
						throwable.printStackTrace();
					}
					if (runnable == null) {

						runnable = new Runnable() {

							@Override public void run() {
								loadInitial(params, callback);
							}
						};
					}
					error(INIT);
				}
			}

			@Override public void loadAfter(@NonNull final LoadParams<Key> params, @NonNull final LoadCallback<Value> callback) {
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

			@Override public void loadBefore(@NonNull final LoadParams<Key> params, @NonNull final LoadCallback<Value> callback) {
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

			@NonNull @Override public Key getKey(@NonNull Value item) {
				Key key = null;
				try {
					key = key(item);
				} catch (Throwable throwable) {
					if (SKHelper.isLogOpen()) {
						throwable.printStackTrace();
					}
				}

				return key;
			}
		};

		return itemKeyedDataSource;
	}

	public abstract void init(@NonNull ItemKeyedDataSource.LoadInitialParams<Key> params, @NonNull ItemKeyedDataSource.LoadInitialCallback<Value> callback);

	public abstract void before(@NonNull ItemKeyedDataSource.LoadParams<Key> params, @NonNull ItemKeyedDataSource.LoadCallback<Value> callback);

	public abstract void after(@NonNull ItemKeyedDataSource.LoadParams<Key> params, @NonNull ItemKeyedDataSource.LoadCallback<Value> callback);

	public abstract void error(@NonNull SKSourceState skSourceState);

	public abstract Key key(@NonNull Value item);

}
