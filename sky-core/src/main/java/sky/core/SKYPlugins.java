package sky.core;

import java.util.ArrayList;

import sky.core.plugins.BizEndInterceptor;
import sky.core.plugins.BizStartInterceptor;
import sky.core.plugins.DisplayEndInterceptor;
import sky.core.plugins.DisplayStartInterceptor;
import sky.core.plugins.ImplEndInterceptor;
import sky.core.plugins.ImplStartInterceptor;
import sky.core.plugins.SKYActivityInterceptor;
import sky.core.plugins.SKYBizErrorInterceptor;
import sky.core.plugins.SKYFragmentInterceptor;
import sky.core.plugins.SKYHttpErrorInterceptor;
import sky.core.plugins.SKYLayoutInterceptor;

/**
 * @author sky
 * @version 版本
 */
public final class SKYPlugins {

	final SKYActivityInterceptor				skyActivityInterceptor;

	final SKYLayoutInterceptor					skyLayoutInterceptor;

	final SKYFragmentInterceptor				skyFragmentInterceptor;

	final ArrayList<BizStartInterceptor>		bizStartInterceptor;		// 方法开始拦截器

	final DisplayStartInterceptor				displayStartInterceptor;	// 方法开始拦截器

	final ArrayList<BizEndInterceptor>			bizEndInterceptor;			// 方法结束拦截器

	final DisplayEndInterceptor					displayEndInterceptor;		// 方法结束拦截器

	private ArrayList<ImplStartInterceptor>		implStartInterceptors;		// 方法开始拦截器

	private ArrayList<ImplEndInterceptor>		implEndInterceptors;		// 方法结束拦截器

	final ArrayList<SKYBizErrorInterceptor>		skyErrorInterceptor;		// 方法错误拦截器

	final ArrayList<SKYHttpErrorInterceptor>	skyHttpErrorInterceptors;	// 网络错误拦截器

	SKYPlugins(SKYLayoutInterceptor skyLayoutInterceptor, SKYActivityInterceptor SKYActivityInterceptor, SKYFragmentInterceptor SKYFragmentInterceptor,
			ArrayList<BizStartInterceptor> bizStartInterceptor, DisplayStartInterceptor displayStartInterceptor, ArrayList<BizEndInterceptor> bizEndInterceptor,
			DisplayEndInterceptor displayEndInterceptor, ArrayList<ImplStartInterceptor> implStartInterceptors, ArrayList<ImplEndInterceptor> implEndInterceptors,
			ArrayList<SKYBizErrorInterceptor> SKYErrorInterceptor, ArrayList<SKYHttpErrorInterceptor> skyHttpErrorInterceptors) {
		this.skyLayoutInterceptor = skyLayoutInterceptor;
		this.bizEndInterceptor = bizEndInterceptor;
		this.displayEndInterceptor = displayEndInterceptor;
		this.displayStartInterceptor = displayStartInterceptor;
		this.bizStartInterceptor = bizStartInterceptor;
		this.skyErrorInterceptor = SKYErrorInterceptor;
		this.implStartInterceptors = implStartInterceptors;
		this.implEndInterceptors = implEndInterceptors;
		this.skyActivityInterceptor = SKYActivityInterceptor;
		this.skyFragmentInterceptor = SKYFragmentInterceptor;
		this.skyHttpErrorInterceptors = skyHttpErrorInterceptors;
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public SKYActivityInterceptor activityInterceptor() {
		return skyActivityInterceptor;
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public SKYLayoutInterceptor layoutInterceptor() {
		return skyLayoutInterceptor;
	}

	/**
	 * 获取拦截器
	 *
	 * @return 返回值
	 */
	public SKYFragmentInterceptor fragmentInterceptor() {
		return skyFragmentInterceptor;
	}

	ArrayList<ImplStartInterceptor> startInterceptors() {
		return implStartInterceptors;
	}

	ArrayList<ImplEndInterceptor> endInterceptors() {
		return implEndInterceptors;
	}

	public static class Builder {

		private SKYLayoutInterceptor				skyLayoutInterceptor;		// 布局切换拦截器

		private SKYActivityInterceptor				skyActivityInterceptor;		// activity拦截器

		private SKYFragmentInterceptor				skyFragmentInterceptor;		// activity拦截器

		private ArrayList<BizStartInterceptor>		skyStartInterceptors;		// 方法开始拦截器

		private ArrayList<BizEndInterceptor>		bizEndInterceptors;			// 方法结束拦截器

		private ArrayList<ImplStartInterceptor>		implStartInterceptors;		// 方法开始拦截器

		private ArrayList<ImplEndInterceptor>		implEndInterceptors;		// 方法结束拦截器

		private ArrayList<SKYBizErrorInterceptor>	skyErrorInterceptors;		// 方法错误拦截器

		private DisplayStartInterceptor				displayStartInterceptor;	// 方法开始拦截器

		private DisplayEndInterceptor				displayEndInterceptor;		// 方法结束拦截器

		private ArrayList<SKYHttpErrorInterceptor>	skyHttpErrorInterceptors;	// 网络错误拦截

		public void setActivityInterceptor(SKYActivityInterceptor SKYActivityInterceptor) {
			this.skyActivityInterceptor = SKYActivityInterceptor;
		}

		public void setFragmentInterceptor(SKYFragmentInterceptor SKYFragmentInterceptor) {
			this.skyFragmentInterceptor = SKYFragmentInterceptor;
		}

		public void setSkyLayoutInterceptor(SKYLayoutInterceptor skyLayoutInterceptor) {
			this.skyLayoutInterceptor = skyLayoutInterceptor;
		}

		public Builder addStartInterceptor(BizStartInterceptor bizStartInterceptor) {
			if (skyStartInterceptors == null) {
				skyStartInterceptors = new ArrayList<>();
			}
			if (!skyStartInterceptors.contains(bizStartInterceptor)) {
				skyStartInterceptors.add(bizStartInterceptor);
			}
			return this;
		}

		public Builder addEndInterceptor(BizEndInterceptor bizEndInterceptor) {
			if (bizEndInterceptors == null) {
				bizEndInterceptors = new ArrayList<>();
			}
			if (!bizEndInterceptors.contains(bizEndInterceptor)) {
				bizEndInterceptors.add(bizEndInterceptor);
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

		public Builder addStartImplInterceptor(ImplStartInterceptor implStartInterceptor) {
			if (implStartInterceptors == null) {
				implStartInterceptors = new ArrayList<>();
			}
			if (!implStartInterceptors.contains(implStartInterceptor)) {
				implStartInterceptors.add(implStartInterceptor);
			}
			return this;
		}

		public Builder addEndImplInterceptor(ImplEndInterceptor implEndInterceptor) {
			if (implEndInterceptors == null) {
				implEndInterceptors = new ArrayList<>();
			}
			if (!implEndInterceptors.contains(implEndInterceptor)) {
				implEndInterceptors.add(implEndInterceptor);
			}
			return this;
		}

		public void addBizErrorInterceptor(SKYBizErrorInterceptor SKYErrorInterceptor) {
			if (skyErrorInterceptors == null) {
				skyErrorInterceptors = new ArrayList<>();
			}
			if (!skyErrorInterceptors.contains(SKYErrorInterceptor)) {
				skyErrorInterceptors.add(SKYErrorInterceptor);
			}
		}

		public void addHttpErrorInterceptor(SKYHttpErrorInterceptor skyHttpErrorInterceptor) {
			if (skyHttpErrorInterceptors == null) {
				skyHttpErrorInterceptors = new ArrayList<>();
			}
			if (!skyHttpErrorInterceptors.contains(skyHttpErrorInterceptor)) {
				skyHttpErrorInterceptors.add(skyHttpErrorInterceptor);
			}
		}

		SKYPlugins build() {
			// 默认值
			ensureSaneDefaults();
			return new SKYPlugins(skyLayoutInterceptor, skyActivityInterceptor, skyFragmentInterceptor, skyStartInterceptors, displayStartInterceptor, bizEndInterceptors, displayEndInterceptor,
					implStartInterceptors, implEndInterceptors, skyErrorInterceptors, skyHttpErrorInterceptors);
		}

		private void ensureSaneDefaults() {
			if (skyStartInterceptors == null) {
				skyStartInterceptors = new ArrayList<>();
			}
			if (bizEndInterceptors == null) {
				bizEndInterceptors = new ArrayList<>();
			}
			if (skyErrorInterceptors == null) {
				skyErrorInterceptors = new ArrayList<>();
			}
			if (skyFragmentInterceptor == null) {
				skyFragmentInterceptor = SKYFragmentInterceptor.NONE;
			}
			if (skyActivityInterceptor == null) {
				skyActivityInterceptor = SKYActivityInterceptor.NONE;
			}
			if (skyLayoutInterceptor == null) {
				skyLayoutInterceptor = SKYLayoutInterceptor.NONE;
			}
			if (implStartInterceptors == null) {
				implStartInterceptors = new ArrayList<>();
			}
			if (implEndInterceptors == null) {
				implEndInterceptors = new ArrayList<>();
			}
			if (skyHttpErrorInterceptors == null) {
				skyHttpErrorInterceptors = new ArrayList<>();
			}
		}

	}
}
