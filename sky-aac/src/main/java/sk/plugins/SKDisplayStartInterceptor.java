package sk.plugins;

import java.lang.reflect.Method;

import android.os.Bundle;

/**
 * @author sky
 * @version 版本
 */
public interface SKDisplayStartInterceptor {

	<T> boolean interceptStart(String viewName, Class<T> service, Method method, int interceptor, String intent, Bundle bundle);

}
