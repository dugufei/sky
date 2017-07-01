package jc.sky.modules.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sky
 * @version 版本
 */
class SKYHttpExecutorService extends ThreadPoolExecutor {

	private static final int	DEFAULT_THREAD_COUNT	= 5;

	SKYHttpExecutorService() {
		super(DEFAULT_THREAD_COUNT, DEFAULT_THREAD_COUNT, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}
}
