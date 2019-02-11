package sk.plugins;

import sk.SKActivity;
import sk.SKFragment;

/**
 * @author sky
 * @version 1.0 on 2016-12-03 下午5:07
 * @see SKLayoutInterceptor 布局切换拦截器
 */
public interface SKLayoutInterceptor {

	class AdapterInterceptor implements SKLayoutInterceptor {

		@Override public void showContent(SKActivity skyActivity) {

		}

		@Override public void showEmpty(SKActivity skyActivity) {

		}

		@Override public void showError(SKActivity skyActivity) {

		}

		@Override public void showLoading(SKActivity skyActivity) {

		}

		@Override public void showContent(SKFragment skyFragment) {

		}

		@Override public void showEmpty(SKFragment skyFragment) {

		}

		@Override public void showError(SKFragment skyFragment) {

		}

		@Override public void showLoading(SKFragment skyFragment) {

		}

	}

	SKLayoutInterceptor NONE = new SKLayoutInterceptor() {

		@Override public void showContent(SKActivity skyActivity) {

		}

		@Override public void showEmpty(SKActivity skyActivity) {

		}

		@Override public void showError(SKActivity skyActivity) {

		}

		@Override public void showLoading(SKActivity skyActivity) {

		}

		@Override public void showContent(SKFragment skyFragment) {

		}

		@Override public void showEmpty(SKFragment skyFragment) {

		}

		@Override public void showLoading(SKFragment skyFragment) {

		}

		@Override public void showError(SKFragment skyFragment) {

		}

	};

	void showContent(SKActivity skyActivity);

	void showEmpty(SKActivity skyActivity);

	void showError(SKActivity skyActivity);

	void showLoading(SKActivity skyActivity);

	void showContent(SKFragment skyFragment);

	void showEmpty(SKFragment skyFragment);

	void showLoading(SKFragment skyFragment);

	void showError(SKFragment skyFragment);
}
