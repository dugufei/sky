package sk;

import java.util.ArrayList;

import sk.plugins.BizEndInterceptor;
import sk.plugins.BizStartInterceptor;
import sk.plugins.DisplayEndInterceptor;
import sk.plugins.DisplayStartInterceptor;
import sk.plugins.SKActivityInterceptor;
import sk.plugins.SKFragmentInterceptor;
import sk.plugins.SKLayoutInterceptor;

/**
 * @author sky
 * @version 版本
 */
public final class SKInterceptor {

	final SKActivityInterceptor				skyActivityInterceptor;

	final SKLayoutInterceptor				skyLayoutInterceptor;

	final SKFragmentInterceptor				skyFragmentInterceptor;

	final ArrayList<BizStartInterceptor>	bizStartInterceptor;		// 方法开始拦截器

	final DisplayStartInterceptor			displayStartInterceptor;	// 方法开始拦截器

	final ArrayList<BizEndInterceptor>		bizEndInterceptor;			// 方法结束拦截器

	final DisplayEndInterceptor				displayEndInterceptor;		// 方法结束拦截器

	SKInterceptor(SKLayoutInterceptor skyLayoutInterceptor, SKActivityInterceptor SKActivityInterceptor, SKFragmentInterceptor SKFragmentInterceptor,
			ArrayList<BizStartInterceptor> bizStartInterceptor, DisplayStartInterceptor displayStartInterceptor, ArrayList<BizEndInterceptor> bizEndInterceptor,
			DisplayEndInterceptor displayEndInterceptor) {
		this.skyLayoutInterceptor = skyLayoutInterceptor;
		this.bizEndInterceptor = bizEndInterceptor;
		this.displayEndInterceptor = displayEndInterceptor;
		this.displayStartInterceptor = displayStartInterceptor;
		this.bizStartInterceptor = bizStartInterceptor;
		this.skyActivityInterceptor = SKActivityInterceptor;
		this.skyFragmentInterceptor = SKFragmentInterceptor;
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public SKActivityInterceptor activityInterceptor() {
		return skyActivityInterceptor;
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public SKLayoutInterceptor layoutInterceptor() {
		return skyLayoutInterceptor;
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public SKFragmentInterceptor fragmentInterceptor() {
		return skyFragmentInterceptor;
	}

	public static class Builder {

		private SKLayoutInterceptor				skLayoutInterceptor;		// 布局切换拦截器

		private SKActivityInterceptor			skActivityInterceptor;		// activity拦截器

		private SKFragmentInterceptor			skFragmentInterceptor;		// activity拦截器

		private ArrayList<BizStartInterceptor>	startInterceptors;			// 方法开始拦截器

		private ArrayList<BizEndInterceptor>	endInterceptors;			// 方法结束拦截器

		private DisplayStartInterceptor			displayStartInterceptor;	// 方法开始拦截器

		private DisplayEndInterceptor			displayEndInterceptor;		// 方法结束拦截器

		public void setActivityInterceptor(SKActivityInterceptor skActivityInterceptor) {
			this.skActivityInterceptor = skActivityInterceptor;
		}

		public void setFragmentInterceptor(SKFragmentInterceptor skFragmentInterceptor) {
			this.skFragmentInterceptor = skFragmentInterceptor;
		}

		public void setLayoutInterceptor(SKLayoutInterceptor skLayoutInterceptor) {
			this.skLayoutInterceptor = skLayoutInterceptor;
		}

		public Builder addStartInterceptor(BizStartInterceptor bizStartInterceptor) {
			if (startInterceptors == null) {
				startInterceptors = new ArrayList<>();
			}
			if (!startInterceptors.contains(bizStartInterceptor)) {
				startInterceptors.add(bizStartInterceptor);
			}
			return this;
		}

		public Builder addEndInterceptor(BizEndInterceptor bizEndInterceptor) {
			if (endInterceptors == null) {
				endInterceptors = new ArrayList<>();
			}
			if (!endInterceptors.contains(bizEndInterceptor)) {
				endInterceptors.add(bizEndInterceptor);
			}
			return this;
		}

		public Builder setDisplayStartInterceptor(DisplayStartInterceptor displayStartInterceptor) {
			this.displayStartInterceptor = displayStartInterceptor;
			return this;
		}

		public Builder setDisplayEndInterceptor(DisplayEndInterceptor displayEndInterceptor) {
			this.displayEndInterceptor = displayEndInterceptor;
			return this;
		}

		SKInterceptor build() {
			// 默认值
			ensureSaneDefaults();
			return new SKInterceptor(skLayoutInterceptor, skActivityInterceptor, skFragmentInterceptor, startInterceptors, displayStartInterceptor, endInterceptors, displayEndInterceptor);
		}

		private void ensureSaneDefaults() {
			if (startInterceptors == null) {
				startInterceptors = new ArrayList<>();
			}
			if (endInterceptors == null) {
				endInterceptors = new ArrayList<>();
			}
			if (skFragmentInterceptor == null) {
				skFragmentInterceptor = SKFragmentInterceptor.NONE;
			}
			if (skActivityInterceptor == null) {
				skActivityInterceptor = SKActivityInterceptor.NONE;
			}
			if (skLayoutInterceptor == null) {
				skLayoutInterceptor = SKLayoutInterceptor.NONE;
			}
		}

	}
}
