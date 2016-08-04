package jc.sky.core.plugin;

import android.os.Bundle;

import jc.sky.view.SKYFragment;

/**
 * @创建人 sky
 * @创建时间 16/1/6
 * @类描述 fragment拦截器
 */
public interface SKYFragmentInterceptor {

	void onFragmentCreated(SKYFragment j2WFragment, Bundle bundle, Bundle savedInstanceState);

	void onFragmentStart(SKYFragment j2WFragment);

	void onFragmentResume(SKYFragment j2WFragment);

	void onFragmentPause(SKYFragment j2WFragment);

	void onFragmentStop(SKYFragment j2WFragment);

	void onFragmentDestroy(SKYFragment j2WFragment);

	SKYFragmentInterceptor NONE = new SKYFragmentInterceptor() {
		@Override
		public void onFragmentCreated(SKYFragment j2WFragment, Bundle bundle, Bundle savedInstanceState) {

		}

		@Override
		public void onFragmentStart(SKYFragment j2WFragment) {

		}

		@Override
		public void onFragmentResume(SKYFragment j2WFragment) {

		}

		@Override
		public void onFragmentPause(SKYFragment j2WFragment) {

		}

		@Override
		public void onFragmentStop(SKYFragment j2WFragment) {

		}

		@Override
		public void onFragmentDestroy(SKYFragment j2WFragment) {

		}
	};
}
