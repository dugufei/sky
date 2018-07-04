package sky.di;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static sky.di.SKCollections.newLinkedHashMapWithExpectedSize;

/**
 * @author sky
 * @version 1.0 on 2018-06-28 上午10:21
 * @see SKMapBuilder
 */
public final class SKMapBuilder<K, V> {

	private final Map<K, V> contributions;

	private SKMapBuilder(int size) {
		contributions = newLinkedHashMapWithExpectedSize(size);
	}

	public static <K, V> SKMapBuilder<K, V> newMapBuilder(int size) {
		return new SKMapBuilder<>(size);
	}

	public SKMapBuilder<K, V> put(K key, V value) {
		contributions.put(key, value);
		return this;
	}

	public Map<K, V> build() {
		switch (contributions.size()) {
			case 0:
				return Collections.emptyMap();
			default:
				return Collections.unmodifiableMap(contributions);
		}
	}
}