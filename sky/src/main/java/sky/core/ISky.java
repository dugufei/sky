package sky.core;

import retrofit2.Retrofit;

/**
 * @author sky
 * @version 版本 版本
 */
public interface ISky {

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
	Retrofit.Builder httpAdapter(Retrofit.Builder builder);

	/**
	 * 方法拦截器适配
	 *
	 * @param builder
	 *            参数
	 * @return 返回值
	 */
	SKYPlugins.Builder pluginInterceptor(SKYPlugins.Builder builder);

	/**
	 * 获取配置管理器
	 *
	 * @return 返回值
	 */
	SKYModulesManage modulesManage();

	/**
	 * 默认方法
	 */
	ISky ISKY = new ISky() {

		/**
		 * @return 返回值
		 */
		@Override public boolean isLogOpen() {
			return true;
		}

		/**
		 * @param builder
		 *            参数
		 * @return
		 */
		@Override public Retrofit.Builder httpAdapter(Retrofit.Builder builder) {
			builder.baseUrl("http://www.jincanshen.com");
			return builder;
		}

		/**
		 * @param builder
		 *            参数
		 * @return
		 */
		@Override public SKYPlugins.Builder pluginInterceptor(SKYPlugins.Builder builder) {
			return builder;
		}

		/**
		 * @return
		 */
		@Override public SKYModulesManage modulesManage() {
			return  new SKYModulesManage();
		}

	};
}
