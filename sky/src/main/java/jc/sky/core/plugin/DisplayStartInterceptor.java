package jc.sky.core.plugin;

import android.os.Bundle;

import java.lang.reflect.Method;

/**
 * @author sky
 * @version 版本
 */
public interface DisplayStartInterceptor {

	<T> boolean interceptStart(String viewName, Class<T> service, Method method, int interceptor,String intent, Bundle bundle);

}
