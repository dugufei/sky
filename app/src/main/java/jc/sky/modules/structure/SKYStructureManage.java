package jc.sky.modules.structure;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.SimpleArrayMap;
import android.view.KeyEvent;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import jc.sky.R;
import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.core.Impl;
import jc.sky.core.SKYIBiz;
import jc.sky.core.SKYICommonBiz;
import jc.sky.display.SKYIDisplay;
import jc.sky.modules.log.L;
import jc.sky.modules.methodProxy.SKYProxy;
import jc.sky.view.SKYActivity;
import jc.sky.view.SKYFragment;

/**
 * @创建人 sky
 * @创建时间 15/9/10 下午3:57
 * @类描述 结构管理器
 */

public class SKYStructureManage implements SKYStructureIManage {

	private final ConcurrentHashMap<Class<?>, Object>										stackDisplay;

	private final ConcurrentHashMap<Class<?>, Object>										stackHttp;

	private final ConcurrentHashMap<Class<?>, Object>										stackCommonBiz;

	private final ConcurrentHashMap<Class<?>, Object>										stackImpl;

	private final ConcurrentHashMap<Class<?>, SimpleArrayMap<Integer, SKYStructureModel>>	statckRepeatBiz;

	public SKYStructureManage() {
		/** 初始化集合 **/
		stackHttp = new ConcurrentHashMap<>();
		stackCommonBiz = new ConcurrentHashMap<>();
		stackDisplay = new ConcurrentHashMap<>();
		stackImpl = new ConcurrentHashMap<>();
		statckRepeatBiz = new ConcurrentHashMap<>();

	}

	@Override public synchronized void attach(SKYStructureModel view) {
		synchronized (statckRepeatBiz) {
			SimpleArrayMap<Integer, SKYStructureModel> stack = statckRepeatBiz.get(view.getService());
			if (stack == null) {
				stack = new SimpleArrayMap();
			}
			stack.put(view.key, view);
			statckRepeatBiz.put(view.getService(), stack);
		}
	}

	@Override public void detach(SKYStructureModel view) {
		synchronized (statckRepeatBiz) {
			SimpleArrayMap<Integer, SKYStructureModel> stack = statckRepeatBiz.get(view.getService());
			if (stack != null) {
				SKYStructureModel SKYStructureModel = stack.get(view.key);
				if (SKYStructureModel == null) {
					return;
				}
				stack.remove(SKYStructureModel.key);
				if (stack.size() < 1) {
					statckRepeatBiz.remove(view.getService());
				}
				SKYStructureModel.clearAll();
			}
		}
		synchronized (stackImpl) {
			stackImpl.clear();
		}
		synchronized (stackDisplay) {
			stackDisplay.clear();
		}
		synchronized (stackCommonBiz) {
			stackCommonBiz.clear();
		}
		synchronized (stackHttp) {
			stackHttp.clear();
		}
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
			Object o = c.newInstance();
			return o;
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，没有找到业务类！");
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，实例化异常！");
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，访问权限异常！");
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，没有找到构造方法！");
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，反射异常！");
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

	@Override public <B extends SKYIBiz> B biz(Class<B> biz) {
		SimpleArrayMap<Integer, SKYStructureModel> stack = statckRepeatBiz.get(biz);
		if (stack == null) {
			return createNullService(biz);
		}
		SKYStructureModel SKYStructureModel = stack.valueAt(0);
		if (SKYStructureModel == null) {
			return createNullService(biz);
		}
		if (SKYStructureModel.getSKYProxy() == null || SKYStructureModel.getSKYProxy().proxy == null) {
			return createNullService(biz);
		}
		return (B) SKYStructureModel.getSKYProxy().proxy;
	}

	@Override public <B extends SKYIBiz> boolean isExist(Class<B> biz) {
		SimpleArrayMap<Integer, SKYStructureModel> stack = statckRepeatBiz.get(biz);
		if (stack == null) {
			return false;
		}
		SKYStructureModel SKYStructureModel = stack.valueAt(0);
		if (SKYStructureModel == null) {
			return false;
		}
		if (SKYStructureModel.getSKYProxy() == null || SKYStructureModel.getSKYProxy().proxy == null) {
			return false;
		}
		return true;
	}

	@Override public <D extends SKYIDisplay> D display(Class<D> displayClazz) {
		D display = (D) stackDisplay.get(displayClazz);
		if (display == null) {
			synchronized (stackDisplay) {
				if (display == null) {
					SKYCheckUtils.checkNotNull(displayClazz, "display接口不能为空");
					SKYCheckUtils.validateServiceInterface(displayClazz);
					Object impl = getImplClass(displayClazz);
					SKYProxy SKYProxy = SKYHelper.methodsProxy().createDisplay(displayClazz, impl);
					stackDisplay.put(displayClazz, SKYProxy.proxy);
					display = (D) SKYProxy.proxy;
				}
			}
		}
		return display;
	}

	@Override public <B extends SKYICommonBiz> B common(Class<B> service) {
		B b = (B) stackCommonBiz.get(service);
		if (b == null) {
			synchronized (stackCommonBiz) {
				if (b == null) {
					SKYCheckUtils.checkNotNull(service, "biz接口不能为空～");
					SKYCheckUtils.validateServiceInterface(service);
					Object impl = getImplClass(service);
					SKYProxy SKYProxy = SKYHelper.methodsProxy().create(service, impl);
					stackCommonBiz.put(service, SKYProxy.proxy);
					b = (B) SKYProxy.proxy;
				}
			}
		}
		return b;
	}

	@Override public <B extends SKYIBiz> List<B> bizList(Class<B> service) {
		SimpleArrayMap<Integer, SKYStructureModel> stack = statckRepeatBiz.get(service);
		List list = new ArrayList();
		if (stack == null) {
			return list;
		}
		int count = stack.size();
		for (int i = 0; i < count; i++) {
			SKYStructureModel SKYStructureModel = stack.valueAt(i);
			if (SKYStructureModel == null || SKYStructureModel.getSKYProxy() == null || SKYStructureModel.getSKYProxy().proxy == null) {
				list.add(createNullService(service));
			} else {
				list.add(SKYStructureModel.getSKYProxy().proxy);
			}
		}
		return list;
	}

	@Override public <H> H http(Class<H> httpClazz) {
		H http = (H) stackHttp.get(httpClazz);
		if (http == null) {
			synchronized (stackHttp) {
				if (http == null) {
					SKYCheckUtils.checkNotNull(httpClazz, "http接口不能为空");
					SKYCheckUtils.validateServiceInterface(httpClazz);
					http = SKYHelper.httpAdapter().create(httpClazz);
					stackHttp.put(httpClazz, http);
				}
			}
		}
		return http;
	}

	@Override public <P> P impl(Class<P> implClazz) {
		P impl = (P) stackImpl.get(implClazz);
		if (impl == null) {
			synchronized (stackImpl) {
				if (impl == null) {
					SKYCheckUtils.checkNotNull(implClazz, "impl接口不能为空");
					SKYCheckUtils.validateServiceInterface(implClazz);
					impl = SKYHelper.methodsProxy().createImpl(implClazz, getImplClass(implClazz));
					stackImpl.put(implClazz, impl);
				}
			}
		}
		return impl;
	}

	@Override public <T> T createMainLooper(Class<T> service, final Object ui) {
		SKYCheckUtils.validateServiceInterface(service);
		return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new InvocationHandler() {

			@Override public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
				// 如果有返回值 - 直接执行
				if (!method.getReturnType().equals(void.class)) {
                    if(ui == null){
                        return null;
                    }
					return method.invoke(ui, args);
				}

				// 如果是主线程 - 直接执行
				if (!SKYHelper.isMainLooperThread()) {// 子线程
                    if(ui == null){
                        return null;
                    }
					return method.invoke(ui, args);
				}
				Runnable runnable = new Runnable() {

					@Override public void run() {
						try {
                            if(ui == null){
                                return;
                            }
							method.invoke(ui, args);
						} catch (Exception throwable) {
							if (SKYHelper.getInstance().isLogOpen()) {
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

	public <U> U createNullService(final Class<U> service) {
		SKYCheckUtils.validateServiceInterface(service);
		return (U) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service }, new InvocationHandler() {

			@Override public Object invoke(Object proxy, Method method, Object... args) throws Throwable {

				if (SKYHelper.getInstance().isLogOpen()) {
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
		if (SKYHelper.getInstance().isLogOpen()) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("[");
			for (Fragment fragment : fragmentManager.getFragments()) {
				if (fragment != null) {
					stringBuilder.append(",");
					stringBuilder.append(fragment.getClass().getSimpleName());
				}
			}
			stringBuilder.append("]");
			stringBuilder.deleteCharAt(1);
			L.tag("Activity FragmentManager:");
			L.i(stringBuilder.toString());
		}
	}
}