package android.arch.lifecycle;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;

import sk.SKBiz;
import sk.SKViewModel;

/**
 * @author sky
 * @version 1.0 on 2018-07-25 上午10:49
 * @see SKViewModelProvider
 */
public class SKViewModelProvider {

	public static final String DEFAULT_KEY = "android.arch.lifecycle.ViewModelProvider.DefaultKey";

	public interface Factory {

		@NonNull <T extends SKViewModel> T create(@NonNull Class<T> modelClass, @NonNull final Class<? extends SKBiz> biz, Bundle bundle);
	}

	private final SKViewModelProvider.Factory	mFactory;

	private final SKViewModelStore				mViewModelStore;

	public SKViewModelProvider(@NonNull SKViewModelStoreOwner owner, @NonNull SKViewModelProvider.Factory factory) {
		this(owner.getViewModelStore(), factory);
	}

	public SKViewModelProvider(@NonNull SKViewModelStore store, @NonNull SKViewModelProvider.Factory factory) {
		mFactory = factory;
		this.mViewModelStore = store;
	}

	@NonNull @MainThread public <T extends SKViewModel> T get(@NonNull Class<T> modelClass, @NonNull Class<? extends SKBiz> bizClass, Bundle bundle) {
		String canonicalName = modelClass.getCanonicalName();
		if (canonicalName == null) {
			throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
		}
		return get(DEFAULT_KEY + ":" + canonicalName, modelClass, bizClass, bundle);
	}

	@NonNull @MainThread public <T extends SKViewModel> T get(@NonNull String key, @NonNull Class<T> modelClass, @NonNull final Class<? extends SKBiz> bizClass, Bundle bundle) {
		SKViewModel viewModel = mViewModelStore.get(key);

		if (modelClass.isInstance(viewModel)) {
			// noinspection unchecked
			return (T) viewModel;
		} else {
			// noinspection StatementWithEmptyBody
			if (viewModel != null) {
				// TODO: log a warning.
			}
		}

		viewModel = mFactory.create(modelClass, bizClass, bundle);

		mViewModelStore.put(key, viewModel);
		// noinspection unchecked
		return (T) viewModel;
	}

	public static class NewInstanceFactory implements SKViewModelProvider.Factory {

		@SuppressWarnings("ClassNewInstance") @NonNull @Override public <T extends SKViewModel> T create(@NonNull Class<T> modelClass, @NonNull final Class<? extends SKBiz> biz, Bundle bundle) {
			// noinspection TryWithIdenticalCatches
			try {
				return modelClass.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException("Cannot create an instance of " + modelClass, e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Cannot create an instance of " + modelClass, e);
			}
		}

	}

	public static class AndroidViewModelFactory extends SKViewModelProvider.NewInstanceFactory {

		private static SKViewModelProvider.AndroidViewModelFactory sInstance;

		/**
		 * Retrieve a singleton instance of AndroidViewModelFactory.
		 *
		 * @param application
		 *            an application to pass in {@link AndroidViewModel}
		 * @return A valid {@link ViewModelProvider.AndroidViewModelFactory}
		 */
		@NonNull public static SKViewModelProvider.AndroidViewModelFactory getInstance(@NonNull Application application) {
			if (sInstance == null) {
				sInstance = new SKViewModelProvider.AndroidViewModelFactory(application);
			}
			return sInstance;
		}

		private Application mApplication;

		/**
		 * Creates a {@code AndroidViewModelFactory}
		 *
		 * @param application
		 *            an application to pass in {@link AndroidViewModel}
		 */
		public AndroidViewModelFactory(@NonNull Application application) {
			mApplication = application;
		}

		@NonNull @Override public <T extends SKViewModel> T create(@NonNull Class<T> modelClass, @NonNull final Class<? extends SKBiz> biz, Bundle bundle) {
			if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
				// noinspection TryWithIdenticalCatches
				try {
					return modelClass.getConstructor(Application.class).newInstance(mApplication);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException("Cannot create an instance of " + modelClass, e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Cannot create an instance of " + modelClass, e);
				} catch (InstantiationException e) {
					throw new RuntimeException("Cannot create an instance of " + modelClass, e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException("Cannot create an instance of " + modelClass, e);
				}
			}
			return super.create(modelClass, biz, bundle);
		}
	}

}
