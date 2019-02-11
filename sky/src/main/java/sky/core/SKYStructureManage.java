package sky.core;

import android.os.Looper;
import android.os.Trace;
import android.util.Log;
import android.view.KeyEvent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import androidx.fragment.app.FragmentManager;
import sky.cglib.proxy.Enhancer;
import sky.cglib.proxy.MethodInterceptor;
import sky.core.plugins.ImplEndInterceptor;
import sky.core.plugins.ImplStartInterceptor;

import static sky.core.SKYUtils.validateServiceInterface;

/**
 * @author sky
 * @version 版本
 */

class SKYStructureManage implements SKYStructureIManage {

	private final ConcurrentHashMap<Class<?>, Stack<SKYStructureModel>> statckRepeatBiz;

	public SKYStructureManage() {
		/** 初始化集合 **/
		statckRepeatBiz = new ConcurrentHashMap<>();
	}

	@Override public void attach(SKYStructureModel view) {
		synchronized (statckRepeatBiz) {
			if (view.getService() == null) {
				return;
			}

			Stack<SKYStructureModel> stack = statckRepeatBiz.get(view.getService());
			if (stack == null) {
				stack = new Stack<>();
			}
			stack.push(view);

			statckRepeatBiz.put(view.getService(), stack);
		}
	}

	@Override public void detach(SKYStructureModel view) {
		synchronized (statckRepeatBiz) {
			if (view.getService() == null) {
				return;
			}
			Stack<SKYStructureModel> stack = statckRepeatBiz.get(view.getService());

			if (stack == null) {
				return;
			}

			SKYStructureModel skyStructureModel = stack.pop();

			if (skyStructureModel == null) {
				L.d("SKYStructureManage.detach not find model(" + view.key + ")");
				return;
			}

			if (stack.size() < 1) {
				statckRepeatBiz.remove(view.getService());
			}

			if (SKYHelper.isLogOpen()) {
				L.d(view.getView().getClass().getSimpleName() + " -- stack:remove(" + view.key + ")");

				StringBuilder builder = new StringBuilder("\u21E0 ");
				builder.append("SKYStructureManage.Biz").append('(');
				if (statckRepeatBiz != null && statckRepeatBiz.size() > 0) {
					for (Class clazz : statckRepeatBiz.keySet()) {
						builder.append(clazz.getSimpleName());
						builder.append(", ");
					}
					builder.deleteCharAt(builder.length() - 1);
				}

				builder.append(')');
				L.d(builder.toString());
			}
			if (skyStructureModel != null) {
				skyStructureModel.clearAll();
			}
		}
	}

	@Override public <B extends SKYBiz> B biz(Class<B> biz) {
		Stack<SKYStructureModel> stack = statckRepeatBiz.get(biz);
		if (stack == null) {
			return createNullService(biz);
		}
		SKYStructureModel skyStructureModel = stack.peek();
		if (skyStructureModel == null) {
			return createNullService(biz);
		}

		if (skyStructureModel.getSKYProxy() == null || skyStructureModel.getSKYProxy().proxy == null) {
			return createNullService(biz);
		}
		return (B) skyStructureModel.getSKYProxy().proxy;
	}

	@Override public <B extends SKYBiz> boolean isExist(Class<B> biz) {
		Stack<SKYStructureModel> stack = statckRepeatBiz.get(biz);
		if (stack == null) {
			return false;
		}
		SKYStructureModel skyStructureModel = stack.peek();
		if (skyStructureModel == null) {
			return false;
		}

		if (skyStructureModel.getSKYProxy() == null || skyStructureModel.getSKYProxy().proxy == null) {
			return false;
		}
		return true;
	}

	@Override public <B extends SKYBiz> ArrayList<B> bizList(Class<B> service) {
		Stack<SKYStructureModel> stack = statckRepeatBiz.get(service);
		ArrayList list = new ArrayList();
		if (stack == null) {
			return list;
		}

		int count = stack.size();

		for (int i = 0; i < count; i++) {
			SKYStructureModel skyStructureModel = stack.get(i);
			if (skyStructureModel == null || skyStructureModel.getSKYProxy() == null || skyStructureModel.getSKYProxy().proxy == null) {
				list.add(0, createNullService(service));
			} else {
				list.add(0, skyStructureModel.getSKYProxy().proxy);
			}
		}
		return list;
	}

	@Override public <T> T createMainLooper(Class<T> service, final Object ui) {
		validateServiceInterface(service);
		return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new InvocationHandler() {

			@Override public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
				// 如果有返回值 - 直接执行
				if (!method.getReturnType().equals(void.class)) {
					if (ui == null) {
						return null;
					}
					return method.invoke(ui, args);
				}

				// 如果是主线程 - 直接执行
				if (!SKYHelper.isMainLooperThread()) {// 子线程
					if (ui == null) {
						return null;
					}
					return method.invoke(ui, args);
				}
				Runnable runnable = new Runnable() {

					@Override public void run() {
						try {
							if (ui == null) {
								return;
							}
							method.invoke(ui, args);
						} catch (Exception throwable) {
							if (SKYHelper.isLogOpen()) {
								throwable.printStackTrace();
							}
							return;
						}
					}
				};
				SKYHelper.mainLooper().execute(runnable);
				return null;
			}
		});
	}

	@Override public <T> T createMainLooperNotIntf(final Class<T> service, final Object ui) {
		Enhancer e = new Enhancer(SKYHelper.getInstance());
		e.setSuperclass(service);
		e.setInterceptor(new MethodInterceptor() {

			@Override public Object intercept(String name, Class[] argsType, final Object[] args) throws Exception {
				final Method method = service.getMethod(name, argsType);

				// 如果有返回值 - 直接执行
				if (!method.getReturnType().equals(void.class)) {
					if (ui == null) {
						return null;
					}
					return method.invoke(ui, args);
				}

				// 如果是主线程 - 直接执行
				if (!SKYHelper.isMainLooperThread()) {// 子线程
					if (ui == null) {
						return null;
					}
					return method.invoke(ui, args);
				}
				Runnable runnable = new Runnable() {

					@Override public void run() {
						try {
							if (ui == null) {
								return;
							}
							method.invoke(ui, args);
						} catch (Exception throwable) {
							if (SKYHelper.isLogOpen()) {
								throwable.printStackTrace();
							}
							return;
						}
					}
				};
				SKYHelper.mainLooper().execute(runnable);
				return null;
			}
		});
		return (T) e.create();
	}

	public <U> U createNullServiceNotInf(final Class<U> service) {
		Enhancer e = new Enhancer(SKYHelper.getInstance());
		e.setSuperclass(service);
		e.setInterceptor(new MethodInterceptor() {

			@Override public Object intercept(String name, Class[] argsType, final Object[] args) throws Exception {
				final Method method = service.getMethod(name, argsType);
				if (SKYHelper.isLogOpen()) {
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("UI被销毁,回调接口继续执行");
					stringBuilder.append("方法[");
					stringBuilder.append(method.getName());
					stringBuilder.append("]");
					L.tag(service.getSimpleName());
					L.i(stringBuilder.toString());
				}

				if (method.getReturnType().equals(int.class) || method.getReturnType().equals(long.class) || method.getReturnType().equals(float.class) || method.getReturnType().equals(double.class)
						|| method.getReturnType().equals(short.class)) {
					return 0;
				}

				if (method.getReturnType().equals(boolean.class)) {
					return false;
				}
				if (method.getReturnType().equals(byte.class)) {
					return Byte.parseByte(null);
				}
				if (method.getReturnType().equals(char.class)) {
					return ' ';
				}
				return null;
			}
		});
		return (U) e.create();
	}

	public <U> U createNullService(final Class<U> service) {
		if (!service.isInterface()) {
			return createNullServiceNotInf(service);
		}

		return (U) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new InvocationHandler() {

			@Override public Object invoke(Object proxy, Method method, Object... args) throws Throwable {

				if (SKYHelper.isLogOpen()) {
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("UI被销毁,回调接口继续执行");
					stringBuilder.append("方法[");
					stringBuilder.append(method.getName());
					stringBuilder.append("]");
					L.tag(service.getSimpleName());
					L.i(stringBuilder.toString());
				}

				if (method.getReturnType().equals(int.class) || method.getReturnType().equals(long.class) || method.getReturnType().equals(float.class) || method.getReturnType().equals(double.class)
						|| method.getReturnType().equals(short.class)) {
					return 0;
				}

				if (method.getReturnType().equals(boolean.class)) {
					return false;
				}
				if (method.getReturnType().equals(byte.class)) {
					return Byte.parseByte(null);
				}
				if (method.getReturnType().equals(char.class)) {
					return ' ';
				}
				return null;
			}
		});
	}

	@Override public boolean onKeyBack(int keyCode, FragmentManager fragmentManager, SKYActivity bSKYActivity) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			int idx = fragmentManager.getBackStackEntryCount();
			if (idx > 1) {
				FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(idx - 1);
				Object view = fragmentManager.findFragmentByTag(entry.getName());
				if (view instanceof SKYFragment) {
					return ((SKYFragment) view).onKeyBack();
				}
			} else {

				Object view = fragmentManager.findFragmentById(R.id.sky_home);
				if (view instanceof SKYFragment) {
					return ((SKYFragment) view).onKeyBack();
				}
			}
			if (bSKYActivity != null) {
				return bSKYActivity.onKeyBack();
			}
		}
		return false;
	}

	@Override public void printBackStackEntry(FragmentManager fragmentManager) {
		if (SKYHelper.isLogOpen()) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("[");

			int count = fragmentManager.getBackStackEntryCount();

			for (int i = 0; i < count; i++) {
				stringBuilder.append(",");
				stringBuilder.append(fragmentManager.getBackStackEntryAt(i).getName());
			}
			stringBuilder.append("]");
			stringBuilder.deleteCharAt(1);
			L.tag("Activity FragmentManager:");
			L.i(stringBuilder.toString());
		}
	}

	@Override public <T> SKYProxy createNotInf(final Class superClazz, Object impl) {
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
	@Override public <T> SKYProxy create(final Class<T> service, Object impl) {
		validateServiceInterface(service);

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
	@Override public <T> SKYProxy createDisplay(final Class<T> service, Object impl) {
		validateServiceInterface(service);

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
	@Override public <T> T createImpl(final Class<T> service, final Object impl) {
		validateServiceInterface(service);
		return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new SKYInvocationHandler() {

			@Override public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
				// 业务拦截器 - 前
				for (ImplStartInterceptor item : SKYHelper.methodsProxy().startInterceptors()) {
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
				for (ImplEndInterceptor item : SKYHelper.methodsProxy().endInterceptors()) {
					item.interceptEnd(impl.getClass().getName(), service, method, args, backgroundResult);
				}
				return backgroundResult;
			}
		});
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

		final String section = builder.toString().substring(2);
		Trace.beginSection(section);
	}

	private void exitMethod(Method method, Object result, long lengthMillis) {
		Trace.endSection();
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

}