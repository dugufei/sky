package jc.sky.core.exception;

/**
 * @创建人 sky
 * @创建时间 15/7/13 上午1:10
 * @类描述
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