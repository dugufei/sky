package sky.test;

import sky.SKProvider;
import sky.SKSingleton;
import sky.test.model.Model;
import sky.test.model.Model1;
import sky.test.model.Model2;
import sky.test.model.Model3;

/**
 * @author sky
 * @version 1.0 on 2019-02-14 11:29 AM
 * @see OderTwoManage
 */
public class OderTwoManage {

	@SKProvider @SKSingleton public Model2 modelProvider() {
		return new Model2();
	}

	@SKProvider public Model3 model1Provider() {
		return new Model3();
	}
}
