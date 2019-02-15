package sky.test;

import sky.SKProvider;
import sky.SKSingleton;
import sky.test.model.Model;
import sky.test.model.Model1;

/**
 * @author sky
 * @version 1.0 on 2019-02-14 11:29 AM
 * @see OderManage
 */
public class OderManage {

	@SKProvider @SKSingleton public Model modelProvider() {
		return new Model();
	}

	@SKProvider public Model1 model1Provider() {
		return new Model1();
	}
}
