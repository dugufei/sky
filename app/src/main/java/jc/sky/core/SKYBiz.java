package jc.sky.core;

import java.util.ArrayList;
import java.util.Vector;

import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYAppUtil;
import jc.sky.display.SKYIDisplay;
import jc.sky.modules.structure.SKYStructureModel;
import retrofit2.Call;

/**
 * Created by sky on 15/2/1.
 */
public abstract class SKYBiz<U> implements SKYIBiz {

	private U					u;

	private Class				ui;

	private SKYStructureModel	SKYStructureModel;

	private Vector<Call>		callVector;

	protected <H> H http(Class<H> hClass) {
		return SKYHelper.http(hClass);
	}

	protected <I> I interfaces(Class<I> inter) {
		return SKYHelper.interfaces(inter);
	}

	protected <D extends SKYIDisplay> D display(Class<D> eClass) {
		return SKYHelper.display(eClass);
	}

	public <C extends SKYIBiz> C biz(Class<C> service) {
		if (SKYStructureModel != null && SKYStructureModel.isSupterClass(service)) {
			if (SKYStructureModel.getSKYProxy() == null || SKYStructureModel.getSKYProxy().proxy == null) {
				return SKYHelper.structureHelper().createNullService(service);
			}
			return (C) SKYStructureModel.getSKYProxy().proxy;
		} else if (SKYStructureModel != null && service.equals(SKYStructureModel.getService())) {
			if (SKYStructureModel.getSKYProxy() == null || SKYStructureModel.getSKYProxy().proxy == null) {
				return SKYHelper.structureHelper().createNullService(service);
			}
			return (C) SKYStructureModel.getSKYProxy().proxy;
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

	protected <D> D httpBody(Call<D> call) {
		callVector.add(call);
		return SKYHelper.httpBody(call);
	}

	/**
	 * View层 是否存在
	 * 
	 * @return
	 */
	public boolean isUI() {
		return u != null;
	}

	@Override public void initUI(SKYStructureModel SKYStructureModel) {
		this.SKYStructureModel = SKYStructureModel;
		ui = SKYAppUtil.getSuperClassGenricType(this.getClass(), 0);
		u = (U) SKYHelper.structureHelper().createMainLooper(ui, SKYStructureModel.getView());
		callVector = new Vector<>();
	}

	@Override public void detach() {
		u = null;
		ui = null;
		SKYStructureModel = null;
		httpCancel();
	}

	/**
	 * 网络取消
	 */
	protected void httpCancel() {
		int count = callVector.size();
		if (count < 1) {
			callVector = null;
			return;
		}
		for (int i = 0; i < count; i++) {
			Call call = callVector.get(i);
			SKYHelper.httpCancel(call);
		}
		callVector.removeAllElements();
		callVector = null;
	}
}
