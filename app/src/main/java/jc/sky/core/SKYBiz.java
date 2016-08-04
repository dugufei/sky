package jc.sky.core;

import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYAppUtil;
import jc.sky.display.SKYIDisplay;
import jc.sky.modules.structure.SKYStructureModel;

/**
 * Created by sky on 15/2/1.
 */
public abstract class SKYBiz<U> implements SKYIBiz {

	private U					u;

	private Class				ui;

	private SKYStructureModel j2WStructureModel;

	protected <H> H http(Class<H> hClass) {
		if (j2WStructureModel == null || j2WStructureModel.getView() == null) {
			return SKYHelper.http(hClass);
		}
		return j2WStructureModel.http(hClass);
	}

	protected <I> I impl(Class<I> inter) {
		if (j2WStructureModel == null || j2WStructureModel.getView() == null) {
			return SKYHelper.impl(inter);
		}
		return j2WStructureModel.impl(inter);
	}

	protected <D extends SKYIDisplay> D display(Class<D> eClass) {
		if (j2WStructureModel == null || j2WStructureModel.getView() == null) {
			return SKYHelper.display(eClass);
		}
		return j2WStructureModel.display(eClass);
	}

	public <C extends SKYIBiz> C biz(Class<C> service) {
		if (j2WStructureModel != null && j2WStructureModel.isSupterClass(service)) {
			if (j2WStructureModel.getJ2WProxy() == null || j2WStructureModel.getJ2WProxy().proxy == null) {
				return SKYHelper.structureHelper().createNullService(service);
			}
			return (C) j2WStructureModel.getJ2WProxy().proxy;
		} else if (j2WStructureModel != null && service.equals(j2WStructureModel.getService())) {
			if (j2WStructureModel.getJ2WProxy() == null || j2WStructureModel.getJ2WProxy().proxy == null) {
				return SKYHelper.structureHelper().createNullService(service);
			}
			return (C) j2WStructureModel.getJ2WProxy().proxy;
		} else {
			return SKYHelper.biz(service);
		}
	}

	/**
	 * View层 回调引用
	 *
	 * @return
	 */
	protected U ui() {
		if (u == null) {
			Class ui = SKYAppUtil.getSuperClassGenricType(this.getClass(), 0);
			return (U) SKYHelper.structureHelper().createNullService(ui);
		}
		return u;
	}

	/**
	 * View层 回调
	 * 
	 * @param clazz
	 * @param <V>
	 * @return
	 */
	protected <V> V ui(Class<V> clazz) {
		if (clazz.equals(ui)) {
			return (V) ui();
		} else {
			return SKYHelper.structureHelper().createNullService(clazz);
		}
	}

	/**
	 * View层 是否存在
	 * 
	 * @return
	 */
	public boolean isUI() {
		return u != null;
	}

	@Override public void initUI(SKYStructureModel j2WStructureModel) {
		this.j2WStructureModel = j2WStructureModel;
		ui = SKYAppUtil.getSuperClassGenricType(this.getClass(), 0);
		u = (U) SKYHelper.structureHelper().createMainLooper(ui, j2WStructureModel.getView());
	}

	@Override public void detach() {
		u = null;
		ui = null;
		j2WStructureModel = null;
	}
}
