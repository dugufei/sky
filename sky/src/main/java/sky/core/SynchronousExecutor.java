package sky.core;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * @author sky
 * @version 版本
 */
public class SynchronousExecutor implements Executor {

	private final Handler				handler				= new Handler(Looper.getMainLooper());

	@Override public void execute(Runnable runnable) {
		handler.post(runnable);
	}
}