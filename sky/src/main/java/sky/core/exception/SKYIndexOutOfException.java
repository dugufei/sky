package sky.core.exception;

/**
 * @author sky
 * @version 版本
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