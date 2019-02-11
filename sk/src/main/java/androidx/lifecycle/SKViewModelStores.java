package androidx.lifecycle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;

import static androidx.lifecycle.SKHolderFragment.holderFragmentFind;
import static androidx.lifecycle.SKHolderFragment.holderFragmentFor;

/**
 * @author sky
 * @version 1.0 on 2018-07-25 下午2:08
 * @see SKViewModelStores
 */
public class SKViewModelStores {

	private SKViewModelStores() {}

	@NonNull @MainThread public static SKViewModelStore of(@NonNull FragmentActivity activity) {
		if (activity instanceof SKViewModelStoreOwner) {
			return ((SKViewModelStoreOwner) activity).getSKViewModelStore();
		}
		return holderFragmentFor(activity).getSKViewModelStore();
	}

	@NonNull @MainThread public static SKViewModelStore of(@NonNull Fragment fragment) {
		if (fragment instanceof SKViewModelStoreOwner) {
			return ((SKViewModelStoreOwner) fragment).getSKViewModelStore();
		}
		return holderFragmentFor(fragment).getSKViewModelStore();
	}

	@NonNull @MainThread public static SKViewModelStore find(@NonNull FragmentActivity activity) {
		if (activity instanceof SKViewModelStoreOwner) {
			return ((SKViewModelStoreOwner) activity).getSKViewModelStore();
		}
		SKHolderFragment skHolderFragment = holderFragmentFind(activity);

		if (skHolderFragment == null) {
			return null;
		}
		return skHolderFragment.getSKViewModelStore();
	}

	@NonNull @MainThread public static SKViewModelStore find(@NonNull Fragment fragment) {
		if (fragment instanceof SKViewModelStoreOwner) {
			return ((SKViewModelStoreOwner) fragment).getSKViewModelStore();
		}
		SKHolderFragment skHolderFragment = holderFragmentFind(fragment);

		if (skHolderFragment == null) {
			return null;
		}
		return skHolderFragment.getSKViewModelStore();
	}
}
