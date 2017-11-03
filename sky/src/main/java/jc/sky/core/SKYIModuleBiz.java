package jc.sky.core;

/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午8:44
 * @see SKYIModuleBiz
 */
public interface SKYIModuleBiz {

	/**
	 * 执行
	 *
	 * @param method
	 *            方法名
	 * @return 返回值
	 */

	SKYIMethodRun method(String method);

	SKYIModuleBiz NONE = new SKYIModuleBiz() {

		@Override public SKYIMethodRun method(String method) {
			return SKYIMethodRun.NONE;
		}
	};
}
