package jc.sky.core.exception;

/**
 * @author sky
 * @version 版本
 */
public class SKYBizException extends RuntimeException {

	public SKYBizException() {}

	public SKYBizException(String detailMessage) {
		super(detailMessage);
	}

	public SKYBizException(String message, Throwable cause) {
		super(message, cause);
	}

	public SKYBizException(Throwable cause) {
		super((cause == null ? null : cause.toString()), cause);
	}
}