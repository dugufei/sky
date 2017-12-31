package sky.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author sky
 * @version 版本
 */
class SKYSingleWorkExecutorServiece extends ThreadPoolExecutor {

	SKYSingleWorkExecutorServiece() {
		super(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}
}
