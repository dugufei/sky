package sky.core;

import android.app.Application;
import android.os.Looper;
import android.support.v4.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import sky.core.exception.SKYBizException;
import sky.core.exception.SKYHttpException;
import sky.core.exception.SKYUINullPointerException;
import sky.core.methodModule.SKYIModule;
import sky.core.methodModule.SKYIModuleMethod;
import sky.core.methodModule.SkyMethodModel;
import sky.core.screen.SKYScreenHolder;
import sky.core.screen.SKYScreenManager;

/**
 * @author sky
 * @version 版本 版本
 */
public class SKYHelper {

	static SKYModulesManage mSKYModulesManage;

	/**
	 * 绑定
	 * 
	 * @return 返回值
	 */
	public static Sky newSky() {
		return new Sky();
	}

	/**
	 * 获取管理
	 *
	 * @param <M>
	 *            参数
	 * @return 返回值
	 */
	protected static <M> M getManage() {
		return (M) mSKYModulesManage;
	}

	/**
	 * 获取启动管理器
	 *
	 * @param eClass
	 *            参数
	 * @param <D>
	 *            参数
	 * @return 返回值
	 */
	public static <D extends SKYIDisplay> D display(Class<D> eClass) {
		return mSKYModulesManage.skyCacheManager.display(eClass);
	}

	/**
	 * 执行业务代码
	 * 
	 * @param clazzName
	 *            类名
	 */
	public static synchronized SKYIModuleMethod moduleBiz(String clazzName) {
		SkyMethodModel skyMethodModel = mSKYModulesManage.provideModuleBiz.get(clazzName);
		if (skyMethodModel == null) {
			Class clazz = mSKYModulesManage.provideModule.get(clazzName);
			if (null == clazz) {
				L.d("Sky::没有匹配到Biz [" + clazzName + "]");
				return SKYIModuleMethod.NONE;

			}
			SKYIModule skyiModule;
			try {
				skyiModule = (SKYIModule) clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
			} catch (Exception var8) {
				L.e("Sky::加载组件时 出现了致命的异常. [" + var8.getMessage() + "]");
				return SKYIModuleMethod.NONE;
			}
			skyiModule.loadInto(mSKYModulesManage.provideModuleBiz);
			mSKYModulesManage.provideModule.remove(clazzName);
			return moduleBiz(clazzName);
		}
		return skyMethodModel;
	}

	/**
	 * 执行业务代码
	 *
	 * @param clazzName
	 *            类名
	 */
	public static synchronized SKYIModuleMethod moduleDisplay(String clazzName) {
		SkyMethodModel skyMethodModel = mSKYModulesManage.provideModuleBiz.get(clazzName);
		if (skyMethodModel == null) {
			Class clazz = mSKYModulesManage.provideModule.get(clazzName);
			if (null == clazz) {
				L.d("Sky::没有匹配到Display [" + clazzName + "]");
				return SKYIModuleMethod.NONE;

			}
			SKYIModule skyiModule;
			try {
				skyiModule = (SKYIModule) clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
			} catch (Exception var8) {
				L.e("Sky::加载组件时 出现了致命的异常. [" + var8.getMessage() + "]");
				return SKYIModuleMethod.NONE;
			}
			skyiModule.loadInto(mSKYModulesManage.provideModuleBiz);
			mSKYModulesManage.provideModule.remove(clazzName);
			return moduleBiz(clazzName);
		}
		return skyMethodModel;
	}

	/**
	 * 获取业务
	 *
	 * @param service
	 *            参数
	 * @param <B>
	 *            参数
	 * @return 返回值
	 */
	public static <B extends SKYBiz> B biz(Class<B> service) {
		if (checkBizIsPublic(service)) { // 判定是否是公共方法
			return mSKYModulesManage.skyCacheManager.biz(service);
		}
		return structureHelper().biz(service);
	}

	/**
	 * 获取 ui
	 * 
	 * @param uiClazz
	 *            参数
	 * @param <U>
	 *            参数
	 * @return 返回值
	 */
	public static <U> U ui(Class<U> uiClazz) {
		if (uiClazz.isInterface()) {
			throw new SKYUINullPointerException("3.0.0 - ui(class)方法,3.0.0版本不支持接口获取代理类");
		}
		U u = screenHelper().getActivityOf(uiClazz);
		boolean isMain = isMainLooperThread();

		if (u == null) {
			ArrayList<SKYScreenHolder> arrayList = screenHelper().getActivities();

			if (arrayList != null && arrayList.size() > 0) {
				for (int i = arrayList.size() - 1; i >= 0; i--) {
					Fragment skyFragment = arrayList.get(i).getActivity().getSupportFragmentManager().findFragmentByTag(uiClazz.getName());
					if (skyFragment != null && skyFragment instanceof SKYFragment) {
						if (isMain) {
							SKYBiz skyBiz = (SKYBiz) ((SKYFragment) skyFragment).model();
							u = (U) skyBiz.ui();
						} else {
							u = (U) skyFragment;
						}
					} else if (skyFragment != null && skyFragment instanceof SKYDialogFragment) {
						if (isMain) {
							SKYBiz skyBiz = (SKYBiz) ((SKYDialogFragment) skyFragment).model();
							u = (U) skyBiz.ui();
						} else {
							u = (U) skyFragment;
						}
					}
				}
			}
		} else {
			if (isMain) {
				SKYActivity skyactivity = (SKYActivity) u;
				SKYBiz skyBiz = (SKYBiz) skyactivity.model();
				if(skyBiz != null){
					u = (U) skyBiz.ui();
				}
			}
		}

		if (u == null) {
			u = SKYHelper.structureHelper().createNullService(uiClazz);
		}

		return u;
	}

	/**
	 * 业务是否存在
	 *
	 * @param service
	 *            参数
	 * @param <B>
	 *            参数
	 * @return true 存在 false 不存在
	 */
	public static <B extends SKYBiz> boolean isExist(Class<B> service) {
		return structureHelper().isExist(service);
	}

	/**
	 * 获取业务
	 *
	 * @param service
	 *            参数
	 * @param <B>
	 *            参数
	 * @return 返回值
	 */
	public static <B extends SKYBiz> List<B> bizList(Class<B> service) {
		if (checkBizIsPublic(service)) { // 判定是否是公共方法
			throw new SKYBizException("Class 不能是公共业务类");
		}
		return structureHelper().bizList(service);
	}

	/**
	 * 获取全局上下文
	 *
	 * @return 返回值
	 */
	public static Application getInstance() {
		return mSKYModulesManage.application;
	}

	/**
	 * 获取网络数据
	 *
	 * @param call
	 *            参数
	 * @param <D>
	 *            参数
	 * @return 返回值
	 */
	public static <D> D httpBody(Call<D> call) {
		if (call == null) {
			throw new SKYHttpException("Call 不能为空～");
		}
		Call<D> skyCall;
		if (call.isExecuted()) {
			skyCall = call.clone();
		} else {
			skyCall = call;
		}

		try {
			Response<D> response = skyCall.execute();
			if (!response.isSuccessful()) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("code:");
				stringBuilder.append(response.code());
				stringBuilder.append(" ");
				stringBuilder.append("message:");
				stringBuilder.append(response.message());
				stringBuilder.append(" ");
				stringBuilder.append("errorBody:");
				stringBuilder.append(response.errorBody().string());
				throw new SKYHttpException(stringBuilder.toString());
			}

			return response.body();
		} catch (IOException e) {
			if (SKYHelper.isLogOpen() && e != null) {
				e.printStackTrace();
				L.i(e.getMessage());
			}
			throw new SKYHttpException("网络异常", e.getCause());
		}
	}

	/**
	 * 取消网络请求
	 *
	 * @param call
	 *            参数
	 */
	public static void httpCancel(Call call) {
		if (call == null) {
			return;
		}

		if (call.isExecuted()) {
			call.cancel();
		}
	}

	/**
	 * 是否打印日志
	 *
	 * @return 返回值
	 */
	public static boolean isLogOpen() {
		return mSKYModulesManage.sky.isLogOpen();
	}

	/**
	 * 判断是否是主线程
	 *
	 * @return true 子线程 false 主线程
	 */
	public static boolean isMainLooperThread() {
		return Looper.getMainLooper().getThread() != Thread.currentThread();
	}

	/**
	 * Toast 提示信息
	 *
	 * @return 返回值
	 */
	public static SKYToast toast() {
		return mSKYModulesManage.skyToast;
	}

	/**
	 * 获取网络
	 *
	 * @param httpClazz
	 *            参数
	 * @param <H>
	 *            参数
	 * @return 返回值
	 */
	public static <H> H http(Class<H> httpClazz) {
		return mSKYModulesManage.skyCacheManager.http(httpClazz);

	}

	/**
	 * activity管理器
	 *
	 * @return 管理器
	 */
	public static SKYScreenManager screenHelper() {
		return mSKYModulesManage.skyScreenManager;
	}

	/**
	 * MainLooper 主线程中执行
	 *
	 * @return 返回值
	 */
	public static SynchronousExecutor mainLooper() {
		return mSKYModulesManage.synchronousExecutor;
	}

	/**
	 * 结构管理器
	 *
	 * @return 管理器
	 */
	static SKYStructureIManage structureHelper() {
		return mSKYModulesManage.skyStructureManage;
	}

	/**
	 * SKYThreadPoolManager 线程池管理器
	 *
	 * @return 返回值
	 */
	static SKYThreadPoolManager threadPoolHelper() {
		return mSKYModulesManage.skyThreadPoolManager;
	}

	/**
	 * 获取网络适配器
	 *
	 * @return 返回值
	 */
	static Retrofit httpAdapter() {
		return mSKYModulesManage.retrofit;
	}

	/**
	 * 获取方法代理
	 *
	 * @return 返回值
	 */
	static SKYPlugins methodsProxy() {
		return mSKYModulesManage.skyPlugins;
	}

	/**
	 * 公共视图
	 *
	 * @return 返回值
	 */
	static SKYIViewCommon getComnonView() {
		return mSKYModulesManage.skyiViewCommon;
	}

	/**
	 * 检查是否是公共方法
	 *
	 * @param bizClazz
	 *            biz
	 * @return true 公共业务 false 不是公共业务
	 */
	static boolean checkBizIsPublic(Class bizClazz) {
		boolean isPublic = false;

		if (mSKYModulesManage.provideBizTypes.get(bizClazz.hashCode()) == null) {
			Class genricType = SKYUtils.getClassGenricType(bizClazz, 0);
			if (genricType == null && !bizClazz.isInterface()) { // 表示公共biz
				isPublic = true;
			}
			mSKYModulesManage.provideBizTypes.put(bizClazz.hashCode(), isPublic);
		} else {
			isPublic = mSKYModulesManage.provideBizTypes.get(bizClazz.hashCode());
		}
		return isPublic;
	}
}
