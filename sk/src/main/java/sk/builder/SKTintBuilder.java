package sk.builder;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import sk.SKActivity;

/**
 * @author sky
 * @version 1.0 on 2018-07-13 下午2:34
 * @see SKTintBuilder
 */
public class SKTintBuilder {

	public SystemBarTintManager	tintManager;

	public int					tintColor;

	public boolean				statusBarEnabled			= true;

	public boolean				navigationBarTintEnabled	= true;

	public void createTint(SKActivity skActivity) {
		/** 状态栏颜色 **/
		tintManager = new SystemBarTintManager(skActivity);
		// enable status bar tint
		tintManager.setStatusBarTintEnabled(statusBarEnabled);
		// enable navigation bar tint
		tintManager.setNavigationBarTintEnabled(navigationBarTintEnabled);
		tintManager.setStatusBarTintResource(tintColor);
	}
}
