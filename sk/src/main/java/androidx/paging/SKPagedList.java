package androidx.paging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.Executor;

/**
 * @author sky
 * @version 1.0 on 2018-08-08 上午9:26
 * @see SKPagedList
 */
public abstract class SKPagedList<T> extends PagedList<T> {


	SKPagedList(@NonNull PagedStorage<T> storage, @NonNull Executor mainThreadExecutor, @NonNull Executor backgroundThreadExecutor, @Nullable BoundaryCallback<T> boundaryCallback,
			@NonNull Config config) {
		super(storage, mainThreadExecutor, backgroundThreadExecutor, boundaryCallback, config);
	}


}
