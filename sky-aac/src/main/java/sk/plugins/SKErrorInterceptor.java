package sk.plugins;

import java.lang.reflect.Method;

import sk.SKErrorEnum;

/**
 * @author sky
 * @version 版本
 */
public interface SKErrorInterceptor<U> {

	void interceptorError(Method method, Object clazz, Object[] objects, int interceptor, SKErrorEnum skErrorEnum);
}
