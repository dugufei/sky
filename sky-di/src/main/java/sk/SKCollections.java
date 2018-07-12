package sk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @author sky
 * @version 1.0 on 2018-06-28 上午10:24
 * @see SKCollections
 */
public final class SKCollections {

	private static final int MAX_POWER_OF_TWO = 1 << (Integer.SIZE - 2);

	private SKCollections() {}

	public static <T> List<T> presizedList(int size) {
		if (size == 0) {
			return Collections.emptyList();
		}
		return new ArrayList<T>(size);
	}

	public static boolean hasDuplicates(List<?> list) {
		if (list.size() < 2) {
			return false;
		}
		Set<Object> asSet = new HashSet<Object>(list);
		return list.size() != asSet.size();
	}

	static <T> HashSet<T> newHashSetWithExpectedSize(int expectedSize) {
		return new HashSet<T>(calculateInitialCapacity(expectedSize));
	}

	static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
		return new LinkedHashMap<K, V>(calculateInitialCapacity(expectedSize));
	}

	private static int calculateInitialCapacity(int expectedSize) {
		if (expectedSize < 3) {
			return expectedSize + 1;
		}
		if (expectedSize < MAX_POWER_OF_TWO) {
			// This is the calculation used in JDK8 to resize when a putAll
			// happens; it seems to be the most conservative calculation we
			// can make. 0.75 is the default load factor.
			return (int) (expectedSize / 0.75F + 1.0F);
		}
		return Integer.MAX_VALUE; // any large value
	}
}
