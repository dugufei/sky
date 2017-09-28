package jc.sky.modules.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYAppUtil;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.core.SKYIBiz;
import jc.sky.core.SKYICommonBiz;
import jc.sky.display.SKYIDisplay;
import jc.sky.modules.log.L;
import jc.sky.modules.methodProxy.SKYProxy;

/**
 * @author sky
 * @version 版本
 */
public final class CacheManager implements ICacheManager {

	private final static int							TYPE_HTTP		= 1;						// 网络

	private final static int							TYPE_COMMON		= 2;						// 公共

	private final static int							TYPE_IMPL		= 3;						// 实现

	private final static int							TYPE_DISPLAY	= 4;						// 跳转调度

	private final static int							TYPE_BIZ		= 5;						// 业务

	private final LoadingCache<Class<?>, Object>		cache;

	private final ConcurrentHashMap<Class<?>, Integer>	keyType			= new ConcurrentHashMap();

	public CacheManager() {
		// CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
		cache = CacheBuilder.newBuilder()
				// 设置并发级别为10，并发级别是指可以同时写缓存的线程数
				.concurrencyLevel(10)
				// 设置写缓存后30秒过期
				.expireAfterAccess(30, TimeUnit.SECONDS)
				// 设置缓存容器的初始容量为10
				.initialCapacity(10)
				// 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
				.maximumSize(100)
				// 设置要统计缓存的命中率
				.recordStats()
				// build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
				.build(new CacheLoader<Class<?>, Object>() {

					@Override public Object load(Class<?> key) throws Exception {

						int type = keyType.get(key);

						switch (type) {
							case TYPE_HTTP:
								SKYCheckUtils.validateServiceInterface(key);

								Object http = SKYHelper.httpAdapter().create(key);
								if (SKYHelper.isLogOpen()) {
									L.tag("SkyCacheManager");
									StringBuilder stringBuilder = new StringBuilder();
									stringBuilder.append("Http加载成功:");
									stringBuilder.append(key.getName());
									L.i(stringBuilder.toString());
								}
								return http;
							case TYPE_IMPL:
								SKYCheckUtils.validateServiceInterface(key);

								Object skyImpl = SKYHelper.methodsProxy().createImpl(key, SKYAppUtil.getImplClass(key));
								if (SKYHelper.isLogOpen()) {
									L.tag("SkyCacheManager");
									StringBuilder stringBuilder = new StringBuilder();
									stringBuilder.append("Interface加载成功:");
									stringBuilder.append(key.getName());
									L.i(stringBuilder.toString());
								}
								return skyImpl;
							case TYPE_COMMON:
								SKYCheckUtils.validateServiceInterface(key);

								SKYProxy skyCommon = SKYHelper.methodsProxy().create(key, SKYAppUtil.getImplClass(key));
								if (SKYHelper.isLogOpen()) {
									L.tag("SkyCacheManager");
									StringBuilder stringBuilder = new StringBuilder();
									stringBuilder.append("Common加载成功:");
									stringBuilder.append(key.getName());
									L.i(stringBuilder.toString());
								}
								return skyCommon;
							case TYPE_DISPLAY:
								SKYCheckUtils.validateServiceInterface(key);

								SKYProxy skyDisplay = SKYHelper.methodsProxy().createDisplay(key, SKYAppUtil.getImplClass(key));
								if (SKYHelper.isLogOpen()) {
									L.tag("SkyCacheManager");
									StringBuilder stringBuilder = new StringBuilder();
									stringBuilder.append("Display加载成功:");
									stringBuilder.append(key.getName());
									L.i(stringBuilder.toString());
								}
								return skyDisplay;
							case TYPE_BIZ:
								SKYProxy skyProxy;
								if (key.isInterface()) {
									skyProxy = SKYHelper.methodsProxy().create(key, SKYAppUtil.getImplClass(key));
								} else {
									skyProxy = SKYHelper.methodsProxy().createNotInf(key, SKYAppUtil.getImplClassNotInf(key));
								}
								if (SKYHelper.isLogOpen()) {
									L.tag("SkyCacheManager");
									StringBuilder stringBuilder = new StringBuilder();
									stringBuilder.append("Biz加载成功:");
									stringBuilder.append(key.getName());
									L.i(stringBuilder.toString());
								}
								return skyProxy;
						}
						return null;
					}
				});
	}

	@Override public <D extends SKYIDisplay> D display(Class<D> displayClazz) {
		try {
			keyType.put(displayClazz, TYPE_DISPLAY);
			SKYProxy skyProxy = (SKYProxy) cache.get(displayClazz);
			return (D) skyProxy.proxy;
		} catch (ExecutionException e) {
			keyType.remove(displayClazz);
			if (SKYHelper.isLogOpen()) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override public <B extends SKYICommonBiz> B common(Class<B> service) {
		try {
			keyType.put(service, TYPE_COMMON);
			SKYProxy skyProxy = (SKYProxy) cache.get(service);
			return (B) skyProxy.proxy;
		} catch (ExecutionException e) {
			keyType.remove(service);
			if (SKYHelper.isLogOpen()) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override public <B extends SKYIBiz> B biz(Class<B> service) {
		try {
			keyType.put(service, TYPE_BIZ);
			SKYProxy skyProxy = (SKYProxy) cache.get(service);
			return (B) skyProxy.proxy;
		} catch (ExecutionException e) {
			keyType.remove(service);
			if (SKYHelper.isLogOpen()) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override public <I> I interfaces(Class<I> implClazz) {
		try {
			keyType.put(implClazz, TYPE_IMPL);
			return (I) cache.get(implClazz);
		} catch (ExecutionException e) {
			keyType.remove(implClazz);
			if (SKYHelper.isLogOpen()) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override public <H> H http(Class<H> httpClazz) {
		try {
			keyType.put(httpClazz, TYPE_HTTP);
			return (H) cache.get(httpClazz);
		} catch (ExecutionException e) {
			keyType.remove(httpClazz);
			if (SKYHelper.isLogOpen()) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override public void printState() {
		L.tag("SkyCacheManager");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("命中率:");
		stringBuilder.append(cache.stats().toString());
		L.i(stringBuilder.toString());
	}
}
