package android.arch.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * @author sky
 * @version 1.0 on 2018-07-25 上午10:36
 * @see SKViewModelProviders
 */
public class SKViewModelProviders {

	private static final String DEFAULT_KEY = "android.arch.lifecycle.ViewModelProvider.DefaultKey";

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
			throw new IllegalStateException("Can't create ViewModelProvider for detached fragment");
		}
		return activity;
	}

	@NonNull @MainThread public static <T extends ViewModel> T find(@NonNull Fragment fragment, @NonNull Class<T> modelClass) {
		ViewModelStore viewModelStore = SKViewModelStores.find(fragment);
		if (viewModelStore == null) {
			return null;
		}
		String canonicalName = modelClass.getCanonicalName();
		if (canonicalName == null) {
			throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
		}
		return (T) viewModelStore.get(DEFAULT_KEY + ":" + canonicalName);
	}

	@NonNull @MainThread public static <T extends ViewModel> T find(@NonNull FragmentActivity activity, @NonNull Class<T> modelClass) {
		ViewModelStore viewModelStore = SKViewModelStores.find(activity);
		if (viewModelStore == null) {
			return null;
		}
		String canonicalName = modelClass.getCanonicalName();
		if (canonicalName == null) {
			throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
		}
		return (T) viewModelStore.get(DEFAULT_KEY + ":" + canonicalName);
	}

	@NonNull @MainThread public static ViewModelProvider of(@NonNull Fragment fragment) {
		return of(fragment, null);
	}

	@NonNull @MainThread public static ViewModelProvider of(@NonNull FragmentActivity activity) {
		return of(activity, null);
	}

	@NonNull @MainThread public static ViewModelProvider of(@NonNull Fragment fragment, @Nullable ViewModelProvider.Factory factory) {
		Application application = checkApplication(checkActivity(fragment));
		if (factory == null) {
			factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
		}
		return new ViewModelProvider(SKViewModelStores.of(fragment), factory);
	}

	@NonNull @MainThread public static ViewModelProvider of(@NonNull FragmentActivity activity, @Nullable ViewModelProvider.Factory factory) {
		Application application = checkApplication(activity);
		if (factory == null) {
			factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
		}
		return new ViewModelProvider(SKViewModelStores.of(activity), factory);
	}

	@SuppressWarnings("WeakerAccess")
	@Deprecated
	public static class DefaultFactory extends ViewModelProvider.AndroidViewModelFactory {

		@Deprecated public DefaultFactory(@NonNull Application application) {
			super(application);
		}
	}
}
