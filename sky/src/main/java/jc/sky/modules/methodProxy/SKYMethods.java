package jc.sky.modules.methodProxy;

import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.core.plugin.DisplayEndInterceptor;
import jc.sky.core.plugin.DisplayStartInterceptor;
import jc.sky.core.plugin.SKYLayoutInterceptor;
import jc.sky.core.plugin.SKYActivityInterceptor;
import jc.sky.core.plugin.ImplEndInterceptor;
import jc.sky.core.plugin.BizEndInterceptor;
import jc.sky.core.plugin.SKYBizErrorInterceptor;
import jc.sky.core.plugin.SKYFragmentInterceptor;
import jc.sky.core.plugin.ImplStartInterceptor;
import jc.sky.core.plugin.BizStartInterceptor;
import jc.sky.core.plugin.SKYHttpErrorInterceptor;
import sky.cglib.proxy.Enhancer;
import sky.cglib.proxy.MethodInterceptor;

/**
 * @author sky
 * @version 版本
 */
public final class SKYMethods {

	final SKYActivityInterceptor				skyActivityInterceptor;

	final SKYLayoutInterceptor					skyLayoutInterceptor;

	final SKYFragmentInterceptor				skyFragmentInterceptor;

	final ArrayList<BizStartInterceptor>		bizStartInterceptor;		// 方法开始拦截器

	final DisplayStartInterceptor				displayStartInterceptor;	// 方法开始拦截器

	final ArrayList<BizEndInterceptor>			bizEndInterceptor;			// 方法结束拦截器

	final DisplayEndInterceptor					displayEndInterceptor;		// 方法结束拦截器

	private ArrayList<ImplStartInterceptor>		implStartInterceptors;		// 方法开始拦截器

	private ArrayList<ImplEndInterceptor>		implEndInterceptors;		// 方法结束拦截器

	final ArrayList<SKYBizErrorInterceptor>		skyErrorInterceptor;		// 方法错误拦截器

	final ArrayList<SKYHttpErrorInterceptor>	skyHttpErrorInterceptors;	// 网络错误拦截器

	public SKYMethods(SKYLayoutInterceptor skyLayoutInterceptor, SKYActivityInterceptor SKYActivityInterceptor, SKYFragmentInterceptor SKYFragmentInterceptor,
					  ArrayList<BizStartInterceptor> bizStartInterceptor, DisplayStartInterceptor displayStartInterceptor, ArrayList<BizEndInterceptor> bizEndInterceptor,
					  DisplayEndInterceptor displayEndInterceptor, ArrayList<ImplStartInterceptor> implStartInterceptors, ArrayList<ImplEndInterceptor> implEndInterceptors,
					  ArrayList<SKYBizErrorInterceptor> SKYErrorInterceptor, ArrayList<SKYHttpErrorInterceptor> skyHttpErrorInterceptors) {
		this.skyLayoutInterceptor = skyLayoutInterceptor;
		this.bizEndInterceptor = bizEndInterceptor;
		this.displayEndInterceptor = displayEndInterceptor;
		this.displayStartInterceptor = displayStartInterceptor;
		this.bizStartInterceptor = bizStartInterceptor;
		this.skyErrorInterceptor = SKYErrorInterceptor;
		this.implStartInterceptors = implStartInterceptors;
		this.implEndInterceptors = implEndInterceptors;
		this.skyActivityInterceptor = SKYActivityInterceptor;
		this.skyFragmentInterceptor = SKYFragmentInterceptor;
		this.skyHttpErrorInterceptors = skyHttpErrorInterceptors;
	}

	/**
	 * 创建 IBIZ
	 *
	 * @param impl
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	public <T> SKYProxy createNotInf(final Class superClazz, Object impl) {
		final SKYProxy SKYProxy = new SKYProxy();
		SKYProxy.impl = impl;
		Enhancer e = new Enhancer(SKYHelper.getInstance());
		e.setSuperclass(superClazz);
		e.setInterceptor(new MethodInterceptor() {

			@Override public Object intercept(String name, Class[] argsType, Object[] args) throws Exception {
				Method method = superClazz.getMethod(name, argsType);

				// 如果有返回值 - 直接执行
				if (!method.getReturnType().equals(void.class)) {
					return method.invoke(SKYProxy.impl, args);
				}

				SKYMethod SKYMethod = loadSKYMethod(SKYProxy, method, superClazz);
				// 开始
				if (!SKYHelper.isLogOpen()) {
					return SKYMethod.invoke(SKYProxy.impl, args);
				}
				enterMethod(method, args);
				long startNanos = System.nanoTime();

				Object result = SKYMethod.invoke(SKYProxy.impl, args);

				long stopNanos = System.nanoTime();
				long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
				exitMethod(method, result, lengthMillis);

				return result;
			}
		});
		SKYProxy.proxy = e.create();
		return SKYProxy;
	}

	/**
	 * 创建 IBIZ
	 *
	 * @param service
	 *            参数
	 * @param impl
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	public <T> SKYProxy create(final Class<T> service, Object impl) {
		SKYCheckUtils.validateServiceInterface(service);

		final SKYProxy SKYProxy = new SKYProxy();
		SKYProxy.impl = impl;
		SKYProxy.proxy = Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new SKYInvocationHandler() {

			@Override public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
				// 如果有返回值 - 直接执行
				if (!method.getReturnType().equals(void.class)) {
					return method.invoke(SKYProxy.impl, args);
				}

				SKYMethod SKYMethod = loadSKYMethod(SKYProxy, method, service);
				// 开始
				if (!SKYHelper.isLogOpen()) {
					return SKYMethod.invoke(SKYProxy.impl, args);
				}
				enterMethod(method, args);
				long startNanos = System.nanoTime();

				Object result = SKYMethod.invoke(SKYProxy.impl, args);

				long stopNanos = System.nanoTime();
				long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
				exitMethod(method, result, lengthMillis);

				return result;
			}
		});

		return SKYProxy;
	}

	/**
	 * 创建 Display
	 *
	 * @param service
	 *            参数
	 * @param impl
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	public <T> SKYProxy createDisplay(final Class<T> service, Object impl) {
		SKYCheckUtils.validateServiceInterface(service);

		final SKYProxy SKYProxy = new SKYProxy();
		SKYProxy.impl = impl;
		SKYProxy.proxy = Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new SKYInvocationHandler() {

			@Override public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
				// 如果有返回值 - 直接执行
				if (!method.getReturnType().equals(void.class)) {
					return method.invoke(SKYProxy.impl, args);

				}
				SKYMethod SKYMethod = loadDisplaySKYMethod(SKYProxy, method, service);
				// 开始
				if (!SKYHelper.isLogOpen()) {
					return SKYMethod.invoke(SKYProxy.impl, args);
				}
				enterMethod(method, args);
				long startNanos = System.nanoTime();

				Object result = SKYMethod.invoke(SKYProxy.impl, args);

				long stopNanos = System.nanoTime();
				long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
				exitMethod(method, result, lengthMillis);

				return result;
			}
		});

		return SKYProxy;
	}

	private void enterMethod(Method method, Object... args) {
		Class<?> cls = method.getDeclaringClass();
		String methodName = method.getName();
		Object[] parameterValues = args;
		StringBuilder builder = new StringBuilder("\u21E2 ");
		builder.append(methodName).append('(');
		if (parameterValues != null) {
			for (int i = 0; i < parameterValues.length; i++) {
				if (i > 0) {
					builder.append(", ");
				}
				builder.append(Strings.toString(parameterValues[i]));
			}
		}

		builder.append(')');

		if (Looper.myLooper() != Looper.getMainLooper()) {
			builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
		}
		Log.v(cls.getSimpleName(), builder.toString());

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			final String section = builder.toString().substring(2);
			Trace.beginSection(section);
		}
	}

	private void exitMethod(Method method, Object result, long lengthMillis) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			Trace.endSection();
		}
		Class<?> cls = method.getDeclaringClass();
		String methodName = method.getName();
		boolean hasReturnType = method.getReturnType() != void.class;

		StringBuilder builder = new StringBuilder("\u21E0 ").append(methodName).append(" [").append(lengthMillis).append("ms]");

		if (hasReturnType) {
			builder.append(" = ");
			builder.append(Strings.toString(result));
		}
		Log.v(cls.getSimpleName(), builder.toString());
	}

	/**
	 * 获取方法唯一标记
	 * 
	 * @param method
	 *            参数
	 * @param classes
	 *            参数
	 * @return 返回值
	 */
	private String getKey(Method method, Class[] classes) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(method.getName());
		stringBuilder.append("(");
		for (Class clazz : classes) {
			stringBuilder.append(clazz.getSimpleName());
			stringBuilder.append(",");
		}
		if (stringBuilder.length() > 2) {
			stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);
		}
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	/**
	 * 创建 IMPL
	 *
	 * @param service
	 *            参数
	 * @param impl
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	public <T> T createImpl(final Class<T> service, final Object impl) {
		SKYCheckUtils.validateServiceInterface(service);
		return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new SKYInvocationHandler() {

			@Override public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
				// 业务拦截器 - 前
				for (ImplStartInterceptor item : SKYHelper.methodsProxy().implStartInterceptors) {
					item.interceptStart(impl.getClass().getName(), service, method, args);
				}
				Object backgroundResult;
				if (!SKYHelper.isLogOpen()) {
					backgroundResult = method.invoke(impl, args);// 执行
				} else {
					enterMethod(method, args);
					long startNanos = System.nanoTime();
					backgroundResult = method.invoke(impl, args);// 执行
					long stopNanos = System.nanoTime();
					long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
					exitMethod(method, backgroundResult, lengthMillis);
				}

				// 业务拦截器 - 后
				for (ImplEndInterceptor item : SKYHelper.methodsProxy().implEndInterceptors) {
					item.interceptEnd(impl.getClass().getName(), service, method, args, backgroundResult);
				}
				return backgroundResult;
			}
		});
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public SKYActivityInterceptor activityInterceptor() {
		return skyActivityInterceptor;
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public SKYLayoutInterceptor layoutInterceptor() {
		return skyLayoutInterceptor;
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public SKYFragmentInterceptor fragmentInterceptor() {
		return skyFragmentInterceptor;
	}

	/**
	 * 加载接口
	 *
	 * @param SKYProxy
	 *            参数
	 * @param method
	 *            参数
	 * @param service
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	private <T> SKYMethod loadSKYMethod(SKYProxy SKYProxy, Method method, Class<T> service) {
		synchronized (SKYProxy.methodCache) {
			String methodKey = getKey(method, method.getParameterTypes());
			SKYMethod SKYMethod = SKYProxy.methodCache.get(methodKey);
			if (SKYMethod == null) {
				SKYMethod = SKYMethod.createBizMethod(method, service);
				SKYProxy.methodCache.put(methodKey, SKYMethod);
			}
			return SKYMethod;
		}
	}

	/**
	 * 加载接口
	 *
	 * @param SKYProxy
	 *            参数
	 * @param method
	 *            参数
	 * @param service
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	private <T> SKYMethod loadDisplaySKYMethod(SKYProxy SKYProxy, Method method, Class<T> service) {
		synchronized (SKYProxy.methodCache) {
			String methodKey = getKey(method, method.getParameterTypes());
			SKYMethod SKYMethod = SKYProxy.methodCache.get(methodKey);
			if (SKYMethod == null) {
				SKYMethod = SKYMethod.createDisplayMethod(method, service);
				SKYProxy.methodCache.put(methodKey, SKYMethod);
			}
			return SKYMethod;
		}
	}

	public static class Builder {

		private SKYLayoutInterceptor				skyLayoutInterceptor;		// 布局切换拦截器

		private SKYActivityInterceptor				skyActivityInterceptor;		// activity拦截器

		private SKYFragmentInterceptor				skyFragmentInterceptor;		// activity拦截器

		private ArrayList<BizStartInterceptor>		skyStartInterceptors;		// 方法开始拦截器

		private ArrayList<BizEndInterceptor>		bizEndInterceptors;			// 方法结束拦截器

		private ArrayList<ImplStartInterceptor>		implStartInterceptors;		// 方法开始拦截器

		private ArrayList<ImplEndInterceptor>		implEndInterceptors;		// 方法结束拦截器

		private ArrayList<SKYBizErrorInterceptor>		skyErrorInterceptors;		// 方法错误拦截器

		private DisplayStartInterceptor				displayStartInterceptor;	// 方法开始拦截器

		private DisplayEndInterceptor				displayEndInterceptor;		// 方法结束拦截器

		private ArrayList<SKYHttpErrorInterceptor>	skyHttpErrorInterceptors;	// 网络错误拦截

		public void setActivityInterceptor(SKYActivityInterceptor SKYActivityInterceptor) {
			this.skyActivityInterceptor = SKYActivityInterceptor;
		}

		public void setFragmentInterceptor(SKYFragmentInterceptor SKYFragmentInterceptor) {
			this.skyFragmentInterceptor = SKYFragmentInterceptor;
		}

		public void setSkyLayoutInterceptor(SKYLayoutInterceptor skyLayoutInterceptor) {
			this.skyLayoutInterceptor = skyLayoutInterceptor;
		}

		public Builder addStartInterceptor(BizStartInterceptor bizStartInterceptor) {
			if (skyStartInterceptors == null) {
				skyStartInterceptors = new ArrayList<>();
			}
			if (!skyStartInterceptors.contains(bizStartInterceptor)) {
				skyStartInterceptors.add(bizStartInterceptor);
			}
			return this;
		}

		public Builder addEndInterceptor(BizEndInterceptor bizEndInterceptor) {
			if (bizEndInterceptors == null) {
				bizEndInterceptors = new ArrayList<>();
			}
			if (!bizEndInterceptors.contains(bizEndInterceptor)) {
				bizEndInterceptors.add(bizEndInterceptor);
			}
			return this;
		}

		public Builder setDisplayStartInterceptor(DisplayStartInterceptor displayStartInterceptor) {
			this.displayStartInterceptor = displayStartInterceptor;
			return this;
		}

		public Builder setDisplayEndInterceptor(DisplayEndInterceptor displayEndInterceptor) {
			this.displayEndInterceptor = displayEndInterceptor;
			return this;
		}

		public Builder addStartImplInterceptor(ImplStartInterceptor implStartInterceptor) {
			if (implStartInterceptors == null) {
				implStartInterceptors = new ArrayList<>();
			}
			if (!implStartInterceptors.contains(implStartInterceptor)) {
				implStartInterceptors.add(implStartInterceptor);
			}
			return this;
		}

		public Builder addEndImplInterceptor(ImplEndInterceptor implEndInterceptor) {
			if (implEndInterceptors == null) {
				implEndInterceptors = new ArrayList<>();
			}
			if (!implEndInterceptors.contains(implEndInterceptor)) {
				implEndInterceptors.add(implEndInterceptor);
			}
			return this;
		}

		public void addBizErrorInterceptor(SKYBizErrorInterceptor SKYErrorInterceptor) {
			if (skyErrorInterceptors == null) {
				skyErrorInterceptors = new ArrayList<>();
			}
			if (!skyErrorInterceptors.contains(SKYErrorInterceptor)) {
				skyErrorInterceptors.add(SKYErrorInterceptor);
			}
		}

		public void addHttpErrorInterceptor(SKYHttpErrorInterceptor skyHttpErrorInterceptor) {
			if (skyHttpErrorInterceptors == null) {
				skyHttpErrorInterceptors = new ArrayList<>();
			}
			if (!skyHttpErrorInterceptors.contains(skyHttpErrorInterceptor)) {
				skyHttpErrorInterceptors.add(skyHttpErrorInterceptor);
			}
		}

		public SKYMethods build() {
			// 默认值
			ensureSaneDefaults();
			return new SKYMethods(skyLayoutInterceptor, skyActivityInterceptor, skyFragmentInterceptor, skyStartInterceptors, displayStartInterceptor, bizEndInterceptors, displayEndInterceptor,
					implStartInterceptors, implEndInterceptors, skyErrorInterceptors, skyHttpErrorInterceptors);
		}

		private void ensureSaneDefaults() {
			if (skyStartInterceptors == null) {
				skyStartInterceptors = new ArrayList<>();
			}
			if (bizEndInterceptors == null) {
				bizEndInterceptors = new ArrayList<>();
			}
			if (skyErrorInterceptors == null) {
				skyErrorInterceptors = new ArrayList<>();
			}
			if (skyFragmentInterceptor == null) {
				skyFragmentInterceptor = SKYFragmentInterceptor.NONE;
			}
			if (skyActivityInterceptor == null) {
				skyActivityInterceptor = SKYActivityInterceptor.NONE;
			}
			if (skyLayoutInterceptor == null) {
				skyLayoutInterceptor = SKYLayoutInterceptor.NONE;
			}
			if (implStartInterceptors == null) {
				implStartInterceptors = new ArrayList<>();
			}
			if (implEndInterceptors == null) {
				implEndInterceptors = new ArrayList<>();
			}
			if (skyHttpErrorInterceptors == null) {
				skyHttpErrorInterceptors = new ArrayList<>();
			}
		}

	}
}
