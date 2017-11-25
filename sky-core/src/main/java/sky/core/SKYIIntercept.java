package sky.core;

import sky.core.exception.SKYHttpException;
import sky.core.exception.SKYNotUIPointerException;

/**
 * @author sky
 * @version 1.0 on 2016-11-30 下午3:12
 * @see SKYIIntercept 错误处理
 */
interface SKYIIntercept {

	boolean interceptHttpError(String method, SKYHttpException sKYHttpException);

	boolean interceptUIError(String method, SKYNotUIPointerException sKYNotUIPointerException);

	boolean interceptBizError(String method, Throwable throwable);
}
