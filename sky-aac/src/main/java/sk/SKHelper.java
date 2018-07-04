package sk;

import sky.di.SKDispatchingInput;
import sky.di.SKInput;

/**
 * @author sky
 * @version 1.0 on 2018-06-13 下午10:53
 * @see SKHelper
 */
public class SKHelper {

	static SKDispatchingInput skDispatchingInput;

	static final void inputDispatching(SKDispatchingInput param) {
		skDispatchingInput = param;
	}

	/**
	 * 公共视图
	 *
	 * @return 返回值
	 */
	static final SKCommonView getComnonView() {
		// return skModules.commonViewSKLazy.get();
		return null;
	}

	public static <T> T input(T instance) {
		skDispatchingInput.input(instance);
		return instance;
	}
}
