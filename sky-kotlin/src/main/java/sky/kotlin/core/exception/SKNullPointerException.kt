package sky.kotlin.core.exception

/**
 * @author sky
 * @version 版本
 */
class SKNullPointerException : SKBizException {

    constructor() {}

    constructor(detailMessage: String) : super(detailMessage) {}
}