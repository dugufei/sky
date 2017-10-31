package jc.sky.core.plugin;

import jc.sky.core.SKYBiz;
import jc.sky.core.exception.SKYHttpException;

/**
 * @author sky
 * @version 版本
 */
public interface SKYHttpErrorInterceptor {

	void methodError(SKYBiz skyBiz, String method, SKYHttpException skyHttpException);
}
