package jc.sky.common.utils;

import jc.sky.core.exception.SKYArgumentException;
import jc.sky.core.exception.SKYIndexOutOfException;
import jc.sky.core.exception.SKYNullPointerException;
import jc.sky.core.exception.SKYUINullPointerException;

/**
 * @创建人 sky
 * @创建时间 15/7/15 上午10:05
 * @类描述 检查
 */
public final class SKYCheckUtils {

	/**
	 * 验证
	 *
	 * @param service
	 * @param <T>
	 */
	public static <T> void validateServiceInterface(Class<T> service) {
		if (!service.isInterface()) {
			throw new IllegalArgumentException("该类不是接口");
		}
	}

	/**
	 * 检查是否为空
	 *
	 * @param reference
	 *
	 * @param errorMessageTemplate
	 * @return
	 */
	public static <T> T checkNotNull(T reference, String errorMessageTemplate) {
		if (reference == null) {
			throw new SKYNullPointerException(errorMessageTemplate);
		}
		return reference;
	}

	/**
	 * 检查是否为空
	 *
	 * @param reference
	 *
	 * @param errorMessageTemplate
	 * @return
	 */
	public static <T> T checkUINotNull(T reference, String errorMessageTemplate) {
		if (reference == null) {
			throw new SKYUINullPointerException(errorMessageTemplate);
		}
		return reference;
	}

	/**
	 * 检查参数
	 *
	 * @param expression
	 * @param errorMessageTemplate
	 */
	public static void checkArgument(boolean expression, String errorMessageTemplate) {
		if (!expression) {
			throw new SKYArgumentException(errorMessageTemplate);
		}
	}

	/**
	 * 检查是否越界
	 *
	 * @param index
	 * @param size
	 * @param desc
	 */
	public static void checkPositionIndex(int index, int size, String desc) {
		if (index < 0 || index > size) {
			throw new SKYIndexOutOfException(desc);
		}
	}

	/**
	 * 判断是否相同
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equal(Object a, Object b) {
		return a == b || (a != null && a.equals(b));
	}

	/**
	 * 判断是否为空
	 *
	 * @param text
	 * @return
	 */
	public static boolean isEmpty(CharSequence text) {
		return null == text || text.length() == 0;
	}
}