package sk.plugins;

/**
 * @author sky
 * @version 版本
 */
public interface SKErrorInterceptor<U> {

	void interceptorError(U view, int interceptor, Throwable sKYBizException);

}
