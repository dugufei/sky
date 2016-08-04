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

	SKYStructureModel j2WStructureModel;

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
		j2WStructureModel = new SKYStructureModel(this);
		SKYHelper.structureHelper().attach(j2WStructureModel);
		/** 初始化 **/
		initData();
	}

	public <D extends SKYIDisplay> D display(Class<D> eClass) {
		return j2WStructureModel.display(eClass);
	}

	public B biz() {
		return (B) j2WStructureModel.getJ2WProxy().proxy;
	}

	public <C extends SKYIBiz> C biz(Class<C> service) {
		if (j2WStructureModel.getService().equals(service)) {
			return (C) j2WStructureModel.getJ2WProxy().proxy;
		}
		return SKYHelper.structureHelper().biz(service);
	}

	@Override public void onDestroy() {
		super.onDestroy();
		SKYHelper.structureHelper().detach(j2WStructureModel);
	}

	@Override public int onStartCommand(Intent intent, int flags, int startId) {
		running(intent, flags, startId);
		return START_NOT_STICKY;
	}

}