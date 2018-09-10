package sky.example.helper.modules;

import sky.SKProvider;
import sky.SKSingleton;

/**
 * @author sky
 * @version 1.0 on 2018-07-12 上午12:43
 * @see TextModules
 */
public class TextModules {

	@SKSingleton @SKProvider public BoutModel providerABC() {
		return new BoutModel();
	}
}
