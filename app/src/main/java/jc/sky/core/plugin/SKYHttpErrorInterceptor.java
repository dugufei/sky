package jc.sky.core.plugin;

import java.lang.reflect.Method;

import jc.sky.core.exception.SKYHttpException;

/**
 * @author sky
 * @version 版本
 */
public interface SKYHttpErrorInterceptor {

	<T> void methodError(Class<T> service, Method method, int interceptor, SKYHttpException skyHttpException);
}
