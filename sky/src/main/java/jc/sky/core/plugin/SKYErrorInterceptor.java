package jc.sky.core.plugin;

import java.lang.reflect.Method;

/**
 * @author sky
 * @version 版本
 */
public interface SKYErrorInterceptor {

	<T> void interceptorError(String viewName,Class<T> service, Method method, int interceptor, Throwable throwable);

}
