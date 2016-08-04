package jc.sky.core.exception;

/**
 * @创建人 sky
 * @创建时间 15/7/13 上午1:13
 * @类描述 状态异常
 */
public class SKYIndexOutOfException extends SKYBizException {

    public SKYIndexOutOfException() {}

    public SKYIndexOutOfException(String detailMessage) {
        super(detailMessage);
    }

    public SKYIndexOutOfException(String message, Throwable cause) {
        super(message, cause);
    }

    public SKYIndexOutOfException(Throwable cause) {
        super((cause == null ? null : cause.toString()), cause);
    }
}