package sk;

import sky.di.SKInitInterface;

/**
 * @author sky
 * @version 1.0 on 2018-07-06 上午11:19
 * @see SKInject
 */
public abstract class SKInject implements SKInitInterface {

	@Override public final void initSK() {
		SKInputs.inject(this);
	}
}
