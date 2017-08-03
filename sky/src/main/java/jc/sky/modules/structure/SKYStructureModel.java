package jc.sky.modules.structure;

import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYAppUtil;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.core.Impl;
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

	public SKYStructureModel(Object view, Bundle bundle) {
		// 唯一标记
		key = view.hashCode();
		// 视图
		this.view = view;
		// 数据
		this.bundle = bundle;
		// 业务初始化
		service = SKYAppUtil.getClassGenricType(view.getClass(), 0);
		SKYCheckUtils.checkNotNull(service, "获取不到泛型");
		SKYCheckUtils.validateServiceInterface(service);

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
		this.impl = null;
		SKYProxy.clearProxy();
		SKYProxy = null;
		supper.clear();
		supper = null;
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
