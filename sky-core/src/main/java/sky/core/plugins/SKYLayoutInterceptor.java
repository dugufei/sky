package sky.core.plugins;


import sky.core.SKYActivity;
import sky.core.SKYFragment;

/**
 * @author sky
 * @version 1.0 on 2016-12-03 下午5:07
 * @see SKYLayoutInterceptor 布局切换拦截器
 */
public interface SKYLayoutInterceptor {

	class AdapterInterceptor implements SKYLayoutInterceptor {

		@Override public void showContent(SKYActivity skyActivity) {

		}

		@Override public void showEmpty(SKYActivity skyActivity) {

		}

		@Override public void showBizError(SKYActivity skyActivity) {

		}

		@Override public void showLoading(SKYActivity skyActivity) {

		}

		@Override public void showHttpError(SKYActivity skyActivity) {

		}

		@Override public void showContent(SKYFragment skyFragment) {

		}

		@Override public void showEmpty(SKYFragment skyFragment) {

		}

		@Override public void showBizError(SKYFragment skyFragment) {

		}

		@Override public void showLoading(SKYFragment skyFragment) {

		}

		@Override public void showHttpError(SKYFragment skyFragment) {

		}
	}

	SKYLayoutInterceptor NONE = new SKYLayoutInterceptor() {

		@Override public void showContent(SKYActivity skyActivity) {

		}

		@Override public void showEmpty(SKYActivity skyActivity) {

		}

		@Override public void showBizError(SKYActivity skyActivity) {

		}

		@Override public void showLoading(SKYActivity skyActivity) {

		}

		@Override public void showHttpError(SKYActivity skyActivity) {

		}

		@Override public void showContent(SKYFragment skyFragment) {

		}

		@Override public void showEmpty(SKYFragment skyFragment) {

		}

		@Override public void showBizError(SKYFragment skyFragment) {

		}

		@Override public void showLoading(SKYFragment skyFragment) {

		}

		@Override public void showHttpError(SKYFragment skyFragment) {

		}

	};

	void showContent(SKYActivity skyActivity);

	void showEmpty(SKYActivity skyActivity);

	void showBizError(SKYActivity skyActivity);

	void showLoading(SKYActivity skyActivity);

	void showHttpError(SKYActivity skyActivity);

	void showContent(SKYFragment skyFragment);

	void showEmpty(SKYFragment skyFragment);

	void showBizError(SKYFragment skyFragment);

	void showLoading(SKYFragment skyFragment);

	void showHttpError(SKYFragment skyFragment);
}
