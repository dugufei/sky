package sky.core.methodModule;

/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午8:44
 * @see SKYIMethodRun
 */
public interface SKYIMethodRun {

	/**
	 * 执行
	 *
	 * @param params
	 *            参数
	 */
	<T> T run(Object... params);
}
