package sky.core;

import java.util.concurrent.ExecutorService;


/**
 * @author sky
 * @version 版本
 */
final class SKYThreadPoolManager {

	/** 线程服务-网络线程池 **/
	private SKYHttpExecutorService SKYHttpExecutorService;

	/** 线程服务-并行工作线程池 **/
	private SKYWorkExecutorService SKYWorkExecutorService;

	/** 线程服务-串行工作线程池 **/
	private SKYSingleWorkExecutorService SKYSingleWorkExecutorServiece;

	public synchronized ExecutorService getHttpExecutorService() {
		if (SKYHttpExecutorService == null) {
			SKYHttpExecutorService = new SKYHttpExecutorService();
		}
		return SKYHttpExecutorService;
	}

	public synchronized ExecutorService getSingleWorkExecutorService() {
		if (SKYSingleWorkExecutorServiece == null) {
			SKYSingleWorkExecutorServiece = new SKYSingleWorkExecutorService();
		}
		return SKYSingleWorkExecutorServiece;
	}

	public synchronized ExecutorService getWorkExecutorService() {
		if (SKYWorkExecutorService == null) {
			SKYWorkExecutorService = new SKYWorkExecutorService();
		}
		return SKYWorkExecutorService;
	}

	public synchronized void finish() {
		L.tag("SKYThreadPoolManager");
		L.d("finish()");
		if (SKYHttpExecutorService != null) {
			L.tag("SKYThreadPoolManager");
			L.d("SKYHttpExecutorService.shutdown()");
			SKYHttpExecutorService.shutdown();
			SKYHttpExecutorService = null;
		}
		if (SKYSingleWorkExecutorServiece != null) {
			L.tag("SKYThreadPoolManager");
			L.d("SKYSingleWorkExecutorServiece.shutdown()");
			SKYSingleWorkExecutorServiece.shutdown();
			SKYSingleWorkExecutorServiece = null;
		}
		if (SKYWorkExecutorService != null) {
			L.tag("SKYThreadPoolManager");
			L.d("SKYWorkExecutorService.shutdown()");
			SKYWorkExecutorService.shutdown();
			SKYWorkExecutorService = null;
		}
	}
}
