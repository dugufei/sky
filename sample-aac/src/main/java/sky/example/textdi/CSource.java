package sky.example.textdi;

import sky.SKProvider;
import sky.SKSingleton;

/**
 * @author sky
 * @version 1.0 on 2018-06-23 下午10:02
 * @see CSource
 */
public class CSource {

	// @SKProvider public A providerA() {
	// return new A();
	// }
	//
	// @SKProvider public B providerB() {
	// return new B();
	// }

	@SKProvider public D providerD(B b) {
		return new D();
	}

	@SKProvider public F providerF(B b) {
		return new F();
	}

}
