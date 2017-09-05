package jc.sky.modules.structure;

import android.os.Bundle;

import java.util.Stack;

import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYAppUtil;
import jc.sky.core.SKYBiz;
import jc.sky.modules.methodProxy.SKYProxy;

/**
 * @author sky
 * @version 版本
 */
public class SKYStructureModel {

	final int				key;

	SKYProxy				SKYProxy;

	private Object			view;

	private Bundle			bundle;

	private Class			service;

	private Stack<Class>	supper;

	private Object			impl;

	private Object			biz;

	public SKYStructureModel(Object view, Bundle bundle) {
		// 唯一标记
		key = view.hashCode();
		// 视图
		this.view = view;
		// 数据
		this.bundle = bundle;
		// 业务初始化
		service = SKYAppUtil.getClassGenricType(view.getClass(), 0);
		if (service == null) {
			return;
		}
		if (!service.isInterface()) {

			impl = SKYAppUtil.getImplClassNotInf(service);

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
			SKYProxy = SKYHelper.methodsProxy().createNotInf(service,impl);

		} else {
			impl = SKYAppUtil.getImplClass(service);

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
			SKYProxy = SKYHelper.methodsProxy().create(service, impl);
		}
	}

	public void initBizBundle() {
		if (impl instanceof SKYBiz) {
			((SKYBiz) impl).initBundle();
		}
	}

	/**
	 * 清空
	 */
	public void clearAll() {
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
		if (SKYProxy != null) {
			SKYProxy.clearProxy();
			SKYProxy = null;
		}
		if (supper != null) {
			supper.clear();
			supper = null;
		}
	}

	public int getKey() {
		return key;
	}

	public Bundle getBundle() {
		return bundle;
	}

	public Object getView() {
		return view;
	}

	public Class getService() {
		return service;
	}

	public SKYProxy getSKYProxy() {
		return SKYProxy;
	}

	public boolean isSupterClass(Class clazz) {
		if (supper == null || clazz == null) {
			return false;
		}
		return supper.search(clazz) != -1;
	}
}
