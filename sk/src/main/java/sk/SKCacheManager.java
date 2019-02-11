package sk;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import static sk.utils.SKUtils.validateServiceInterface;

/**
 * @author sky
 * @version 版本
 */
final class SKCacheManager {

	private final static int							TYPE_HTTP	= 1;						// 网络

	private final LoadingCache<Class<?>, Object>		cache;

	private final ConcurrentHashMap<Class<?>, Integer>	keyType		= new ConcurrentHashMap();

	SKCacheManager() {
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
								Object http = SKHelper.httpAdapter().create(key);
								if (SKHelper.isLogOpen()) {
									L.tag("SkCacheManager");
									StringBuilder stringBuilder = new StringBuilder();
									stringBuilder.append("Http加载成功:");
									stringBuilder.append(key.getName());
									L.i(stringBuilder.toString());
								}
								return http;
						}
						return null;
					}
				});
	}

	<H> H http(Class<H> httpClazz) {
		try {
			keyType.put(httpClazz, TYPE_HTTP);
			return (H) cache.get(httpClazz);
		} catch (ExecutionException e) {
			keyType.remove(httpClazz);
			if (SKHelper.isLogOpen()) {
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
