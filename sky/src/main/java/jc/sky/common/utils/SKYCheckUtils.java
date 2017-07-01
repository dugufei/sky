package jc.sky.common.utils;

import jc.sky.core.exception.SKYArgumentException;
import jc.sky.core.exception.SKYIndexOutOfException;
import jc.sky.core.exception.SKYNullPointerException;
import jc.sky.core.exception.SKYUINullPointerException;

/**
 * @author sky
 * @version 版本
 */
public final class SKYCheckUtils {

	/**
	 * 验证
	 * 
	 * @param <T>
	 *            参数
	 *
	 * @param service
	 *            参数
	 * @param <T>
	 *            参数
	 */
	public static <T> void validateServiceInterface(Class<T> service) {
		if (!service.isInterface()) {
			throw new IllegalArgumentException("该类不是接口");
		}
	}

	/**
	 * 检查是否为空
	 * 
	 * @param <T>
	 *            参数
	 *
	 * @param reference
	 *            参数
	 *
	 * @param errorMessageTemplate
	 *            参数
	 * @return 返回值
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
	 * @param <T>
	 *            参数
	 * @param reference
	 *            参数
	 *
	 * @param errorMessageTemplate
	 *            参数
	 * @return 返回值
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
	 *            参数
	 * @param errorMessageTemplate
	 *            参数
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
	 *            参数
	 * @param size
	 *            参数
	 * @param desc
	 *            参数
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
	 *            参数
	 * @param b
	 *            参数
	 * @return 返回值
	 */
	public static boolean equal(Object a, Object b) {
		return a == b || (a != null && a.equals(b));
	}

	/**
	 * 判断是否为空
	 *
	 * @param text
	 *            参数
	 * @return 返回值
	 */
	public static boolean isEmpty(CharSequence text) {
		return null == text || text.length() == 0;
	}
}