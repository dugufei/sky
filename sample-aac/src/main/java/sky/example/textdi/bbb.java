package sky.example.textdi;

import sk.SKInject;
import sky.SKInput;
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
