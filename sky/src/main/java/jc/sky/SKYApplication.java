package jc.sky;

import android.app.Application;

import jc.sky.modules.SKYExtraModulesManage;
import sky.core.ISky;
import sky.core.SKYIViewCommon;
import sky.core.SKYModulesManage;

/**
 * @author sky
 * @version 1.0 on 2017-05-27 上午9:31
 * @see SKYApplication
 */
public abstract class SKYApplication extends Application implements ISky, SKYIViewCommon {

	@Override public void onCreate() {
		super.onCreate();
		// sky架构
		SKYExtraHelper.newSky().setSky(this).setIViewCommon(this).Inject(this);
		// 文件初始化
//		SKYHelper.fileCacheManage().configureCustomerCache(this.getExternalFilesDir(""));
	}


	@Override public SKYModulesManage modulesManage() {
		return new SKYExtraModulesManage();
	}

}
