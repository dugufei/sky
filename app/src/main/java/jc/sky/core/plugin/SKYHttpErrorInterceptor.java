package jc.sky.core.plugin;

import java.lang.reflect.Method;

import jc.sky.core.exception.SKYHttpException;

/**
 * @创建人 sky
 * @创建时间 16/1/6
 * @类描述 网络错误异常
 */
public interface SKYHttpErrorInterceptor {

	<T> void methodError(Class<T> service, Method method, int interceptor, SKYHttpException skyHttpException);
}
