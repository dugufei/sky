package sky.core;

import sky.core.exception.SKYHttpException;
import sky.core.exception.SKYNotUIPointerException;

/**
 * @author sky
 * @version 1.0 on 2016-11-30 下午3:12
 * @see SKYIIntercept 错误处理
 */
interface SKYIIntercept {

	boolean interceptHttpError(int interceptor, SKYHttpException sKYHttpException);

	boolean interceptUIError( int interceptor, SKYNotUIPointerException sKYNotUIPointerException);

	boolean interceptBizError(int interceptor, Throwable throwable);
}
