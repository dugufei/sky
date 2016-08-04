package jc.sky.core.exception;

/**
 * @创建人 sky
 * @创建时间 15/7/13 上午1:13
 * @类描述 UI空指针异常
 */
public class SKYUINullPointerException extends SKYBizException {

	public SKYUINullPointerException() {}

	public SKYUINullPointerException(String detailMessage) {
		super(detailMessage);
	}
}