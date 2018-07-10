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


	/**
	 * 公共视图
	 *
	 * @return 返回值
	 */
	static final SKCommonView getComnonView() {
		// return skModules.commonViewSKLazy.get();
		return null;
	}

	/**
	 * 注入
	 * 
	 * @param instance
	 * @param <T>
	 * @return
	 */
	public static <T> T input(T instance) {
		skDispatchingInput.input(instance);
		return instance;
	}

	static final void inputDispatching(SKDispatchingInput param) {
		skDispatchingInput = param;
	}

}
