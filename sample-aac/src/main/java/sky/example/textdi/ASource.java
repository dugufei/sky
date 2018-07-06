package sky.example.textdi;

import java.util.ArrayList;
import java.util.List;

import sky.SKProvider;
import sky.SKSingleton;

/**
 * @author sky
 * @version 1.0 on 2018-06-23 下午10:02
 * @see ASource
 */
public class ASource {

	@SKSingleton @SKProvider public A providerA(B b) {
		return new A();
	}

	@SKSingleton @SKProvider public B providerB() {
		return new B();
	}

	@SKSingleton @SKProvider public B providerC() {
		return new B();
	}

	//
	@SKProvider public hhh providerHHH() {
		return new hhh();
	}

	@SKProvider public ArrayList<A> providerListA() {
		ArrayList<A> list = new ArrayList();
		A a = new A();
		a.s = "哈哈哈";
		list.add(a);
		return list;
	}

	@SKProvider public ArrayList<B> providerListB() {
		ArrayList<B> list = new ArrayList();
		B a = new B();
		a.b = "吼吼";
		list.add(a);
		return list;
	}

	//
	// @SKSingleton @SKProvider public D providerD(B b) {
	// return new D();
	// }
}
