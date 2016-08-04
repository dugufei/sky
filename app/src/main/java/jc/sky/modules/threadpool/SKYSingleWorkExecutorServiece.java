package jc.sky.modules.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by sky on 15/2/20.
 */
class SKYSingleWorkExecutorServiece extends ThreadPoolExecutor {

	SKYSingleWorkExecutorServiece() {
		super(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}
}
