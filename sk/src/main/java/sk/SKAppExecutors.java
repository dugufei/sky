package sk;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import sk.plugins.SKErrorInterceptor;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 下午3:29
 * @see SKAppExecutors 线程池管理
 */
public final class SKAppExecutors {

	private final Executor					diskIO;		// io操作

	private final Executor					network;	// 网络操作

	private final ScheduledExecutorService	scheduled;	// 定时任务

	private final Executor					work;		// 工作线程

	private final Executor					mainThread;

	public SKAppExecutors() {
		diskIO = Executors.newSingleThreadExecutor();
		network = Executors.newFixedThreadPool(3);
		scheduled = Executors.newScheduledThreadPool(3);
		work = Executors.newCachedThreadPool();
		mainThread = new MainThreadExecutor();
	}

	public Executor diskIO() {
		return diskIO;
	}

	public void diskIO(final Runnable runnable) {
		diskIO.execute(new Runnable() {

			@Override public void run() {
				try {
					runnable.run();
				} catch (Throwable throwable) {
					if (SKHelper.isLogOpen()) {
						throwable.printStackTrace();
					}
					SKErrorEnum skErrorEnum = SKErrorEnum.LOGIC;
					for (SKErrorInterceptor skErrorInterceptor : SKHelper.interceptor().skErrorInterceptors()) {
						if (throwable.getCause() instanceof SKHttpException) {
							skErrorEnum = SKErrorEnum.HTTP;
						}
						skErrorInterceptor.interceptorError(null, null, null, 0, skErrorEnum);
					}
				}
			}
		});
	}

	public Executor network() {
		return network;
	}

	public void network(final Runnable runnable) {
		diskIO.execute(new Runnable() {

			@Override public void run() {
				try {
					runnable.run();
				} catch (Throwable throwable) {
					if (SKHelper.isLogOpen()) {
						throwable.printStackTrace();
					}
					SKErrorEnum skErrorEnum = SKErrorEnum.LOGIC;
					for (SKErrorInterceptor skErrorInterceptor : SKHelper.interceptor().skErrorInterceptors()) {
						if (throwable.getCause() instanceof SKHttpException) {
							skErrorEnum = SKErrorEnum.HTTP;
						}
						skErrorInterceptor.interceptorError(null, null, null, 0, skErrorEnum);
					}
				}
			}
		});
	}

	public Executor work(){
		return work;
	}

	public void work(final  Runnable runnable){
		work.execute(new Runnable() {

			@Override public void run() {
				try {
					runnable.run();
				} catch (Throwable throwable) {
					if (SKHelper.isLogOpen()) {
						throwable.printStackTrace();
					}
					SKErrorEnum skErrorEnum = SKErrorEnum.LOGIC;
					for (SKErrorInterceptor skErrorInterceptor : SKHelper.interceptor().skErrorInterceptors()) {
						if (throwable.getCause() instanceof SKHttpException) {
							skErrorEnum = SKErrorEnum.HTTP;
						}
						skErrorInterceptor.interceptorError(null, null, null, 0, skErrorEnum);
					}
				}
			}
		});
	}

	public Executor mainThread() {
		return mainThread;
	}

	public void mainThread(final Runnable runnable) {
		diskIO.execute(new Runnable() {

			@Override public void run() {
				try {
					runnable.run();
				} catch (Throwable throwable) {
					if (SKHelper.isLogOpen()) {
						throwable.printStackTrace();
					}
					SKErrorEnum skErrorEnum = SKErrorEnum.LOGIC;
					for (SKErrorInterceptor skErrorInterceptor : SKHelper.interceptor().skErrorInterceptors()) {
						if (throwable.getCause() instanceof SKHttpException) {
							skErrorEnum = SKErrorEnum.HTTP;
						}
						skErrorInterceptor.interceptorError(null, null, null, 0, skErrorEnum);
					}
				}
			}
		});
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
