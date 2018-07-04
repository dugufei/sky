package sky.example.textdi;

import sky.SKProvider;
import sky.SKSingleton;

/**
 * @author sky
 * @version 1.0 on 2018-06-23 下午10:02
 * @see ASource
 */
public class ASource {

	@SKSingleton @SKProvider public A providerA(B b,D d) {
		return new A();
	}

	@SKProvider public B providerB() {
		return new B();
	}

	@SKProvider public C providerC(A a) {
		return new C();
	}

	@SKSingleton @SKProvider public D providerD(B b) {
		return new D();
	}
}
