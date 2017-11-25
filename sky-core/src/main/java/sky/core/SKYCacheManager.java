package sky.core;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static sky.core.SKYUtils.validateServiceInterface;


/**
 * @author sky
 * @version 版本
 */
final class SKYCacheManager {

	private final static int							TYPE_HTTP		= 1;						// 网络

	private final static int							TYPE_DISPLAY	= 2;						// 跳转调度

	private final static int							TYPE_BIZ		= 3;						// 业务

	private final LoadingCache<Class<?>, Object> cache;

	private final ConcurrentHashMap<Class<?>, Integer>	keyType			= new ConcurrentHashMap();

	SKYCacheManager() {
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
								validateServiceInterface(key);

								Object http = SKYHelper.httpAdapter().create(key);
								if (SKYHelper.isLogOpen()) {
									L.tag("SkyCacheManager");
									StringBuilder stringBuilder = new StringBuilder();
									stringBuilder.append("Http加载成功:");
									stringBuilder.append(key.getName());
									L.i(stringBuilder.toString());
								}
								return http;
							case TYPE_DISPLAY:
								validateServiceInterface(key);

								SKYProxy skyDisplay = SKYHelper.structureHelper().createDisplay(key, SKYUtils.getImplClass(key));
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
									skyProxy = SKYHelper.structureHelper().create(key, SKYUtils.getImplClass(key));
								} else {
									skyProxy = SKYHelper.structureHelper().createNotInf(key, SKYUtils.getImplClassNotInf(key));
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

	<D extends SKYIDisplay> D display(Class<D> displayClazz) {
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

	<B extends SKYBiz> B biz(Class<B> service) {
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

	<H> H http(Class<H> httpClazz) {
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

	void printState() {
		L.tag("SkyCacheManager");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("命中率:");
		stringBuilder.append(cache.stats().toString());
		L.i(stringBuilder.toString());
	}
}
