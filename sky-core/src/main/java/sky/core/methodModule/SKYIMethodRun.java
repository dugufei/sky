package sky.core.methodModule;

import sky.core.L;
import sky.core.SKYHelper;

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

	SKYIMethodRun NONE = new SKYIMethodRun() {

		@Override public <T> T  run(Object... params) {
			if (SKYHelper.isLogOpen()) {
				L.i("执行空方法");
			}
			return null;
		}
	};
}
