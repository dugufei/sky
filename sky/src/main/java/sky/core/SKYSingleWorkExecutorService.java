package sky.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sky
 * @version 版本
 */
class SKYSingleWorkExecutorService extends ThreadPoolExecutor {

	SKYSingleWorkExecutorService() {
		super(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}
}
