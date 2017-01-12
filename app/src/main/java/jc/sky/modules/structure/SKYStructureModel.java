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

		Object impl = getImplClass(service);
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

	/**
	 * 清空
	 */
	public void clearAll() {
		this.bundle = null;
		this.view = null;
		service = null;
		SKYProxy.clearProxy();
		SKYProxy = null;
		supper.clear();
		supper = null;
	}

	/**
	 * 获取实现类
	 *
	 * @param service
	 * @param <D>
	 * @return
	 */
	private <D> Object getImplClass(@NotNull Class<D> service) {
		validateServiceClass(service);
		try {
			// 获取注解
			Impl impl = service.getAnnotation(Impl.class);
			SKYCheckUtils.checkNotNull(impl, "该接口没有指定实现类～");
			/** 加载类 **/
			Class clazz = Class.forName(impl.value().getName());
			Constructor c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			SKYCheckUtils.checkNotNull(clazz, "业务类为空～");
			/** 创建类 **/
			return c.newInstance();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，没有找到业务类！" + e.getMessage());
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，实例化异常！" + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，访问权限异常！" + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，没有找到构造方法！" + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，反射异常！" + e.getMessage());
		}
	}

	/**
	 * 验证类 - 判断是否是一个接口
	 *
	 * @param service
	 * @param <T>
	 */
	private <T> void validateServiceClass(Class<T> service) {
		if (service == null || !service.isInterface()) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(service);
			stringBuilder.append("，该类不是接口！");
			throw new IllegalArgumentException(stringBuilder.toString());
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
		return supper.search(clazz) != -1;
	}
}
