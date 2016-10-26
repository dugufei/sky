package jc.sky.core.plugin;

import java.lang.reflect.Method;

/**
 * @author sky
 * @version 版本
 */
public interface BizStartInterceptor {

	<T> void interceptStart(String viewName, Class<T> service, Method method, int interceptor, Object[] objects);

}
