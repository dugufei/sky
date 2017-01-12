package jc.sky.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import jc.sky.SKYHelper;
import jc.sky.core.SKYIBiz;
import jc.sky.display.SKYIDisplay;
import jc.sky.modules.structure.SKYStructureModel;

/**
 * @author sky
 * @version 版本
 */
public abstract class SKYService<B extends SKYIBiz> extends Service {

	SKYStructureModel SKYStructureModel;

	/**
	 * @param intent
	 *            参数
	 * @return 返回值
	 */
	@Nullable @Override public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 初始化数据
	 *
	 * 数据
	 */
	protected abstract void initData();

	/**
	 * 运行
	 * 
	 * @param intent
	 *            参数
	 * @param flags
	 *            参数
	 * @param startId
	 *            参数
	 */
	protected abstract void running(Intent intent, int flags, int startId);
	

	@Override public void onCreate() {
		super.onCreate();
		SKYStructureModel = new SKYStructureModel(this, null);

		SKYHelper.structureHelper().attach(SKYStructureModel);
		/** 初始化 **/
		initData();
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

	/**
	 * @param intent
	 *            参数
	 * @param flags
	 *            参数
	 * @param startId
	 *            参数
	 * @return 返回值
	 */
	@Override public int onStartCommand(Intent intent, int flags, int startId) {
		running(intent, flags, startId);
		return START_NOT_STICKY;
	}

}