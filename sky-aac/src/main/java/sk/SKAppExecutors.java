package sk;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 下午3:29
 * @see SKAppExecutors 线程池管理
 */
public final class SKAppExecutors {

	private final Executor					diskIO;		// io操作

	private final Executor					network;	// 网络操作

	private final ScheduledExecutorService	scheduled;	// 定时任务

	private final Executor					mainThread;

	public SKAppExecutors() {
		diskIO = Executors.newSingleThreadExecutor();
		network = Executors.newFixedThreadPool(3);
		scheduled = Executors.newScheduledThreadPool(3);
		mainThread = new MainThreadExecutor();
	}

	public Executor diskIO() {
		return diskIO;
	}

	public Executor network() {
		return network;
	}

	public Executor mainThread() {
		return mainThread;
	}

	public ScheduledExecutorService scheduled() {
		return scheduled;
	}

	/**
	 * 主线程
	 */
	private class MainThreadExecutor implements Executor {

		private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

		@Override public void execute(@NonNull Runnable runnable) {
			mainThreadHandler.post(runnable);
		}
	}
}
