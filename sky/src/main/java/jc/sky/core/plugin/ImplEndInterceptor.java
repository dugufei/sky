package jc.sky.core.plugin;

import java.lang.reflect.Method;

/**
 * @author sky
 * @version 版本
 */
public interface ImplEndInterceptor {
    <T> void interceptEnd(String viewName, Class<T> service, Method method, Object[] objects, Object backgroundResult);
}
