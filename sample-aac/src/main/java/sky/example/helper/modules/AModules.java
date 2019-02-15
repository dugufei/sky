package sky.example.helper.modules;

import sky.SKProvider;
import sky.SKSingleton;

/**
 * @author sky
 * @version 1.0 on 2018-07-12 上午12:43
 * @see AModules
 */
public class AModules {

	@SKSingleton @SKProvider public AModel providerABC() {
		return new AModel();
	}
}
