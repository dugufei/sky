package jc.sky.core.plugin;


/**
 * @author sky
 * @version 版本
 */
public interface SKYBizErrorInterceptor {

	void interceptorError(Class view, String method, Throwable throwable);
}
