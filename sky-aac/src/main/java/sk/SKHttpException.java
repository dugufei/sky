package sk;

/**
 * @author sky
 * @version 版本
 */
public class SKHttpException extends RuntimeException {

    public SKHttpException() {}

    public SKHttpException(String detailMessage) {
        super(detailMessage);
    }

    public SKHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public SKHttpException(Throwable cause) {
        super((cause == null ? null : cause.toString()), cause);
    }
}