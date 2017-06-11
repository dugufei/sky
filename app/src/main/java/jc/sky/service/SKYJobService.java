package jc.sky.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;

import jc.sky.SKYHelper;
import jc.sky.core.SKYIBiz;
import jc.sky.display.SKYIDisplay;
import jc.sky.modules.structure.SKYStructureModel;

/**
 * @author sky
 * @version 1.0 on 2017-06-10 下午9:59
 * @see SKYJobService
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public abstract class SKYJobService<B extends SKYIBiz> extends JobService {

	private SKYStructureModel SKYStructureModel;

	/**
	 * 达到条件执行
	 * 
	 *  true 完成任务 不会执行过长的任务 false 需要自行处理，处理后 调用jobFinished 完成
	 */
	// @Override public boolean onStartJob(JobParameters params) {
	// return false;
	// }

	// @Override public boolean onStopJob(JobParameters params) {
	// return false;
	// }

	@Override public void onCreate() {
		super.onCreate();
		SKYStructureModel = new SKYStructureModel(this, null);
		SKYHelper.structureHelper().attach(SKYStructureModel);
	}

	/**
	 * @param eClass
	 *            参数
	 * @param <D>
	 *            参数
	 * @return 返回值
	 */
	public <D extends SKYIDisplay> D display(Class<D> eClass) {
		return SKYHelper.display(eClass);
	}

	/**
	 * @return 返回值
	 */
	public B biz() {
		return (B) SKYStructureModel.getSKYProxy().proxy;
	}

	/**
	 * @param service
	 *            参数
	 * @param <C>
	 *            参数
	 * @return 返回值
	 */
	public <C extends SKYIBiz> C biz(Class<C> service) {
		if (SKYStructureModel.getService().equals(service)) {
			return (C) SKYStructureModel.getSKYProxy().proxy;
		}
		return SKYHelper.structureHelper().biz(service);
	}

	@Override public void onDestroy() {
		super.onDestroy();
		SKYHelper.structureHelper().detach(SKYStructureModel);
	}

}
