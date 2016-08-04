package jc.sky.modules.threadpool;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by sky on 15/3/20.
 */
public class SKYWorkExecutorService extends ThreadPoolExecutor {

	SKYWorkExecutorService() {
		super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	}
}
