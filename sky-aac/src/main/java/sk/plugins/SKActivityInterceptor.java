package sk.plugins;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import sk.SKActivity;
import sk.SKBuilder;


/**
 * @author sky
 * @version 版本
 */
public interface SKActivityInterceptor {

	void build(SKActivity skyActivity, SKBuilder initialSKBuilder);

	void onCreate(SKActivity skyActivity, Bundle bundle, Bundle savedInstanceState);

	void onPostCreate(SKActivity skyActivity, Bundle savedInstanceState);

	void onStart(SKActivity skyActivity);

	void onResume(SKActivity skyActivity);

	void onPostResume(SKActivity skyActivity);

	void onPause(SKActivity skyActivity);

	void onStop(SKActivity skyActivity);

	void onDestroy(SKActivity skyActivity);

	void onRestart(SKActivity skyActivity);

	void onActivityResult(int requestCode, int resultCode, Intent data);

	void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

	void onShowLoading(SKActivity skyActivity);

	void onCloseLoading(SKActivity skyActivity);

	class AdapterInterceptor implements SKActivityInterceptor {

		@Override public void build(SKActivity skyActivity, SKBuilder initialSKBuilder) {

		}

		@Override public void onCreate(SKActivity skyActivity, Bundle bundle, Bundle savedInstanceState) {

		}

		@Override public void onPostCreate(SKActivity skyActivity, Bundle savedInstanceState) {

		}

		@Override public void onStart(SKActivity skyActivity) {

		}

		@Override public void onResume(SKActivity skyActivity) {

		}

		@Override public void onPostResume(SKActivity skyActivity) {

		}

		@Override public void onPause(SKActivity skyActivity) {

		}

		@Override public void onStop(SKActivity skyActivity) {

		}

		@Override public void onDestroy(SKActivity skyActivity) {

		}

		@Override public void onRestart(SKActivity skyActivity) {

		}

		@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {

		}

		@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		}

		@Override public void onShowLoading(SKActivity skyActivity) {

		}

		@Override public void onCloseLoading(SKActivity skyActivity) {

		}
	}

	SKActivityInterceptor NONE = new SKActivityInterceptor() {

		@Override public void build(SKActivity skyActivity, SKBuilder initialSKBuilder) {

		}

		@Override public void onCreate(SKActivity skyActivity, Bundle bundle, Bundle savedInstanceState) {

		}

		@Override public void onPostCreate(SKActivity skyActivity, Bundle savedInstanceState) {

		}

		@Override public void onStart(SKActivity skyActivity) {

		}

		@Override public void onResume(SKActivity skyActivity) {

		}

		@Override public void onPostResume(SKActivity skyActivity) {

		}

		@Override public void onPause(SKActivity skyActivity) {

		}

		@Override public void onStop(SKActivity skyActivity) {

		}

		@Override public void onDestroy(SKActivity skyActivity) {

		}

		@Override public void onRestart(SKActivity skyActivity) {

		}

		@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {

		}

		@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		}

		@Override public void onShowLoading(SKActivity skyActivity) {

		}

		@Override public void onCloseLoading(SKActivity skyActivity) {

		}
	};

}
