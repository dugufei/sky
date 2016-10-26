package jc.sky.core.plugin;

import java.lang.reflect.Method;

/**
 * @author sky
 * @version 版本
 */
public interface DisplayEndInterceptor {

	<T> void interceptEnd(String viewName, Class<T> service, Method method, int interceptor, String intent, Object[] objects, Object backgroundResult);

}
