package sk.utils;

/**
 * @author sky
 * @version 1.0 on 2018-07-31 下午1:56
 * @see SKUtils
 */
public final class SKUtils {

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
		if (service.isInterface()) {
			throw new IllegalArgumentException(service.getCanonicalName() + " 该类是接口");
		}
	}
}
