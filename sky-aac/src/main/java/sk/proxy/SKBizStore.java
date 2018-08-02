package sk.proxy;

import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import sk.L;
import sk.SKBiz;
import sk.SKDisplay;
import sk.SKHelper;
import sky.cglib.proxy.Enhancer;
import sky.cglib.proxy.MethodInterceptor;

import static sk.utils.SKUtils.validateServiceInterface;

/**
 * @author sky
 * @version 1.0 on 2018-07-31 下午3:19
 * @see SKBizStore
 */
public final class SKBizStore {

	private final ConcurrentHashMap<Class<?>, Stack<SKProxy>> stackRepeatBiz;

	public SKBizStore() {
		/** 初始化集合 **/
		stackRepeatBiz = new ConcurrentHashMap<>();
	}

	public void add(SKProxy skProxy) {

		synchronized (stackRepeatBiz) {

			Stack stack = stackRepeatBiz.get(skProxy.bizClass);
			if (stack == null) {
				stack = new Stack<>();
			}
			stack.push(skProxy);
			stackRepeatBiz.put(skProxy.bizClass, stack);

			L.i("SKBizStore add biz class (" + skProxy.bizClass.getCanonicalName() + ")");
		}
	}

	public void remove(SKProxy skProxy) {
		synchronized (stackRepeatBiz) {

			Stack stack = stackRepeatBiz.get(skProxy.bizClass);
			if (stack == null) {
				return;
			}
			stack.remove(skProxy);

			if (stack.size() < 1) {
				stackRepeatBiz.remove(skProxy.bizClass);
			}

			L.i("SKBizStore remove biz class (" + skProxy.bizClass.getCanonicalName() + ")");
		}
	}

	public <B extends SKBiz> B biz(Class<B> biz) {
		Stack<SKProxy> stack = stackRepeatBiz.get(biz);
		if (stack == null) {
			return createNullProxy(biz);
		}
		SKProxy skProxy = stack.peek();

		return (B) skProxy.proxy;
	}

	public SKProxy createProxy(final Class superClazz, Object impl) {
		final SKProxy skProxy = new SKProxy();
		skProxy.bizClass = superClazz;
		skProxy.impl = impl;
		Enhancer e = new Enhancer(SKHelper.getInstance());
		e.setSuperclass(superClazz);
		e.setInterceptor(new MethodInterceptor() {

			@Override public Object intercept(String name, Class[] argsType, Object[] args) throws Exception {
				Method method = superClazz.getMethod(name, argsType);

				// 如果有返回值 - 直接执行
				if (!method.getReturnType().equals(void.class)) {
					return method.invoke(skProxy.impl, args);
				}

				SKMethod skMethod = loadSKMethod(skProxy, method, superClazz);
				// 开始
				if (!SKHelper.isLogOpen()) {
					return skMethod.invoke(skProxy.impl, args);
				}
				enterMethod(method, args);
				long startNanos = System.nanoTime();

				Object result = skMethod.invoke(skProxy.impl, args);

				long stopNanos = System.nanoTime();
				long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
				exitMethod(method, result, lengthMillis);

				return result;
			}
		});
		skProxy.proxy = e.create();
		return skProxy;
	}

	public <T> SKProxy createDisplay(final Class<T> service, final SKDisplay impl) {
		final SKProxy SKYProxy = new SKProxy();
		SKYProxy.impl = impl;
		SKYProxy.proxy = Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new InvocationHandler() {

			@Override public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
				// 如果有返回值 - 直接执行
				if (!method.getReturnType().equals(void.class)) {
					return method.invoke(SKYProxy.impl, args);

				}
				SKMethod SKYMethod = loadDisplaySKMethod(SKYProxy, method, service);
				// 开始
				if (!SKHelper.isLogOpen()) {
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
				builder.append(SKStrings.toString(parameterValues[i]));
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
			builder.append(SKStrings.toString(result));
		}
		Log.v(cls.getSimpleName(), builder.toString());
	}

	/**
	 * 加载接口
	 *
	 * @param skProxy
	 *            参数
	 * @param method
	 *            参数
	 * @param service
	 *            参数
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	private <T> SKMethod loadSKMethod(SKProxy skProxy, Method method, Class<T> service) {
		synchronized (skProxy.methodCache) {
			String methodKey = getKey(method, method.getParameterTypes());
			SKMethod SKYMethod = skProxy.methodCache.get(methodKey);
			if (SKYMethod == null) {
				SKYMethod = SKMethod.createMethod(method, service);
				skProxy.methodCache.put(methodKey, SKYMethod);
			}
			return SKYMethod;
		}
	}

	private <T> SKMethod loadDisplaySKMethod(SKProxy skProxy, Method method, Class<T> service) {
		synchronized (skProxy.methodCache) {
			String methodKey = getKey(method, method.getParameterTypes());
			SKMethod SKYMethod = skProxy.methodCache.get(methodKey);
			if (SKYMethod == null) {
				SKYMethod = SKMethod.createDisplayMethod(method, service);
				skProxy.methodCache.put(methodKey, SKYMethod);
			}
			return SKYMethod;
		}
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

	public <U> U createNullProxy(final Class<U> service) {
		Enhancer e = new Enhancer(SKHelper.getInstance());
		e.setSuperclass(service);
		e.setInterceptor(new MethodInterceptor() {

			@Override public Object intercept(String name, Class[] argsType, final Object[] args) throws Exception {
				final Method method = service.getMethod(name, argsType);
				if (SKHelper.isLogOpen()) {
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
}
