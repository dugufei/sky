package jc.sky;

import android.app.Application;
import android.content.Context;

import jc.sky.modules.SKYModulesManage;
import jc.sky.modules.methodProxy.SKYMethods;
import jc.sky.view.common.SKYIViewCommon;
import retrofit2.Retrofit;

/**
 * Created by sky on 15/1/26. 说明：使用架构必须继承
 */
public class SKYApplication extends Application implements ISKYBind {

	@Override public boolean isLogOpen() {
		return true;
	}

	@Override public Retrofit getRestAdapter(Retrofit.Builder builder) {
		builder.baseUrl("http://skyJC.com");
		return builder.build();
	}

	@Override public SKYMethods getMethodInterceptor(SKYMethods.Builder builder) {
		return builder.build();
	}

	/**
	 * 获取配置管理器
	 *
	 * @return
	 */
	@Override public SKYModulesManage getModulesManage() {
		return new SKYModulesManage(this);
	}
}
