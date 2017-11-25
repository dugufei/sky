package sky.core.plugins;

import android.os.Bundle;

import java.lang.reflect.Method;

/**
 * @author sky
 * @version 版本
 */
public interface DisplayEndInterceptor {

	<T> void interceptEnd(String viewName, Class<T> service, Method method, int interceptor, String intent, Bundle bundle, Object backgroundResult);

}
