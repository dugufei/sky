package sky.core;

import android.content.Intent;
import android.os.Bundle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sky.Background;
import sky.BackgroundType;
import sky.Interceptor;
import sky.Repeat;
import sky.core.exception.SKYHttpException;
import sky.core.exception.SKYNotUIPointerException;
import sky.core.plugins.BizEndInterceptor;
import sky.core.plugins.BizStartInterceptor;
import sky.core.plugins.SKYBizErrorInterceptor;
import sky.core.plugins.SKYHttpErrorInterceptor;

/**
 * @author sky
 * @version 版本
 */
final class SKYMethod {

	// 执行方法
	static final int	TYPE_INVOKE_EXE							= 0;

	static final int	TYPE_DISPLAY_INVOKE_EXE					= 4;

	// 执行后台方法
	static final int	TYPE_INVOKE_BACKGROUD_HTTP_EXE			= 1;

	static final int	TYPE_INVOKE_BACKGROUD_SINGLEWORK_EXE	= 2;

	static final int	TYPE_INVOKE_BACKGROUD_WORK_EXE			= 3;

	static SKYMethod createBizMethod(Method method, Class service) {
		// 是否重复
		boolean isRepeat = parseRepeat(method);
		// 拦截方法标记
		int interceptor = parseInterceptor(method);
		// 判断是否是子线程
		int type = parseBackground(method);

		return new SKYMethod(interceptor, method, type, isRepeat, service);
	}

	static <T> SKYMethod createDisplayMethod(Method method, Class<T> service) {
		// 是否重复
		boolean isRepeat = parseRepeat(method);
		// 拦截方法标记
		int interceptor = parseInterceptor(method);
		// 判断是否是子线程
		int type = TYPE_DISPLAY_INVOKE_EXE;

		return new SKYMethod(interceptor, method, type, isRepeat, service);

	}

	private static boolean parseRepeat(Method method) {

		Repeat SKYRepeat = method.getAnnotation(Repeat.class);
		if (SKYRepeat != null && SKYRepeat.value()) {
			return true;
		} else {
			return false;
		}
	}

	private static int parseBackground(Method method) {
		int type = TYPE_INVOKE_EXE;
		Background background = method.getAnnotation(Background.class);

		if (background != null) {
			BackgroundType backgroundType = background.value();

			switch (backgroundType) {
				case HTTP:
					type = TYPE_INVOKE_BACKGROUD_HTTP_EXE;
					break;
				case SINGLEWORK:
					type = TYPE_INVOKE_BACKGROUD_SINGLEWORK_EXE;
					break;
				case WORK:
					type = TYPE_INVOKE_BACKGROUD_WORK_EXE;
					break;
			}
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

	<T> T invoke(final Object impl, final Object[] args) throws InterruptedException {
		T result = null;
		if (!isRepeat) {
			if (isExe) { // 如果存在什么都不做
				if (SKYHelper.isLogOpen()) {
					L.tag("SKY-Method");
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
					case TYPE_INVOKE_BACKGROUD_HTTP_EXE:
						SKYHelper.threadPoolHelper().getHttpExecutorService().execute(methodRunnable);
						break;
					case TYPE_INVOKE_BACKGROUD_SINGLEWORK_EXE:
						SKYHelper.threadPoolHelper().getSingleWorkExecutorService().execute(methodRunnable);
						break;
					case TYPE_INVOKE_BACKGROUD_WORK_EXE:
						SKYHelper.threadPoolHelper().getWorkExecutorService().execute(methodRunnable);
						break;
				}
				break;
		}

		return result;
	}

	private class MethodRunnable extends SKYRunnable {

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
			exeError(throwable, method.getName());
		} finally {
			isExe = false;
		}
	}

	private void defaultMethod(Object[] objects) {
		try {
			exeMethod(method, impl, objects);
		} catch (Throwable throwable) {
			exeError(throwable, method.getName());
		} finally {
			isExe = false;
		}
	}

	private void exeDisplayMethod(final Method method, final Object impl, final Object[] objects) throws InvocationTargetException, IllegalAccessException {
		boolean isExe = true;
		String clazzName = null;
		Bundle bundle = null;
		// 业务拦截器 - 前
		if (SKYHelper.methodsProxy().displayStartInterceptor != null) {
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

			isExe = SKYHelper.methodsProxy().displayStartInterceptor.interceptStart(implName, service, method, interceptor, clazzName, bundle);
		}

		if (isExe) {
			// 如果是主线程 - 直接执行
			if (!SKYHelper.isMainLooperThread()) { // 主线程
				backgroundResult = method.invoke(impl, objects);
				return;
			}
			Runnable runnable = new Runnable() {

				@Override public void run() {
					try {
						method.invoke(impl, objects);
					} catch (Exception throwable) {
						if (SKYHelper.isLogOpen()) {
							throwable.printStackTrace();
						}
						return;
					}
				}
			};
			SKYHelper.mainLooper().execute(runnable);
			backgroundResult = null;// 执行
			// 业务拦截器 - 后
			if (SKYHelper.methodsProxy().displayEndInterceptor != null) {
				SKYHelper.methodsProxy().displayEndInterceptor.interceptEnd(implName, service, method, interceptor, clazzName, bundle, backgroundResult);
			}
		} else {
			if (SKYHelper.isLogOpen()) {
				Object[] parameterValues = objects;
				StringBuilder builder = new StringBuilder("\u21E2 ");
				builder.append(method.getName()).append('(');
				if (parameterValues != null) {
					for (int i = 0; i < parameterValues.length; i++) {
						if (i > 0) {
							builder.append(", ");
						}
						builder.append(Strings.toString(parameterValues[i]));
					}
				}

				builder.append(')');
				L.i("该方法被过滤 - %s", builder.toString());
			}
		}
	}

	void exeMethod(Method method, Object impl, Object[] objects) throws InvocationTargetException, IllegalAccessException {
		// 业务拦截器 - 前
		for (BizStartInterceptor item : SKYHelper.methodsProxy().bizStartInterceptor) {
			item.interceptStart(implName, service, method, interceptor, objects);
		}
		backgroundResult = method.invoke(impl, objects);// 执行
		// 业务拦截器 - 后
		for (BizEndInterceptor item : SKYHelper.methodsProxy().bizEndInterceptor) {
			item.interceptEnd(implName, service, method, interceptor, objects, backgroundResult);
		}
	}

	void exeError(Throwable throwable, String method) {
		if (SKYHelper.isLogOpen()) {
			throwable.printStackTrace();
		}

		if (impl instanceof SKYIIntercept) {
			SKYIIntercept skyiIntercept = (SKYIIntercept) impl;

			if (throwable.getCause() instanceof SKYHttpException) {
				if (!skyiIntercept.interceptHttpError(interceptor, (SKYHttpException) throwable.getCause())) {
					// 网络错误拦截器
					for (SKYHttpErrorInterceptor item : SKYHelper.methodsProxy().skyHttpErrorInterceptors) {
						item.interceptorError(((SKYBiz) impl).ui(), interceptor, (SKYHttpException) throwable.getCause());
					}
				}
			} else if (throwable.getCause() instanceof SKYNotUIPointerException) {
				// 忽略
				if (!skyiIntercept.interceptUIError(interceptor, (SKYNotUIPointerException) throwable.getCause())) {
				}
				return;
			} else {
				if (!skyiIntercept.interceptBizError(interceptor, throwable.getCause())) {
					// 业务错误拦截器
					for (SKYBizErrorInterceptor item : SKYHelper.methodsProxy().skyErrorInterceptor) {
						item.interceptorError(((SKYBiz) impl).ui(), interceptor, throwable.getCause());
					}
				}
			}
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
	SKYMethod(int interceptor, Method method, int type, boolean isRepeat, Class service) {
		this.interceptor = interceptor;
		this.type = type;
		this.isRepeat = isRepeat;
		this.method = method;
		this.service = service;
		if (type == TYPE_INVOKE_BACKGROUD_HTTP_EXE || type == TYPE_INVOKE_BACKGROUD_SINGLEWORK_EXE || type == TYPE_INVOKE_BACKGROUD_WORK_EXE) {
			this.methodRunnable = new MethodRunnable();
		}
	}
}
