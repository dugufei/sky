package jc.sky.modules.threadpool;

import java.util.concurrent.ExecutorService;

import jc.sky.modules.log.L;

/**
 * Created by sky on 15/2/20.调度
 */
public final class SKYThreadPoolManager {

	/** 线程服务-网络线程池 **/
	private SKYHttpExecutorService j2WHttpExecutorService;

	/** 线程服务-并行工作线程池 **/
	private SKYWorkExecutorService j2WWorkExecutorService;

	/** 线程服务-串行工作线程池 **/
	private SKYSingleWorkExecutorServiece j2WSingleWorkExecutorServiece;

	public synchronized ExecutorService getHttpExecutorService() {
		if (j2WHttpExecutorService == null) {
			j2WHttpExecutorService = new SKYHttpExecutorService();
		}
		return j2WHttpExecutorService;
	}

	public synchronized ExecutorService getSingleWorkExecutorService() {
		if (j2WSingleWorkExecutorServiece == null) {
			j2WSingleWorkExecutorServiece = new SKYSingleWorkExecutorServiece();
		}
		return j2WSingleWorkExecutorServiece;
	}

	public synchronized ExecutorService getWorkExecutorService() {
		if (j2WWorkExecutorService == null) {
			j2WWorkExecutorService = new SKYWorkExecutorService();
		}
		return j2WWorkExecutorService;
	}

	public synchronized void finish() {
		L.tag("SKYThreadPoolManager");
		L.i("finish()");
		if (j2WHttpExecutorService != null) {
			L.tag("SKYThreadPoolManager");
			L.i("j2WHttpExecutorService.shutdown()");
			j2WHttpExecutorService.shutdown();
			j2WHttpExecutorService = null;
		}
		if (j2WSingleWorkExecutorServiece != null) {
			L.tag("SKYThreadPoolManager");
			L.i("j2WSingleWorkExecutorServiece.shutdown()");
			j2WSingleWorkExecutorServiece.shutdown();
			j2WSingleWorkExecutorServiece = null;
		}
		if (j2WWorkExecutorService != null) {
			L.tag("SKYThreadPoolManager");
			L.i("j2WWorkExecutorService.shutdown()");
			j2WWorkExecutorService.shutdown();
			j2WWorkExecutorService = null;
		}
	}
}
