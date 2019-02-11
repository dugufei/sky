package sk;

import retrofit2.Retrofit;

/**
 * @author sk
 * @version
 */
public interface ISK {

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
	SKInterceptor.Builder pluginInterceptor(SKInterceptor.Builder builder);

	/**
	 * 获取配置管理器
	 *
	 * @return 返回值
	 */
	SKDefaultManager manage();

	/**
	 * 默认方法
	 */
	ISK NONE = new ISK() {

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

		@Override public SKInterceptor.Builder pluginInterceptor(SKInterceptor.Builder builder) {
			return builder;
		}

		/**
		 * @return
		 */
		@Override public SKDefaultManager manage() {
			return new SKDefaultManager();
		}

	};
}
