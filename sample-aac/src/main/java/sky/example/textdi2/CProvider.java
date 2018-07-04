package sky.example.textdi2;

import sky.SKProvider;
import sky.SKSingleton;
import sky.example.textdi.A;
import sky.example.textdi.B;
import sky.example.textdi.C;
import sky.example.textdi.D;
import sky.example.textdi.hhh;

/**
 * @author sky
 * @version 1.0 on 2018-06-23 下午10:02
 * @see CProvider
 */
public class CProvider {

	@SKProvider public A providerA() {
		return new A();
	}

	@SKSingleton @SKProvider public B providerB() {
		return new B();
	}

	@SKProvider public C providerC(A a) {
		return new C();
	}

	@SKProvider public D providerD(B b) {
		return new D();
	}

	@SKSingleton @SKProvider public hhh providerhhh() {
		return new hhh();
	}

}
