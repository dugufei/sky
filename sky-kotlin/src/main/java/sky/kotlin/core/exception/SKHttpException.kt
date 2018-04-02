package sky.kotlin.core.exception

/**
 * @author sky
 * @version 版本
 */
class SKHttpException : RuntimeException {

    constructor() {}

    constructor(detailMessage: String) : super(detailMessage) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(cause: Throwable?) : super(cause?.toString(), cause) {}
}