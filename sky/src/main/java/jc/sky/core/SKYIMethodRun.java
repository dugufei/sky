package jc.sky.core;

import jc.sky.SKYHelper;
import jc.sky.modules.log.L;

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
	void run(Object... params);

	SKYIMethodRun NONE = new SKYIMethodRun() {

		@Override public void run(Object... params) {
			if (SKYHelper.isLogOpen()) {
				L.i("执行空方法");
			}
		}
	};
}
