package jc.sky.core.exception;

/**
 * @创建人 sky
 * @创建时间 16/10/12 上午10:47
 * @类描述
 */
public class SKYHttpException extends RuntimeException {

    public SKYHttpException() {}

    public SKYHttpException(String detailMessage) {
        super(detailMessage);
    }

    public SKYHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public SKYHttpException(Throwable cause) {
        super((cause == null ? null : cause.toString()), cause);
    }
}