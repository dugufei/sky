package sk.utils;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 上午11:26
 * @see SKPreconditions
 */
public final class SKPreconditions {

	public static <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		}
		return reference;
	}

	public static <T> T checkNotNull(T reference, String errorMessage) {
		if (reference == null) {
			throw new NullPointerException(errorMessage);
		}
		return reference;
	}

	public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object errorMessageArg) {
		if (reference == null) {
			if (!errorMessageTemplate.contains("%s")) {
				throw new IllegalArgumentException("错误消息模板没有格式说明符");
			}
			if (errorMessageTemplate.indexOf("%s") != errorMessageTemplate.lastIndexOf("%s")) {
				throw new IllegalArgumentException("错误消息模板具有多个格式说明符");
			}
			String argString = errorMessageArg instanceof Class ? ((Class) errorMessageArg).getCanonicalName() : String.valueOf(errorMessageArg);
			throw new NullPointerException(errorMessageTemplate.replace("%s", argString));
		}
		return reference;
	}

	private SKPreconditions() {}
}
