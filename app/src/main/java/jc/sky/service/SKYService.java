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
 * @创建人 sky
 * @创建时间 15/8/15 下午7:39
 * @类描述 服务
 */
public abstract class SKYService<B extends SKYIBiz> extends Service {

	SKYStructureModel SKYStructureModel;

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
	 */
	protected abstract void running(Intent intent, int flags, int startId);

	@Override public void onCreate() {
		super.onCreate();
		SKYStructureModel = new SKYStructureModel(this);
		SKYHelper.structureHelper().attach(SKYStructureModel);
		/** 初始化 **/
		initData();
	}

	public <D extends SKYIDisplay> D display(Class<D> eClass) {
		return SKYHelper.display(eClass);
	}

	public B biz() {
		return (B) SKYStructureModel.getSKYProxy().proxy;
	}

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

	@Override public int onStartCommand(Intent intent, int flags, int startId) {
		running(intent, flags, startId);
		return START_NOT_STICKY;
	}

}