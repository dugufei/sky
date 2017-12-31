package sky.core.plugins;


import sky.core.exception.SKYHttpException;

/**
 * @author sky
 * @version 版本
 */
public interface SKYHttpErrorInterceptor {

	void methodError(Class view, String method, SKYHttpException skyHttpException);
}
