package android.arch.lifecycle;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import static android.arch.lifecycle.SKHolderFragment.holderFragmentFind;
import static android.arch.lifecycle.SKHolderFragment.holderFragmentFor;

/**
 * @author sky
 * @version 1.0 on 2018-07-25 下午2:08
 * @see SKViewModelStores
 */
public class SKViewModelStores {

	private SKViewModelStores() {}

	@NonNull @MainThread public static ViewModelStore of(@NonNull FragmentActivity activity) {
		if (activity instanceof ViewModelStoreOwner) {
			return ((ViewModelStoreOwner) activity).getViewModelStore();
		}
		return holderFragmentFor(activity).getViewModelStore();
	}

	@NonNull @MainThread public static ViewModelStore of(@NonNull Fragment fragment) {
		if (fragment instanceof ViewModelStoreOwner) {
			return ((ViewModelStoreOwner) fragment).getViewModelStore();
		}
		return holderFragmentFor(fragment).getViewModelStore();
	}

	@NonNull @MainThread public static ViewModelStore find(@NonNull FragmentActivity activity) {
		if (activity instanceof ViewModelStoreOwner) {
			return ((ViewModelStoreOwner) activity).getViewModelStore();
		}
		SKHolderFragment skHolderFragment = holderFragmentFind(activity);

		if (skHolderFragment == null) {
			return null;
		}
		return skHolderFragment.getViewModelStore();
	}

	@NonNull @MainThread public static ViewModelStore find(@NonNull Fragment fragment) {
		if (fragment instanceof ViewModelStoreOwner) {
			return ((ViewModelStoreOwner) fragment).getViewModelStore();
		}
        SKHolderFragment skHolderFragment = holderFragmentFind(fragment);

        if (skHolderFragment == null) {
            return null;
        }
        return skHolderFragment.getViewModelStore();
	}
}
