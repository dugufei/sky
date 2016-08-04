package jc.sky.core.plugin;

import java.lang.reflect.Method;

/**
 * @创建人 sky
 * @创建时间 16/1/6
 * @类描述 执行前拦截
 */
public interface BizStartInterceptor {

	<T> void interceptStart(String viewName, Class<T> service, Method method, int interceptor, Object[] objects);

}
