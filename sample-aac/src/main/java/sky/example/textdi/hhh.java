package sky.example.textdi;

import android.util.Log;

import sk.SKInject;
import sk.SKInputs;
import sky.SKInput;
import sky.SKSource;
import sky.di.SKInitInterface;
import sky.di.SKLazy;

/**
 * @author sky
 * @version 1.0 on 2018-06-23 下午10:04
 * @see hhh
 */
public class hhh extends SKInject {

	@SKInput A			a;

	@SKInput SKLazy<B>	b;

	@SKInput C			c;

	@SKInput D			d;

	@SKInput F			f;

	@SKInput bbb		bbb;

	public void init(A aaaaa, B bnnnn) {
		Log.d("我是谁aaaa", aaaaa + "：");
		Log.d("我是谁bbbb", bnnnn + "：");
		Log.d("我是谁", this.a + "：");
		Log.d("我是谁111", this.b.get() + "");
		Log.d("我是谁222", c + "");
		Log.d("我是谁333", d + "");
		Log.d("我是谁444", bbb + "");
	}


}

