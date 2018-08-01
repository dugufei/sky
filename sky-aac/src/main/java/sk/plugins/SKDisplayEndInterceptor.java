package sk.plugins;

import java.lang.reflect.Method;

import android.os.Bundle;

/**
 * @author sky
 * @version 版本
 */
public interface SKDisplayEndInterceptor {

	<T> void interceptEnd(String viewName, Class<T> service, Method method, int interceptor, String intent, Bundle bundle, Object backgroundResult);

}
