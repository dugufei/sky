package jc.sky.core.exception;

/**
 * @创建人 sky
 * @创建时间 15/7/13 上午1:12
 * @类描述 参数异常
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