package jc.sky;

import android.app.Application;

import jc.sky.modules.SKYModulesManage;
import jc.sky.modules.methodProxy.SKYMethods;
import jc.sky.view.common.SKYIViewCommon;
import retrofit2.Retrofit;

/**
 * @author sky
 * @version 1.0 on 2017-05-27 上午9:31
 * @see SKYApplication
 */
public class SKYApplication extends Application implements ISKYBind, SKYIViewCommon {

	@Override public void onCreate() {
		super.onCreate();
		// sky架构
		SKYHelper.newBind().setSkyBind(this).setIViewCommon(this).Inject(this);
		// 文件初始化
		SKYHelper.fileCacheManage().configureCustomerCache(this.getExternalFilesDir(""));
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

	@Override public boolean isLogOpen() {
		return true;
	}

	@Override public Retrofit getRestAdapter(Retrofit.Builder builder) {
		builder.baseUrl("http://www.jincanshen.com");
		return builder.build();
	}

	@Override public SKYMethods getMethodInterceptor(SKYMethods.Builder builder) {
		return builder.build();
	}

	@Override public SKYModulesManage getModulesManage() {
		return new SKYModulesManage();
	}
}
