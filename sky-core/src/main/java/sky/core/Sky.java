package sky.core;

import android.app.Application;

/**
 * @author sky
 * @version 1.0 on 2017-11-25 下午11:09
 * @see Sky
 */
public class Sky {

	private ISky sky;

	/**
	 * @param sky
	 * @return 返回值
	 */
	public Sky setSky(ISky sky) {
		this.sky = sky;
		return this;
	}

	private SKYIViewCommon skyiViewCommon;

	/**
	 * @param skyiViewCommon
	 *            参数
	 * @return 返回值
	 */
	public Sky setIViewCommon(SKYIViewCommon skyiViewCommon) {
		this.skyiViewCommon = skyiViewCommon;
		return this;
	}

	/**
	 * @param application
	 *            参数
	 */
	public void Inject(Application application) {
		if (application == null) {
			throw new RuntimeException("Sky架构:Application没有设置");
		}

		if (this.sky == null) {
			this.sky = ISky.ISKY;
		}

		if (this.skyiViewCommon == null) {
			this.skyiViewCommon = SKYIViewCommon.SKYI_VIEW_COMMON;
		}
		// 提供者
		SKYModule skyModule = new SKYModule(application);
		skyModule.setSky(this.sky);
		skyModule.setSkyiViewCommon(this.skyiViewCommon);
		SKYHelper.mSKYModulesManage = this.sky.modulesManage();
		DaggerSKYIComponent.builder().sKYModule(skyModule).build().inject(SKYHelper.mSKYModulesManage);
		// 初始化组件化
		SKYModuleUtils.initModule(application);
	}
}
