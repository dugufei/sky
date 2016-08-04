package jc.sky.core.plugin;

/**
 * Created by sky on 15/2/24. 请求拦截器- 主要是用来组装请求路径
 */
public interface SKYRequestInterceptor {

	void intercept(RequestFacade request);

	interface RequestFacade {

		/** 添加路径. */
		void addUrl(String value);

		void addEncodedUrl(String value);

		/** 向请求添加标题。这不会取代任何现有的标题. */
		void addHeader(String name, String value);

		/** 添加路径 - 编码格式 utf-8 **/
		void addPathParam(String name, String value);

		/** 添加路径 - 不转换编码 **/
		void addEncodedPathParam(String name, String value);

		/** 添加参数 - 编码格式 utf-8 */
		void addQueryParam(String name, String value);

		/** 添加参数 - 不转换编码格式 */
		void addEncodedQueryParam(String name, String value);
	}

	/**
	 * 默认什么都不做
	 */
	SKYRequestInterceptor NONE			= new SKYRequestInterceptor() {

												@Override public void intercept(RequestFacade request) {
													// Do nothing.
												}
											};

	SKYResponseInterceptor NONE_RESPONSE	= new SKYResponseInterceptor() {

												@Override public void httpInterceptorResults(String name, Object object) {
													// Do nothing.
												}
											};
}
