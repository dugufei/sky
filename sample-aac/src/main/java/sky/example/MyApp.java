package sky.example;

import sk.SKApp;
import sk.SKDI;
import sk.SKDefaultLibrary;
import sky.SKDIApp;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:07
 * @see MyApp
 */

@SKDIApp(SKDefaultLibrary.class)
public class MyApp extends SKApp {

	@Override public void onCreate() {
		super.onCreate();
		SKDI.create();
	}
}
