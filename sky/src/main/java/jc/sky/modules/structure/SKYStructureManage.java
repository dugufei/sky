package jc.sky.modules.structure;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.SimpleArrayMap;
import android.view.KeyEvent;

import com.google.common.base.Strings;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jc.sky.R;
import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.core.SKYIBiz;
import jc.sky.modules.log.L;
import jc.sky.view.SKYActivity;
import jc.sky.view.SKYFragment;

/**
 * @author sky
 * @version 版本
 */

public class SKYStructureManage implements SKYStructureIManage {

	private final ConcurrentHashMap<Class<?>, SimpleArrayMap<Integer, SKYStructureModel>> statckRepeatBiz;

	public SKYStructureManage() {
		/** 初始化集合 **/
		statckRepeatBiz = new ConcurrentHashMap<>();

	}

	@Override public void attach(SKYStructureModel view) {
		synchronized (statckRepeatBiz) {
			SimpleArrayMap<Integer, SKYStructureModel> stack = statckRepeatBiz.get(view.getService());
			if (stack == null) {
				stack = new SimpleArrayMap();
			}
			stack.put(view.key, view);

			statckRepeatBiz.put(view.getService(), stack);

			if (SKYHelper.isLogOpen()) {
				L.tag("SKYStructureManage");
				L.i(view.getView().getClass().getSimpleName() + " -- stack:put(" + view.key + ")");
			}
		}
	}

	@Override public void detach(SKYStructureModel view) {
		synchronized (statckRepeatBiz) {

			SimpleArrayMap<Integer, SKYStructureModel> stack = statckRepeatBiz.get(view.getService());

			if (stack == null) {
				return;
			}

			SKYStructureModel SKYStructureModel = stack.get(view.key);

			stack.remove(view.key);

			if (stack.size() < 1) {
				statckRepeatBiz.remove(view.getService());
			}

			if (SKYHelper.isLogOpen()) {
				L.tag("SKYStructureManage");
				L.i(view.getView().getClass().getSimpleName() + " -- stack:remove(" + view.key + ")");

				L.tag("SKYStructureManage");
				StringBuilder builder = new StringBuilder("\u21E0 ");
				builder.append("SKYStructureManage.statckRepeatBiz").append('(');
				if (statckRepeatBiz != null && statckRepeatBiz.size() > 0) {
					for (Class clazz : statckRepeatBiz.keySet()) {
						builder.append(clazz.getSimpleName());
						builder.append(", ");
					}
					builder.deleteCharAt(builder.length() - 1);
				}

				builder.append(')');
			}

			if (SKYStructureModel != null) {
				SKYStructureModel.clearAll();
			}
		}
	}

	@Override public <B extends SKYIBiz> B biz(Class<B> biz) {
		return biz(biz, 0);
	}

	@Override public <B extends SKYIBiz> B biz(Class<B> biz, int position) {
		SimpleArrayMap<Integer, SKYStructureModel> stack = statckRepeatBiz.get(biz);
		if (stack == null) {
			Set<Map.Entry<Class<?>, SimpleArrayMap<Integer, SKYStructureModel>>> entrySet = statckRepeatBiz.entrySet();
			for (Map.Entry<Class<?>, SimpleArrayMap<Integer, SKYStructureModel>> entry : entrySet) {
				SimpleArrayMap<Integer, SKYStructureModel> simpleArrayMap = entry.getValue();
				if (simpleArrayMap.valueAt(position).isSupterClass(biz)) {
					return (B) simpleArrayMap.valueAt(position).getSKYProxy().proxy;
				}
			}

			return createNullService(biz);
		}
		SKYStructureModel SKYStructureModel = stack.valueAt(position);
		if (SKYStructureModel == null) {
			return createNullService(biz);
		}
		if (SKYStructureModel.getSKYProxy() == null || SKYStructureModel.getSKYProxy().proxy == null) {
			return createNullService(biz);
		}
		return (B) SKYStructureModel.getSKYProxy().proxy;
	}

	@Override public <B extends SKYIBiz> boolean isExist(Class<B> biz) {
		return isExist(biz, 0);
	}

	@Override public <B extends SKYIBiz> boolean isExist(Class<B> biz, int position) {
		SimpleArrayMap<Integer, SKYStructureModel> stack = statckRepeatBiz.get(biz);
		if (stack == null) {
			Set<Map.Entry<Class<?>, SimpleArrayMap<Integer, SKYStructureModel>>> entrySet = statckRepeatBiz.entrySet();
			for (Map.Entry<Class<?>, SimpleArrayMap<Integer, SKYStructureModel>> entry : entrySet) {
				SimpleArrayMap<Integer, SKYStructureModel> simpleArrayMap = entry.getValue();
				if (simpleArrayMap.valueAt(position).isSupterClass(biz)) {
					return true;
				}
			}
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

	@Override public <T> T createMainLooper(Class<T> service, final Object ui) {
		SKYCheckUtils.validateServiceInterface(service);
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

	public <U> U createNullService(final Class<U> service) {
		SKYCheckUtils.validateServiceInterface(service);
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
}