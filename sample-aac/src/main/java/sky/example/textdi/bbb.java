package sky.example.textdi;

import sk.SKInputs;
import sky.SKInput;
import sky.SKSource;
import sky.di.SKLazy;

/**
 * @author sky
 * @version 1.0 on 2018-06-28 下午3:20
 * @see bbb
 */
@SKSource(BSource.class)
public class bbb {

	public bbb() {
		SKInputs.inject(this);
	}

	@SKInput A			a;

	@SKInput SKLazy<B>	b;
}
