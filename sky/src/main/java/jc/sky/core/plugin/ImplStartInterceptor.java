package jc.sky.core.plugin;

import java.lang.reflect.Method;

/**
 * @author sky
 * @version 版本
 */
public interface ImplStartInterceptor {
    <T> void interceptStart(String viewName, Class<T> service, Method method, Object[] objects);

}
