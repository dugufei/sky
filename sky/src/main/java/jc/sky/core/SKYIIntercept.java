package jc.sky.core;

import jc.sky.core.exception.SKYHttpException;
import jc.sky.core.exception.SKYNotUIPointerException;

/**
 * @author sky
 * @version 1.0 on 2016-11-30 下午3:12
 * @see SKYIIntercept 错误处理
 */
public interface SKYIIntercept {

	boolean interceptHttpError(SKYHttpException sKYHttpException);

	boolean interceptUIError(SKYNotUIPointerException sKYNotUIPointerException);

	boolean interceptBizError(Throwable throwable);
}
