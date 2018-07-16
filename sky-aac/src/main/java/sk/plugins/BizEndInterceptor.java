package sk.plugins;

import java.lang.reflect.Method;

/**
 * @author sky
 * @version 版本
 */
public interface BizEndInterceptor {

	<T> void interceptEnd(String viewName, Class<T> service, Method method, int interceptor, Object[] objects, Object backgroundResult);

}
