package sky.example.textdi;

import android.util.Log;

import sk.SKInputs;
import sky.SKInput;
import sky.SKSource;
import sky.di.SKLazy;

/**
 * @author sky
 * @version 1.0 on 2018-06-23 下午10:04
 * @see hhh
 */
@SKSource({ASource.class,BSource.class})
public class hhh {

	public hhh() {
		SKInputs.inject(this);
	}

	@SKInput A			a;

	@SKInput B			b;

	@SKInput C	c;

	@SKInput D			d;

	@SKInput bbb		bbb;

	public void init() {
		Log.d("我是谁", a.s);
		Log.d("我是谁111", c.c);
	}
}
