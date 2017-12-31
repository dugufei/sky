package sky;

import android.app.Application;

import sky.core.ISky;
import sky.core.SKYHelper;
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
		initSky();
	}

	protected void initSky() {
		SKYHelper.newSky().setSky(this).setIViewCommon(this).Inject(this);
	}

	@Override public SKYModulesManage modulesManage() {
		return new SKYModulesManage();
	}

}
