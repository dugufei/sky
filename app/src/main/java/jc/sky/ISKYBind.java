package jc.sky;

import android.app.Application;

import jc.sky.modules.SKYModulesManage;
import jc.sky.modules.methodProxy.SKYMethods;
import retrofit2.Retrofit;

/**
 * @创建人 sky
 * @创建时间 16/8/26 下午8:29
 * @类描述 初始化接口
 */
public interface ISKYBind {

	/**
	 * 日志是否打印
	 *
	 * @return true 打印 false 不打印
	 */
	boolean isLogOpen();

	/**
	 * 获取网络适配器
	 *
	 * @return
	 */
	Retrofit getRestAdapter(Retrofit.Builder builder);

	/**
	 * 方法拦截器适配
	 *
	 * @param builder
	 * @return
	 */
	SKYMethods getMethodInterceptor(SKYMethods.Builder builder);

	/**
	 * 获取配置管理器
	 *
	 * @return
	 */
	SKYModulesManage getModulesManage();
}
