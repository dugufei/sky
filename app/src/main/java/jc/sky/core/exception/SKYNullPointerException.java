package jc.sky.core.exception;

/**
 * @创建人 sky
 * @创建时间 15/7/13 上午1:13
 * @类描述 空指针异常
 */
public class SKYNullPointerException extends SKYBizException {

	public SKYNullPointerException() {}

	public SKYNullPointerException(String detailMessage) {
		super(detailMessage);
	}
}