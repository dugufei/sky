package jc.sky.modules.cache;

import jc.sky.core.SKYBiz;
import jc.sky.core.SKYIBiz;
import jc.sky.core.SKYICommonBiz;
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
	 * 公共方法
	 * 
	 * @param service
	 *            参数
	 * @param <B>
	 *            参数
	 * @return 返回值
	 */
	<B extends SKYICommonBiz> B common(Class<B> service);

	/**
	 * 业务方法
	 * 
	 * @param service 参数
	 * @param <B> 参数
	 * @return 返回值
	 */
	<B extends SKYIBiz> B biz(Class<B> service);

	/**
	 * 接口注解@Impl
	 * 
	 * @param implClazz
	 *            参数
	 * @param <I>
	 *            参数
	 * @return 返回值
	 */
	<I> I interfaces(Class<I> implClazz);

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
