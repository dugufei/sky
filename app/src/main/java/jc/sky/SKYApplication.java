package jc.sky;

import android.app.Application;
import android.content.Context;

import jc.sky.modules.SKYModulesManage;
import jc.sky.modules.methodProxy.SKYMethods;
import jc.sky.view.common.SKYIViewCommon;
import retrofit2.Retrofit;

/**
 * Created by sky on 15/1/26. 说明：使用架构必须继承
 */
public abstract class SKYApplication extends Application implements SKYIViewCommon {

	/**
	 * modules 管理
	 */
	SKYModulesManage mSKYModulesManage	= null;

	/**
	 * 日志是否打印
	 * 
	 * @return true 打印 false 不打印
	 */
	public abstract boolean isLogOpen();

	/**
	 * 获取网络适配器
	 *
	 * @return
	 */
	public abstract Retrofit getRestAdapter(Retrofit.Builder builder);

	/**
	 * 方法拦截器适配
	 * 
	 * @param builder
	 * @return
	 */
	public abstract SKYMethods getMethodInterceptor(SKYMethods.Builder builder);

	/**
	 * 获取配置管理器
	 * 
	 * @return
	 */
	public SKYModulesManage getModulesManage() {
		return new SKYModulesManage(this);
	}

	/**
	 * 初始化帮助类
	 * 
	 * @param SKYModulesManage
	 */
	public void initHelper(SKYModulesManage SKYModulesManage) {
		SKYHelper.with(SKYModulesManage);
	}

	/**
	 * 设置全局异常处理
	 * 
	 * @return
	 */
	public Thread.UncaughtExceptionHandler getExceptionHandler() {
		return Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		// 初始化
		mSKYModulesManage = getModulesManage();
		// 初始化Application
		initHelper(mSKYModulesManage);
		// 初始化 HTTP
		mSKYModulesManage.initSKYRestAdapter(getRestAdapter(new Retrofit.Builder()));
		// 初始化 LOG
		mSKYModulesManage.initLog(isLogOpen());
		// 初始化 代理方法
		mSKYModulesManage.initMehtodProxy(getMethodInterceptor(new SKYMethods.Builder()));
		// 初始化统一错误处理
		Thread.setDefaultUncaughtExceptionHandler(getExceptionHandler());
	}
}
