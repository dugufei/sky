package sk;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 上午11:32
 * @see SKCoreUtils 核心静态库
 */
final class SKCoreUtils {

	static Class getClassGenricType(final Class clazz, final int index) {
		Type type = clazz.getGenericSuperclass();

		if (!(type instanceof ParameterizedType)) {
			return null;
		}
		// 强制类型转换
		ParameterizedType pType = (ParameterizedType) type;

		Type[] tArgs = pType.getActualTypeArguments();

		if (tArgs.length < 1) {
			return null;
		}

		return (Class) tArgs[index];
	}
}
