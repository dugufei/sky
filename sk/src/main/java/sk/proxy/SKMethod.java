package sk.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import android.content.Intent;
import android.os.Bundle;

import sk.L;
import sk.SKErrorEnum;
import sk.SKHelper;
import sk.SKHttpException;
import sk.plugins.SKBizEndInterceptor;
import sk.plugins.SKBizStartInterceptor;
import sk.plugins.SKErrorInterceptor;
import sky.Interceptor;
import sky.Repeat;
import sky.SKHTTP;
import sky.SKIO;
import sky.SKWork;

/**
 * @author sky
 * @version 版本
 */
final class SKMethod {

	// 执行方法
	static final int	TYPE_INVOKE_EXE			= 0;

	static final int	TYPE_DISPLAY_INVOKE_EXE	= 4;

	// 执行后台方法
	static final int	TYPE_INVOKE_HTTP_EXE	= 1;

	static final int	TYPE_INVOKE_IO_EXE		= 2;

	static final int	TYPE_INVOKE_WORK_EXE	= 3;

	static SKMethod createMethod(Method method, Class service) {
		// 是否重复
		boolean isRepeat = parseRepeat(method);
		// 拦截方法标记
		int interceptor = parseInterceptor(method);
		// 判断是否是子线程
		int type = parseIOAndHTTP(method);

		return new SKMethod(interceptor, method, type, isRepeat, service);
	}

	static <T> SKMethod createDisplayMethod(Method method, Class<T> service) {
		// 是否重复
		boolean isRepeat = parseRepeat(method);
		int interceptor = parseInterceptor(method);
		// 判断是否是子线程
		int type1 = TYPE_DISPLAY_INVOKE_EXE;

		return new SKMethod(interceptor, method, type1, isRepeat, service);

	}

	private static boolean parseRepeat(Method method) {

		Repeat repeat = method.getAnnotation(Repeat.class);
		if (repeat != null && repeat.value()) {
			return true;
		} else {
			return false;
		}
	}

	private static int parseIOAndHTTP(Method method) {
		int type = TYPE_INVOKE_EXE;
		SKWork skWork = method.getAnnotation(SKWork.class);
		SKIO skIo = method.getAnnotation(SKIO.class);
		SKHTTP skHttp = method.getAnnotation(SKHTTP.class);

		if (skIo != null && skHttp != null) {
			throw new RuntimeException("method " + method.getName() + " there is only one annotation");
		}

		if (skIo != null) {
			type = TYPE_INVOKE_IO_EXE;
		}
		if (skHttp != null) {
			type = TYPE_INVOKE_HTTP_EXE;
		}
		if (skWork != null) {
			type = TYPE_INVOKE_WORK_EXE;
		}
		return type;
	}

	private static int parseInterceptor(Method method) {
		// 拦截方法标记
		Interceptor interceptorClass = method.getAnnotation(Interceptor.class);
		if (interceptorClass != null) {
			return interceptorClass.value();
		} else {
			return 0;
		}
	}

	<T> T invoke(final Object impl, final Object[] args) {
		T result = null;
		if (!isRepeat) {
			if (isExe) { // 如果存在什么都不做
				if (SKHelper.isLogOpen()) {
					L.tag("SK-Method");
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(impl.getClass().getSimpleName());
					stringBuilder.append(".");
					stringBuilder.append(method.getName());
					L.i("该方法正在执行 - %s", stringBuilder.toString());
				}
				return result;
			}
			isExe = true;
		}
		this.impl = impl;
		this.implName = impl.getClass().getName();

		switch (type) {
			case TYPE_INVOKE_EXE:
				defaultMethod(args);
				result = (T) backgroundResult;
				break;
			case TYPE_DISPLAY_INVOKE_EXE:
				displayMethod(args);
				result = (T) backgroundResult;
				break;
			default:
				if (isRepeat) {
					methodRunnable = new MethodRunnable();
				}
				methodRunnable.setArgs(args);
				switch (type) {
					case TYPE_INVOKE_HTTP_EXE:
						SKHelper.executors().network().execute(methodRunnable);
						break;
					case TYPE_INVOKE_IO_EXE:
						SKHelper.executors().diskIO().execute(methodRunnable);
						break;
					case TYPE_INVOKE_WORK_EXE:
						SKHelper.executors().work().execute(methodRunnable);
						break;
				}
				break;
		}

		return result;
	}

	private class MethodRunnable extends SKRunnable {

		Object[] objects;

		public MethodRunnable() {
			super("MethodRunnable");
		}

		public void setArgs(Object[] objects) {
			this.objects = objects;
		}

		@Override protected void execute() {
			defaultMethod(objects);
		}
	}

	private void displayMethod(Object[] objects) {
		try {
			exeDisplayMethod(method, impl, objects);
		} catch (Throwable throwable) {
			exeError(throwable, method, impl, objects);
		} finally {
			isExe = false;
		}
	}

	private void defaultMethod(Object[] objects) {
		try {
			exeMethod(method, impl, objects);
		} catch (Throwable throwable) {
			exeError(throwable, method, impl, objects);
		} finally {
			isExe = false;
		}
	}

	private void exeDisplayMethod(final Method method, final Object impl, final Object[] objects) throws InvocationTargetException, IllegalAccessException {
		boolean isExe = true;
		String clazzName = null;
		Bundle bundle = null;
		// 业务拦截器 - 前
		if (SKHelper.interceptor().displayStartInterceptor() != null) {
			String name = method.getName();
			if (name.startsWith("intent") && objects != null) {
				for (Object item : objects) {
					if (item instanceof Class) {
						clazzName = ((Class) item).getName();
					} else if (item instanceof Intent) {
						clazzName = ((Intent) item).getComponent().getClassName();
						bundle = ((Intent) item).getExtras();
					} else if (item instanceof Bundle) {
						bundle = (Bundle) item;
					}
				}
			}

			isExe = SKHelper.interceptor().displayStartInterceptor().interceptStart(clazzName, bundle);
		}

		if (isExe) {
			if (SKHelper.isLogOpen()) {
				Object[] parameterValues = objects;
				StringBuilder builder = new StringBuilder("\u21E2 ");
				builder.append(method.getName()).append('(');
				if (parameterValues != null) {
					for (int i = 0; i < parameterValues.length; i++) {
						if (i > 0) {
							builder.append(", ");
						}
						builder.append(SKStrings.toString(parameterValues[i]));
					}
				}

				builder.append(')');
				L.i("该方法被过滤 - %s", builder.toString());
			}
		}

		// 如果是主线程 - 直接执行
		if (SKHelper.isMainLooperThread()) { // 主线程
			backgroundResult = method.invoke(impl, objects);
			// 业务拦截器 - 后
			if (SKHelper.interceptor().displayEndInterceptor() != null) {
				SKHelper.interceptor().displayEndInterceptor().interceptEnd(clazzName, bundle, backgroundResult);
			}
			return;
		}
		final String finalClazzName = clazzName;
		final Bundle finalBundle = bundle;
		Runnable runnable = new Runnable() {

			@Override public void run() {
				try {
					backgroundResult = method.invoke(impl, objects);
					// 业务拦截器 - 后
					if (SKHelper.interceptor().displayEndInterceptor() != null) {
						SKHelper.interceptor().displayEndInterceptor().interceptEnd(finalClazzName, finalBundle, backgroundResult);
					}
				} catch (Exception throwable) {
					if (SKHelper.isLogOpen()) {
						throwable.printStackTrace();
					}
					return;
				}
			}
		};
		SKHelper.executors().mainThread().execute(runnable);

	}

	void exeMethod(Method method, Object impl, Object[] objects) throws InvocationTargetException, IllegalAccessException {
		// 业务拦截器 - 前
		for (SKBizStartInterceptor item : SKHelper.interceptor().bizStartInterceptors()) {
			item.interceptStart(implName, service, method, interceptor, objects);
		}
		backgroundResult = method.invoke(impl, objects);// 执行
		// 业务拦截器 - 后
		for (SKBizEndInterceptor item : SKHelper.interceptor().bizEndInterceptors()) {
			item.interceptEnd(implName, service, method, interceptor, objects, backgroundResult);
		}
	}

	void exeError(Throwable throwable, Method method, Object impl, Object[] objects) {
		if (SKHelper.isLogOpen()) {
			throwable.printStackTrace();
		}
		SKErrorEnum skErrorEnum = SKErrorEnum.LOGIC;
		for (SKErrorInterceptor skErrorInterceptor : SKHelper.interceptor().skErrorInterceptors()) {
			if (throwable.getCause() instanceof SKHttpException) {
				skErrorEnum = SKErrorEnum.HTTP;
			}
			skErrorInterceptor.interceptorError(method, impl, objects, interceptor, skErrorEnum);
		}

	}

	int				type;

	Object			impl;

	String			implName;

	boolean			isRepeat;

	Method			method;

	MethodRunnable	methodRunnable;

	Class			service;

	int				interceptor;

	Object			backgroundResult;

	boolean			isExe;

	/**
	 * 构造函数
	 *
	 * @param interceptor
	 *            参数
	 * @param method
	 *            参数
	 * @param type
	 *            执行类型
	 * @param isRepeat
	 *            参数
	 * @param service
	 *            参数
	 */
	SKMethod(int interceptor, Method method, int type, boolean isRepeat, Class service) {
		this.interceptor = interceptor;
		this.type = type;
		this.isRepeat = isRepeat;
		this.method = method;
		this.service = service;
		if (type == TYPE_INVOKE_HTTP_EXE || type == TYPE_INVOKE_IO_EXE) {
			this.methodRunnable = new MethodRunnable();
		}
	}
}
