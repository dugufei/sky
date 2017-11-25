package sky.core.plugins;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import sky.core.SKYBuilder;
import sky.core.SKYFragment;

/**
 * @author sky
 * @version 版本
 */
public interface SKYFragmentInterceptor {

	void build(Fragment skyFragment, SKYBuilder initialSKYBuilder);

	void onFragmentCreated(SKYFragment skyFragment, Bundle bundle, Bundle savedInstanceState);

	void buildAfter(SKYFragment skyFragment);

	void onFragmentStart(SKYFragment skyFragment);

	void onFragmentResume(SKYFragment skyFragment);

	void onFragmentPause(SKYFragment skyFragment);

	void onFragmentStop(SKYFragment skyFragment);

	void onFragmentDestroy(SKYFragment skyFragment);

	void onShowLoading(Fragment skyFragment);

	void onCloseLoading(Fragment skyFragment);

	class AdapterInterceptor implements SKYFragmentInterceptor {

		@Override public void build(Fragment skyFragment, SKYBuilder initialSKYBuilder) {

		}

		@Override public void onFragmentCreated(SKYFragment skyFragment, Bundle bundle, Bundle savedInstanceState) {

		}

		@Override public void buildAfter(SKYFragment skyFragment) {

		}

		@Override public void onFragmentStart(SKYFragment skyFragment) {

		}

		@Override public void onFragmentResume(SKYFragment skyFragment) {

		}

		@Override public void onFragmentPause(SKYFragment skyFragment) {

		}

		@Override public void onFragmentStop(SKYFragment skyFragment) {

		}

		@Override public void onFragmentDestroy(SKYFragment skyFragment) {

		}

		@Override public void onShowLoading(Fragment skyFragment) {

		}

		@Override public void onCloseLoading(Fragment skyFragment) {

		}
	}

	SKYFragmentInterceptor NONE = new SKYFragmentInterceptor() {

		@Override public void build(Fragment skyFragment, SKYBuilder initialSKYBuilder) {

		}

		@Override public void onFragmentCreated(SKYFragment skyFragment, Bundle bundle, Bundle savedInstanceState) {

		}

		@Override public void buildAfter(SKYFragment skyFragment) {

		}

		@Override public void onFragmentStart(SKYFragment skyFragment) {

		}

		@Override public void onFragmentResume(SKYFragment skyFragment) {

		}

		@Override public void onFragmentPause(SKYFragment skyFragment) {

		}

		@Override public void onFragmentStop(SKYFragment skyFragment) {

		}

		@Override public void onFragmentDestroy(SKYFragment skyFragment) {

		}

		@Override public void onShowLoading(Fragment skyFragment) {

		}

		@Override public void onCloseLoading(Fragment skyFragment) {

		}
	};

}
