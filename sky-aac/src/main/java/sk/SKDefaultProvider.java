package sk;

import android.app.Application;

import sky.SKProvider;
import sky.SKSingleton;

/**
 * @author sky
 * @version 1.0 on 2018-07-09 下午11:35
 * @see SKDefaultProvider
 */
public class SKDefaultProvider {

	@SKProvider @SKSingleton public Application provideApplication() {
		return null;
	}

}
