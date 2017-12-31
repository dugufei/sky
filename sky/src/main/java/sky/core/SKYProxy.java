package sky.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sky
 * @version 版本
 */
class SKYProxy {

    public Object impl;                                // 实现类

    public Object proxy;                                // 代理类

    public ConcurrentHashMap<String, SKYMethod> methodCache = new ConcurrentHashMap();    // 方法缓存


    /**
     * 清空
     */
    public void clearProxy() {
        impl = null;
        proxy = null;
        methodCache.clear();
        methodCache = null;
    }
}
