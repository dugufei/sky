package jc.sky.core;

import jc.sky.modules.SKYModulesManage;
import jc.sky.modules.methodProxy.SKYMethods;
import retrofit2.Retrofit;

/**
 * @author sky
 * @version 版本 版本
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
	 * @param builder
	 *            参数
	 * @return 返回值 返回值
	 */
	Retrofit getRestAdapter(Retrofit.Builder builder);

	/**
	 * 方法拦截器适配
	 *
	 * @param builder
	 *            参数
	 * @return 返回值
	 */
	SKYMethods getMethodInterceptor(SKYMethods.Builder builder);

	/**
	 * 获取配置管理器
	 *
	 * @return 返回值
	 */
	SKYModulesManage getModulesManage();

	/**
	 * 默认方法
	 */
	ISKYBind ISKY_BIND = new ISKYBind() {

		/**
		 * @return 返回值
		 */
		@Override public boolean isLogOpen() {
			return true;
		}

		/**
		 * @param builder
		 *            参数
		 * @return 返回值
		 */
		@Override public Retrofit getRestAdapter(Retrofit.Builder builder) {
			builder.baseUrl("http://www.jincanshen.com");
			return builder.build();
		}

		/**
		 * @param builder
		 *            参数
		 * @return 返回值
		 */
		@Override public SKYMethods getMethodInterceptor(SKYMethods.Builder builder) {
			return builder.build();
		}

		/**
		 * @return 返回值
		 */
		@Override public SKYModulesManage getModulesManage() {
			return new SKYModulesManage();
		}
	};
}
