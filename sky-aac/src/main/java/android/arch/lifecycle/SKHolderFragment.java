package android.arch.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sky
 * @version 1.0 on 2018-07-25 下午2:02
 * @see SKHolderFragment
 */
public class SKHolderFragment extends Fragment implements SKViewModelStoreOwner {

	private static final String												LOG_TAG						= "ViewModelStores";

	private static final SKHolderFragment.SKHolderFragmentManager			sSKHolderFragmentManager	= new SKHolderFragment.SKHolderFragmentManager();

	/**
	 * @hide
	 */
	@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) public static final String	HOLDER_TAG					= "android.arch.lifecycle.state.StateProviderSKHolderFragment";

	private SKViewModelStore												mViewModelStore				= new SKViewModelStore();

	public SKHolderFragment() {
		setRetainInstance(true);
	}

	@Override public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sSKHolderFragmentManager.holderFragmentCreated(this);
	}

	@Override public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override public void onDestroy() {
		super.onDestroy();
		mViewModelStore.clear();
	}

	@NonNull @Override public SKViewModelStore getSKViewModelStore() {
		return mViewModelStore;
	}

	/**
	 * @hide
	 */
	@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) public static SKHolderFragment holderFragmentFor(FragmentActivity activity) {
		return sSKHolderFragmentManager.holderFragmentFor(activity);
	}

	@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) public static SKHolderFragment holderFragmentFind(FragmentActivity activity) {
		return sSKHolderFragmentManager.holderFragmentFind(activity);
	}

	/**
	 * @hide
	 */
	@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) public static SKHolderFragment holderFragmentFor(Fragment fragment) {
		return sSKHolderFragmentManager.holderFragmentFor(fragment);
	}

	@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) public static SKHolderFragment holderFragmentFind(Fragment fragment) {
		return sSKHolderFragmentManager.holderFragmentFind(fragment);
	}

	@SuppressWarnings("WeakerAccess")
	static class SKHolderFragmentManager {

		private Map<Activity, SKHolderFragment>				mNotCommittedActivityHolders	= new HashMap<>();

		private Map<Fragment, SKHolderFragment>				mNotCommittedFragmentHolders	= new HashMap<>();

		private Application.ActivityLifecycleCallbacks		mActivityCallbacks				= new EmptyActivityLifecycleCallbacks() {

																								@Override public void onActivityDestroyed(Activity activity) {
																									SKHolderFragment fragment = mNotCommittedActivityHolders.remove(activity);
																									if (fragment != null) {
																										Log.e(LOG_TAG, "Failed to save a ViewModel for " + activity);
																									}
																								}
																							};

		private boolean										mActivityCallbacksIsAdded		= false;

		private FragmentManager.FragmentLifecycleCallbacks	mParentDestroyedCallback		= new FragmentManager.FragmentLifecycleCallbacks() {

																								@Override public void onFragmentDestroyed(FragmentManager fm, Fragment parentFragment) {
																									super.onFragmentDestroyed(fm, parentFragment);
																									SKHolderFragment fragment = mNotCommittedFragmentHolders.remove(parentFragment);
																									if (fragment != null) {
																										Log.e(LOG_TAG, "Failed to save a ViewModel for " + parentFragment);
																									}
																								}
																							};

		void holderFragmentCreated(Fragment holderFragment) {
			Fragment parentFragment = holderFragment.getParentFragment();
			if (parentFragment != null) {
				mNotCommittedFragmentHolders.remove(parentFragment);
				parentFragment.getFragmentManager().unregisterFragmentLifecycleCallbacks(mParentDestroyedCallback);
			} else {
				mNotCommittedActivityHolders.remove(holderFragment.getActivity());
			}
		}

		private static SKHolderFragment findSKHolderFragment(FragmentManager manager) {
			if (manager.isDestroyed()) {
				throw new IllegalStateException("Can't access ViewModels from onDestroy");
			}

			Fragment fragmentByTag = manager.findFragmentByTag(HOLDER_TAG);
			if (fragmentByTag != null && !(fragmentByTag instanceof SKHolderFragment)) {
				throw new IllegalStateException("Unexpected " + "fragment instance was returned by HOLDER_TAG");
			}
			return (SKHolderFragment) fragmentByTag;
		}

		private static SKHolderFragment createSKHolderFragment(FragmentManager fragmentManager) {
			SKHolderFragment holder = new SKHolderFragment();
			fragmentManager.beginTransaction().add(holder, HOLDER_TAG).commitAllowingStateLoss();
			return holder;
		}

		SKHolderFragment holderFragmentFor(FragmentActivity activity) {
			FragmentManager fm = activity.getSupportFragmentManager();
			SKHolderFragment holder = findSKHolderFragment(fm);
			if (holder != null) {
				return holder;
			}
			holder = mNotCommittedActivityHolders.get(activity);
			if (holder != null) {
				return holder;
			}

			if (!mActivityCallbacksIsAdded) {
				mActivityCallbacksIsAdded = true;
				activity.getApplication().registerActivityLifecycleCallbacks(mActivityCallbacks);
			}
			holder = createSKHolderFragment(fm);
			mNotCommittedActivityHolders.put(activity, holder);
			return holder;
		}

		SKHolderFragment holderFragmentFor(Fragment parentFragment) {
			FragmentManager fm = parentFragment.getChildFragmentManager();
			SKHolderFragment holder = findSKHolderFragment(fm);
			if (holder != null) {
				return holder;
			}
			holder = mNotCommittedFragmentHolders.get(parentFragment);
			if (holder != null) {
				return holder;
			}

			parentFragment.getFragmentManager().registerFragmentLifecycleCallbacks(mParentDestroyedCallback, false);
			holder = createSKHolderFragment(fm);
			mNotCommittedFragmentHolders.put(parentFragment, holder);
			return holder;
		}

		public SKHolderFragment holderFragmentFind(FragmentActivity activity) {
			FragmentManager fm = activity.getSupportFragmentManager();
			SKHolderFragment holder = findSKHolderFragment(fm);
			if (holder != null) {
				return holder;
			}
			holder = mNotCommittedActivityHolders.get(activity);
			if (holder != null) {
				return holder;
			}
			return null;
		}

		public SKHolderFragment holderFragmentFind(Fragment parentFragment) {
			FragmentManager fm = parentFragment.getChildFragmentManager();
			SKHolderFragment holder = findSKHolderFragment(fm);
			if (holder != null) {
				return holder;
			}
			holder = mNotCommittedFragmentHolders.get(parentFragment);
			if (holder != null) {
				return holder;
			}
			return null;
		}
	}
}
