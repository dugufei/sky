package sk;

import java.util.ArrayList;

import sk.plugins.SKBizEndInterceptor;
import sk.plugins.SKBizStartInterceptor;
import sk.plugins.SKDisplayEndInterceptor;
import sk.plugins.SKDisplayStartInterceptor;
import sk.plugins.SKActivityInterceptor;
import sk.plugins.SKErrorInterceptor;
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

	final ArrayList<SKBizStartInterceptor>	bizStartInterceptor;		// 方法开始拦截器

	final ArrayList<SKBizEndInterceptor>	bizEndInterceptor;			// 方法结束拦截器

	final SKDisplayStartInterceptor			displayStartInterceptor;	// 方法开始拦截器

	final SKDisplayEndInterceptor			displayEndInterceptor;		// 方法结束拦截器

	final ArrayList<SKErrorInterceptor>		skErrorInterceptors;		// 错误拦截

	SKInterceptor(SKLayoutInterceptor skyLayoutInterceptor, SKActivityInterceptor SKActivityInterceptor, SKFragmentInterceptor SKFragmentInterceptor,
			ArrayList<SKBizStartInterceptor> bizStartInterceptor, SKDisplayStartInterceptor displayStartInterceptor, ArrayList<SKBizEndInterceptor> bizEndInterceptor,
			SKDisplayEndInterceptor displayEndInterceptor, ArrayList<SKErrorInterceptor> skErrorInterceptors) {
		this.skyLayoutInterceptor = skyLayoutInterceptor;
		this.bizEndInterceptor = bizEndInterceptor;
		this.displayEndInterceptor = displayEndInterceptor;
		this.displayStartInterceptor = displayStartInterceptor;
		this.bizStartInterceptor = bizStartInterceptor;
		this.skyActivityInterceptor = SKActivityInterceptor;
		this.skyFragmentInterceptor = SKFragmentInterceptor;
		this.skErrorInterceptors = skErrorInterceptors;
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

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public ArrayList<SKBizStartInterceptor> bizStartInterceptors() {
		return bizStartInterceptor;
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public ArrayList<SKBizEndInterceptor> bizEndInterceptors() {
		return bizEndInterceptor;
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public SKDisplayStartInterceptor displayStartInterceptor() {
		return displayStartInterceptor;
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public SKDisplayEndInterceptor displayEndInterceptor() {
		return displayEndInterceptor;
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public ArrayList<SKErrorInterceptor> skErrorInterceptors() {
		return skErrorInterceptors;
	}

	public static class Builder {

		private SKLayoutInterceptor					skLayoutInterceptor;		// 布局切换拦截器

		private SKActivityInterceptor				skActivityInterceptor;		// activity拦截器

		private SKFragmentInterceptor				skFragmentInterceptor;		// fragment拦截器

		private ArrayList<SKBizStartInterceptor>	startInterceptors;			// 业务方法开始拦截器

		private ArrayList<SKBizEndInterceptor>		endInterceptors;			// 业务方法结束拦截器

		private SKDisplayStartInterceptor			displayStartInterceptor;	// 跳转方法开始拦截器

		private SKDisplayEndInterceptor				displayEndInterceptor;		// 跳转方法结束拦截器

		private ArrayList<SKErrorInterceptor>		skErrorInterceptors;		// 错误方法拦截器

		public void setActivityInterceptor(SKActivityInterceptor skActivityInterceptor) {
			this.skActivityInterceptor = skActivityInterceptor;
		}

		public void setFragmentInterceptor(SKFragmentInterceptor skFragmentInterceptor) {
			this.skFragmentInterceptor = skFragmentInterceptor;
		}

		public void setLayoutInterceptor(SKLayoutInterceptor skLayoutInterceptor) {
			this.skLayoutInterceptor = skLayoutInterceptor;
		}

		public Builder addStartInterceptor(SKBizStartInterceptor bizStartInterceptor) {
			if (startInterceptors == null) {
				startInterceptors = new ArrayList<>();
			}
			if (!startInterceptors.contains(bizStartInterceptor)) {
				startInterceptors.add(bizStartInterceptor);
			}
			return this;
		}

		public Builder addErrorIntercepor(SKErrorInterceptor skErrorInterceptor) {
			if (skErrorInterceptors == null) {
				skErrorInterceptors = new ArrayList<>();
			}
			if (!skErrorInterceptors.contains(skErrorInterceptor)) {
				skErrorInterceptors.add(skErrorInterceptor);
			}
			return this;
		}

		public Builder addEndInterceptor(SKBizEndInterceptor bizEndInterceptor) {
			if (endInterceptors == null) {
				endInterceptors = new ArrayList<>();
			}
			if (!endInterceptors.contains(bizEndInterceptor)) {
				endInterceptors.add(bizEndInterceptor);
			}
			return this;
		}

		public Builder setDisplayStartInterceptor(SKDisplayStartInterceptor displayStartInterceptor) {
			this.displayStartInterceptor = displayStartInterceptor;
			return this;
		}

		public Builder setDisplayEndInterceptor(SKDisplayEndInterceptor displayEndInterceptor) {
			this.displayEndInterceptor = displayEndInterceptor;
			return this;
		}

		SKInterceptor build() {
			// 默认值
			ensureSaneDefaults();
			return new SKInterceptor(skLayoutInterceptor, skActivityInterceptor, skFragmentInterceptor, startInterceptors, displayStartInterceptor, endInterceptors, displayEndInterceptor,
					skErrorInterceptors);
		}

		private void ensureSaneDefaults() {
			if (startInterceptors == null) {
				startInterceptors =  new ArrayList<>();
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
			if (skErrorInterceptors == null) {
				skErrorInterceptors = new ArrayList<>();
			}
		}

	}
}
