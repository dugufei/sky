package jc.sky.modules.threadpool;

import java.util.concurrent.ExecutorService;

import jc.sky.modules.log.L;

/**
 * Created by sky on 15/2/20.调度
 */
public final class SKYThreadPoolManager {

	/** 线程服务-网络线程池 **/
	private SKYHttpExecutorService SKYHttpExecutorService;

	/** 线程服务-并行工作线程池 **/
	private SKYWorkExecutorService SKYWorkExecutorService;

	/** 线程服务-串行工作线程池 **/
	private SKYSingleWorkExecutorServiece SKYSingleWorkExecutorServiece;

	public synchronized ExecutorService getHttpExecutorService() {
		if (SKYHttpExecutorService == null) {
			SKYHttpExecutorService = new SKYHttpExecutorService();
		}
		return SKYHttpExecutorService;
	}

	public synchronized ExecutorService getSingleWorkExecutorService() {
		if (SKYSingleWorkExecutorServiece == null) {
			SKYSingleWorkExecutorServiece = new SKYSingleWorkExecutorServiece();
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
		L.i("finish()");
		if (SKYHttpExecutorService != null) {
			L.tag("SKYThreadPoolManager");
			L.i("SKYHttpExecutorService.shutdown()");
			SKYHttpExecutorService.shutdown();
			SKYHttpExecutorService = null;
		}
		if (SKYSingleWorkExecutorServiece != null) {
			L.tag("SKYThreadPoolManager");
			L.i("SKYSingleWorkExecutorServiece.shutdown()");
			SKYSingleWorkExecutorServiece.shutdown();
			SKYSingleWorkExecutorServiece = null;
		}
		if (SKYWorkExecutorService != null) {
			L.tag("SKYThreadPoolManager");
			L.i("SKYWorkExecutorService.shutdown()");
			SKYWorkExecutorService.shutdown();
			SKYWorkExecutorService = null;
		}
	}
}
