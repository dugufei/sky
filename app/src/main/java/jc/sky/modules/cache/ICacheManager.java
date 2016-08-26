package jc.sky.modules.cache;

import jc.sky.core.SKYICommonBiz;
import jc.sky.display.SKYIDisplay;

/**
 * @创建人 sky
 * @创建时间 16/8/24 下午7:28
 * @类描述 缓存接口
 */
public interface ICacheManager {

	/**
	 * 调度
	 * 
	 * @param displayClazz
	 * @param <D>
	 * @return
	 */
	<D extends SKYIDisplay> D display(Class<D> displayClazz);

	/**
	 * 公共方法
	 * 
	 * @param service
	 * @param <B>
	 * @return
	 */
	<B extends SKYICommonBiz> B common(Class<B> service);

	/**
	 * 接口注解@Impl
	 * 
	 * @param implClazz
	 * @param <I>
	 * @return
	 */
	<I> I interfaces(Class<I> implClazz);

	/**
	 * 网络
	 * 
	 * @param httpClazz
	 * @param <H>
	 * @return
	 */
	<H> H http(Class<H> httpClazz);

	/**
	 * 打印接口命中率中率
	 */
	void printState();

}
