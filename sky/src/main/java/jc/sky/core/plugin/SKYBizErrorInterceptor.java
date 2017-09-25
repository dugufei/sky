package jc.sky.core.plugin;

import jc.sky.core.SKYBiz;

/**
 * @author sky
 * @version 版本
 */
public interface SKYBizErrorInterceptor {

    void interceptorError(SKYBiz skyBiz, Throwable throwable);
}
