package sky.example.helper;

import retrofit2.Retrofit;
import sk.ISK;
import sk.SKDefaultManager;
import sk.SKInterceptor;

/**
 * @author sky
 * @version 1.0 on 2018-07-11 下午4:43
 * @see TextBind
 */
public class TextBind implements ISK {

	@Override public boolean isLogOpen() {
		return true;
	}

	@Override public Retrofit.Builder httpAdapter(Retrofit.Builder builder) {
		return builder;
	}

	@Override public SKInterceptor.Builder pluginInterceptor(SKInterceptor.Builder builder) {
		return builder;
	}

	@Override public SKDefaultManager manage() {
		return new TextManager();
	}
}
