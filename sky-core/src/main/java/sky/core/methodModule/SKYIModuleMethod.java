package sky.core.methodModule;

/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午8:44
 * @see SKYIModuleMethod
 */
public interface SKYIModuleMethod {

	/**
	 * 获取方法
	 *
	 * @param method
	 *            方法名
	 * @return 返回值
	 */

	SKYIMethodRun method(String method);

	SKYIModuleMethod NONE = new SKYIModuleMethod() {

		@Override public SKYIMethodRun method(String method) {
			return SKYIMethodRun.NONE;
		}
	};
}
