package sky.core.plugins;

import sky.core.exception.SKYHttpException;

/**
 * @author sky
 * @version 版本
 */
public interface SKYHttpErrorInterceptor<U> {

	void interceptorError(U view, int interceptor, SKYHttpException skyHttpException);
}
