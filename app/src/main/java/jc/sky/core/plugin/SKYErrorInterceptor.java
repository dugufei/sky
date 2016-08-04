package jc.sky.core.plugin;

import java.lang.reflect.Method;

/**
 * @创建人 sky
 * @创建时间 16/1/6
 * @类描述 错误拦截
 */
public interface SKYErrorInterceptor {

	<T> void interceptorError(String viewName,Class<T> service, Method method, int interceptor, Throwable throwable);

}
