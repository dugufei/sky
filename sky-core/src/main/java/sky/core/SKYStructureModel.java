package sky.core;

import android.os.Bundle;

import java.util.Stack;

/**
 * @author sky
 * @version 版本
 */
class SKYStructureModel {

	final int				key;

	SKYProxy				skyProxy;

	private Object			view;

	private Bundle			bundle;

	private Class			service;

	private Stack<Class>	supper;

	private Object			impl;

	private Object			biz;

	SKYStructureModel(Object view, Bundle bundle) {
		// 唯一标记
		key = view.hashCode();
		// 视图
		this.view = view;
		// 数据
		this.bundle = bundle;
		// 业务初始化
		service = SKYUtils.getClassGenricType(view.getClass(), 0);
		if (service == null) {
			return;
		}
		if (!service.isInterface()) {

			impl = SKYUtils.getImplClassNotInf(service);

			// 找到父类
			supper = new Stack<>();
			Class tempClass = impl.getClass().getSuperclass();

			if (tempClass != null) {
				while (!tempClass.equals(SKYBiz.class)) {

					if (tempClass.getSuperclass() != null) {
						Class clazz = tempClass.getSuperclass();
						supper.add(clazz);
					}
					tempClass = tempClass.getSuperclass();
				}
			}

			// 如果是业务类
			if (impl instanceof SKYBiz) {
				((SKYBiz) impl).initUI(this);
			}
			skyProxy = SKYHelper.structureHelper().createNotInf(service, impl);

		} else {
			impl = SKYUtils.getImplClass(service);

			// 找到父类
			supper = new Stack<>();
			Class tempClass = impl.getClass().getSuperclass();

			if (tempClass != null) {
				while (!tempClass.equals(SKYBiz.class)) {

					if (tempClass.getInterfaces() != null) {
						Class clazz = tempClass.getInterfaces()[0];
						supper.add(clazz);
					}
					tempClass = tempClass.getSuperclass();
				}
			}
			// 如果是业务类
			if (impl instanceof SKYBiz) {
				((SKYBiz) impl).initUI(this);
			}
			skyProxy = SKYHelper.structureHelper().create(service, impl);
		}
	}

	void initBizBundle() {
		if (impl instanceof SKYBiz) {
			((SKYBiz) impl).initBundle();
		}
	}

	/**
	 * 清空
	 */
	void clearAll() {
		this.bundle = null;
		this.view = null;
		service = null;
		if (this.impl != null) {
			((SKYBiz) impl).detach();
			impl = null;
		}
		if (this.biz != null) {
			((SKYBiz) biz).detach();
			biz = null;
		}
		if (skyProxy != null) {
			skyProxy.clearProxy();
			skyProxy = null;
		}
		if (supper != null) {
			supper.clear();
			supper = null;
		}
	}

	int getKey() {
		return key;
	}

	Bundle getBundle() {
		return bundle;
	}

	Object getView() {
		return view;
	}

	Class getService() {
		return service;
	}

	SKYProxy getSKYProxy() {
		return skyProxy;
	}

	boolean isSupterClass(Class clazz) {
		if (supper == null || clazz == null) {
			return false;
		}
		return supper.search(clazz) != -1;
	}
}
