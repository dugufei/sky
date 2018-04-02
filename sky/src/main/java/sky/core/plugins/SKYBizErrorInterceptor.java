package sky.core.plugins;

/**
 * @author sky
 * @version 版本
 */
public interface SKYBizErrorInterceptor<U> {

	void interceptorError(U view, int interceptor, Throwable sKYBizException);

}
