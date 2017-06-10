package jc.sky.modules.job;

import android.app.job.JobInfo;

/**
 * @author sky
 * @version 1.0 on 2017-06-10 下午10:41
 * @see SKYIJobService
 */
public interface SKYIJobService {

	/**
	 * 开始调度
	 * 
	 * @param jobInfo
	 *            参数 setPersisted(true) 时 manifest注册
	 *            android.permission.RECEIVE_BOOT_COMPLETED
	 */
	void schedule(JobInfo jobInfo);

	/**
	 * 取消指定任务
	 * 
	 * @param id
	 *            参数
	 */
	void cancel(int id);

	/**
	 * 取消所有任务
	 */
	void cancelAll();

	/**
	 * 获取job
	 *
	 * @param id
	 *            工作标识
	 * @param clazz
	 *            类文件
	 * @return 返回值
	 */
	JobInfo.Builder builder(int id, Class clazz);
}
