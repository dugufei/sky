package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
import sk.SKViewModel;

/**
 * @author sky
 * @version 1.0 on 2018-07-25 上午10:36
 * @see SKViewModelProviders
 */
public class SKViewModelProviders {

	private static final String DEFAULT_KEY = "android.arch.lifecycle.SKViewModelProvider.DefaultKey";

	@Deprecated public SKViewModelProviders() {}

	private static Application checkApplication(Activity activity) {
		Application application = activity.getApplication();
		if (application == null) {
			throw new IllegalStateException("Your activity/fragment is not yet attached to " + "Application. You can't request ViewModel before onCreate call.");
		}
		return application;
	}

	private static Activity checkActivity(Fragment fragment) {
		Activity activity = fragment.getActivity();
		if (activity == null) {
			throw new IllegalStateException("Can't create SKViewModelProvider for detached fragment");
		}
		return activity;
	}

	@NonNull @MainThread public static <T extends SKViewModel> T find(@NonNull Fragment fragment, @NonNull Class<T> modelClass) {
		androidx.lifecycle.SKViewModelStore viewModelStore = androidx.lifecycle.SKViewModelStores.find(fragment);
		if (viewModelStore == null) {
			return null;
		}
		String canonicalName = modelClass.getCanonicalName();
		if (canonicalName == null) {
			throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
		}
		return (T) viewModelStore.get(DEFAULT_KEY + ":" + canonicalName);
	}

	@NonNull @MainThread public static <T extends SKViewModel> T find(@NonNull FragmentActivity activity, @NonNull Class<T> modelClass) {
		androidx.lifecycle.SKViewModelStore viewModelStore = androidx.lifecycle.SKViewModelStores.find(activity);
		if (viewModelStore == null) {
			return null;
		}
		String canonicalName = modelClass.getCanonicalName();
		if (canonicalName == null) {
			throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
		}
		return (T) viewModelStore.get(DEFAULT_KEY + ":" + canonicalName);
	}

	@NonNull @MainThread public static SKViewModelProvider of(@NonNull Fragment fragment) {
		return of(fragment, null);
	}

	@NonNull @MainThread public static SKViewModelProvider of(@NonNull FragmentActivity activity) {
		return of(activity, null);
	}

	@NonNull @MainThread public static SKViewModelProvider of(@NonNull Fragment fragment, @Nullable SKViewModelProvider.Factory factory) {
		Application application = checkApplication(checkActivity(fragment));
		if (factory == null) {
			factory = SKViewModelProvider.AndroidViewModelFactory.getInstance(application);
		}
		return new SKViewModelProvider(androidx.lifecycle.SKViewModelStores.of(fragment), factory);
	}

	@NonNull @MainThread public static SKViewModelProvider of(@NonNull FragmentActivity activity, @Nullable SKViewModelProvider.Factory factory) {
		Application application = checkApplication(activity);
		if (factory == null) {
			factory = SKViewModelProvider.AndroidViewModelFactory.getInstance(application);
		}
		return new SKViewModelProvider(androidx.lifecycle.SKViewModelStores.of(activity), factory);
	}

}
