package sky;

import android.app.Application;

import retrofit2.Retrofit;
import sky.core.ISky;
import sky.core.SKYHelper;
import sky.core.SKYIViewCommon;
import sky.core.SKYModulesManage;
import sky.core.SKYPlugins;

/**
 * @author sky
 * @version 1.0 on 2017-05-27 上午9:31
 * @see SKYApplication
 */
public class SKYApplication extends Application implements ISky, SKYIViewCommon {

	@Override public void onCreate() {
		super.onCreate();
		initSky();
	}

	protected void initSky() {
		SKYHelper.newSky().setSky(this).setIViewCommon(this).Inject(this);
	}

	@Override public boolean isLogOpen() {
		return false;
	}

	@Override public Retrofit.Builder httpAdapter(Retrofit.Builder builder) {
		builder.baseUrl("http://www.jincanshen.com");
		return builder;
	}

	@Override public SKYPlugins.Builder pluginInterceptor(SKYPlugins.Builder builder) {
		return builder;
	}

	@Override public SKYModulesManage modulesManage() {
		return new SKYModulesManage();
	}

	@Override public int layoutLoading() {
		return 0;
	}

	@Override public int layoutEmpty() {
		return 0;
	}

	@Override public int layoutBizError() {
		return 0;
	}

	@Override public int layoutHttpError() {
		return 0;
	}
}
