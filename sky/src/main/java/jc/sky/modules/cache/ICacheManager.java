package jc.sky.modules.cache;

import jc.sky.core.SKYBiz;
import jc.sky.display.SKYIDisplay;

/**
 * @author sky
 * @version 版本
 */
public interface ICacheManager {

	/**
	 * 调度
	 * 
	 * @param displayClazz
	 *            参数
	 * @param <D>
	 *            参数
	 * @return 返回值
	 */
	<D extends SKYIDisplay> D display(Class<D> displayClazz);

	/**
	 * 业务方法
	 * 
	 * @param service 参数
	 * @param <B> 参数
	 * @return 返回值
	 */
	<B extends SKYBiz> B biz(Class<B> service);
	/**
	 * 网络
	 * 
	 * @param httpClazz
	 *            参数
	 * @param <H>
	 *            参数
	 * @return 返回值
	 */
	<H> H http(Class<H> httpClazz);

	/**
	 * 打印接口命中率中率
	 */
	void printState();

}
