package sky.example.textdi;

import sk.SKInject;
import sk.SKInputs;
import sky.SKInput;
import sky.SKSource;
import sky.di.SKInitInterface;
import sky.di.SKLazy;

/**
 * @author sky
 * @version 1.0 on 2018-06-28 下午3:20
 * @see bbb
 */
public class bbb extends SKInject {

	@SKInput A			a;

	@SKInput SKLazy<B>	b;

}
