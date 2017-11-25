package sky.core.plugins;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import sky.core.SKYActivity;
import sky.core.SKYBuilder;


/**
 * @author sky
 * @version 版本
 */
public interface SKYActivityInterceptor {

	void build(SKYActivity skyActivity, SKYBuilder initialSKYBuilder);

	void onCreate(SKYActivity skyActivity, Bundle bundle, Bundle savedInstanceState);

	void onPostCreate(SKYActivity skyActivity, Bundle savedInstanceState);

	void onStart(SKYActivity skyActivity);

	void onResume(SKYActivity skyActivity);

	void onPostResume(SKYActivity skyActivity);

	void onPause(SKYActivity skyActivity);

	void onStop(SKYActivity skyActivity);

	void onDestroy(SKYActivity skyActivity);

	void onRestart(SKYActivity skyActivity);

	void onActivityResult(int requestCode, int resultCode, Intent data);

	void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

	void onShowLoading(SKYActivity skyActivity);

	void onCloseLoading(SKYActivity skyActivity);

	class AdapterInterceptor implements SKYActivityInterceptor {

		@Override public void build(SKYActivity skyActivity, SKYBuilder initialSKYBuilder) {

		}

		@Override public void onCreate(SKYActivity skyActivity, Bundle bundle, Bundle savedInstanceState) {

		}

		@Override public void onPostCreate(SKYActivity skyActivity, Bundle savedInstanceState) {

		}

		@Override public void onStart(SKYActivity skyActivity) {

		}

		@Override public void onResume(SKYActivity skyActivity) {

		}

		@Override public void onPostResume(SKYActivity skyActivity) {

		}

		@Override public void onPause(SKYActivity skyActivity) {

		}

		@Override public void onStop(SKYActivity skyActivity) {

		}

		@Override public void onDestroy(SKYActivity skyActivity) {

		}

		@Override public void onRestart(SKYActivity skyActivity) {

		}

		@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {

		}

		@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		}

		@Override public void onShowLoading(SKYActivity skyActivity) {

		}

		@Override public void onCloseLoading(SKYActivity skyActivity) {

		}
	}

	SKYActivityInterceptor NONE = new SKYActivityInterceptor() {

		@Override public void build(SKYActivity skyActivity, SKYBuilder initialSKYBuilder) {

		}

		@Override public void onCreate(SKYActivity skyActivity, Bundle bundle, Bundle savedInstanceState) {

		}

		@Override public void onPostCreate(SKYActivity skyActivity, Bundle savedInstanceState) {

		}

		@Override public void onStart(SKYActivity skyActivity) {

		}

		@Override public void onResume(SKYActivity skyActivity) {

		}

		@Override public void onPostResume(SKYActivity skyActivity) {

		}

		@Override public void onPause(SKYActivity skyActivity) {

		}

		@Override public void onStop(SKYActivity skyActivity) {

		}

		@Override public void onDestroy(SKYActivity skyActivity) {

		}

		@Override public void onRestart(SKYActivity skyActivity) {

		}

		@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {

		}

		@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		}

		@Override public void onShowLoading(SKYActivity skyActivity) {

		}

		@Override public void onCloseLoading(SKYActivity skyActivity) {

		}
	};

}
