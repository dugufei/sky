package jc.sky.core.exception;

/**
 * @author sky
 * @version 版本
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