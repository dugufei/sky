package sk.proxy;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sky
 * @version 版本
 */
public class SKProxy {

	public Class								bizClass;								// 类

	public Object								impl;									// 实现类

	public Object								proxy;									// 代理类

	public ConcurrentHashMap<String, SKMethod>	methodCache	= new ConcurrentHashMap();	// 方法缓存

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
