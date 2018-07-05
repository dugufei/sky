package sky.example;

import sk.SKApp;
import sk.SKDI;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:07
 * @see MyApp
 */
public class MyApp extends SKApp  {

	@Override public void onCreate() {
		super.onCreate();
		DaggerMyComponent.builder().build().inject(this);

		SKDI.create();
	}
}
