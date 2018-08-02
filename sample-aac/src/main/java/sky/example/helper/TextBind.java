package sky.example.helper;

import android.os.Bundle;

import java.lang.reflect.Method;

import retrofit2.Retrofit;
import sk.ISK;
import sk.L;
import sk.SKDefaultManager;
import sk.SKInterceptor;
import sk.plugins.SKDisplayEndInterceptor;
import sk.plugins.SKDisplayStartInterceptor;

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
		builder.addErrorIntercepor((method, clazz, objects, interceptor) -> L.e(method.getName() + ":" + clazz + ":" + objects));
		builder.setDisplayStartInterceptor(new SKDisplayStartInterceptor() {

			@Override public boolean interceptStart(String clazzName, Bundle bundle) {
				L.i("执行开始 ::::" + clazzName + ": " + bundle);

				return false;
			}
		});
		builder.setDisplayEndInterceptor(new SKDisplayEndInterceptor() {

			@Override public void interceptEnd(String clazzName, Bundle bundle, Object backgroundResult) {
				L.i("执行结束 ::::" + clazzName + ": " + bundle + ": " + backgroundResult);
			}
		});
		return builder;
	}

	@Override public SKDefaultManager manage() {
		return new TextManager();
	}
}
