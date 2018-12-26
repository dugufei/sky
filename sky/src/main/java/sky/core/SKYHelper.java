package sky.core;

import android.app.Application;
import android.os.Looper;
import android.support.v4.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sky.core.exception.SKYBizException;
import sky.core.exception.SKYHttpException;
import sky.core.exception.SKYUINullPointerException;
import sky.core.methodModule.SKYIMethodRun;
import sky.core.modules.download.SKYDownloadManager;
import sky.core.modules.file.SKYFileCacheManage;
import sky.core.modules.job.SKYIJobService;
import sky.core.screen.SKYScreenHolder;
import sky.core.screen.SKYScreenManager;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

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
	public static final Sky newSky() {
		return new Sky();
	}

	/**
	 * 获取管理
	 *
	 * @param <M>
	 *            参数
	 * @return 返回值
	 */
	protected static final <M extends SKYModulesManage> M getManage() {
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
	public static final <D extends SKYIDisplay> D display(Class<D> eClass) {
		return mSKYModulesManage.skyCacheManager.get().display(eClass);
	}



	/**
	 * 任务管理器
	 *
	 * @return 返回值
	 */
	public static final SKYIJobService jobSerceHelper() {
		SKYModulesManage manage = getManage();
		return manage.skyJobService.get();
	}

	/**
	 * 文件缓存管理器
	 *
	 * @return 返回值
	 */
	public static final SKYFileCacheManage fileCacheManage() {
		SKYModulesManage manage = getManage();
		return manage.skyFileCacheManage.get();
	}

	/**
	 * 下载器工具
	 *
	 * @return 返回值
	 */
	public static final SKYDownloadManager downloader() {
		SKYModulesManage manage = getManage();
		return manage.skyDownloadManager.get();
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
	public static final <B extends SKYBiz> B biz(Class<B> service) {
		if (checkBizIsPublic(service)) { // 判定是否是公共方法
			return mSKYModulesManage.skyCacheManager.get().biz(service);
		}
		return structureHelper().biz(service);
	}

	/**
	 * 执行业务代码
	 *
	 * @param code
	 * @return 返回值
	 */
	public static final SKYIMethodRun moduleBiz(int code) {
		return mSKYModulesManage.provideMethodRun.get().get(code);
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
	public static final <U> U ui(Class<U> uiClazz) {
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
				if (skyBiz != null) {
					u = (U) skyBiz.ui();
				}
			}
		}

		if (u == null) {
			if(isMain){
				throw new SKYBizException("is not main thread no create service");
			}else {
				u = SKYHelper.structureHelper().createNullService(uiClazz);
			}
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
	public static final <B extends SKYBiz> boolean isExist(Class<B> service) {
		if (checkBizIsPublic(service)) { // 判定是否是公共方法
			throw new SKYBizException("Class 不能是公共业务类");
		}
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
	public static final <B extends SKYBiz> List<B> bizList(Class<B> service) {
		return structureHelper().bizList(service);
	}

	/**
	 * 获取全局上下文
	 *
	 * @return 返回值
	 */
	public static final Application getInstance() {
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
	public static final <D> D httpBody(Call<D> call) {
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
	public static final void httpCancel(Call call) {
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
	public static final boolean isLogOpen() {
		return mSKYModulesManage.sky.isLogOpen();
	}

	/**
	 * 判断是否是主线程
	 *
	 * @return true 子线程 false 主线程
	 */
	public static final boolean isMainLooperThread() {
		return Looper.getMainLooper().getThread() != Thread.currentThread();
	}

	/**
	 * Toast 提示信息
	 *
	 * @return 返回值
	 */
	public static final SKYToast toast() {
		return mSKYModulesManage.skyToast.get();
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
	public static final <H> H http(Class<H> httpClazz) {
		return mSKYModulesManage.skyCacheManager.get().http(httpClazz);

	}

	/**
	 * activity管理器
	 *
	 * @return 管理器
	 */
	public static final SKYScreenManager screenHelper() {
		return mSKYModulesManage.skyScreenManager.get();
	}

	/**
	 * MainLooper 主线程中执行
	 *
	 * @return 返回值
	 */
	public static final SynchronousExecutor mainLooper() {
		return mSKYModulesManage.synchronousExecutor.get();
	}

	/**
	 * 结构管理器
	 *
	 * @return 管理器
	 */
	static final SKYStructureIManage structureHelper() {
		return mSKYModulesManage.skyStructureManage.get();
	}

	/**
	 * SKYThreadPoolManager 线程池管理器
	 *
	 * @return 返回值
	 */
	static final SKYThreadPoolManager threadPoolHelper() {
		return mSKYModulesManage.skyThreadPoolManager.get();
	}

	/**
	 * 获取网络适配器
	 *
	 * @return 返回值
	 */
	static final Retrofit httpAdapter() {
		return mSKYModulesManage.retrofit.get();
	}

	/**
	 * 获取方法代理
	 *
	 * @return 返回值
	 */
	static final SKYPlugins methodsProxy() {
		return mSKYModulesManage.skyPlugins.get();
	}

	/**
	 * 公共视图
	 *
	 * @return 返回值
	 */
	static final SKYIViewCommon getComnonView() {
		return mSKYModulesManage.skyiViewCommon.get();
	}

	/**
	 * 检查是否是公共方法
	 *
	 * @param bizClazz
	 *            biz
	 * @return true 公共业务 false 不是公共业务
	 */
	static final boolean checkBizIsPublic(Class bizClazz) {
		boolean isPublic = false;

		if (mSKYModulesManage.provideBizTypes.get().get(bizClazz.hashCode()) == null) {
			Class genricType = SKYUtils.getClassGenricType(bizClazz, 0);
			if (genricType == null && !bizClazz.isInterface()) { // 表示公共biz
				isPublic = true;
			}
			mSKYModulesManage.provideBizTypes.get().put(bizClazz.hashCode(), isPublic);
		} else {
			isPublic = mSKYModulesManage.provideBizTypes.get().get(bizClazz.hashCode());
		}
		return isPublic;
	}
}
