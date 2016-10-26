package jc.sky.core.exception;

/**
 * @author sky
 * @version 版本
 */
public class SKYArgumentException extends SKYBizException {

	public SKYArgumentException() {}

	public SKYArgumentException(String detailMessage) {
		super(detailMessage);
	}

	public SKYArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public SKYArgumentException(Throwable cause) {
		super((cause == null ? null : cause.toString()), cause);
	}
}