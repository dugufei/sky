package androidx.lifecycle;

import java.util.HashMap;

import sk.SKViewModel;

/**
 * @author sky
 * @version 1.0 on 2018-07-31 下午1:32
 * @see SKViewModelStore
 */
public class SKViewModelStore {

	private final HashMap<String, SKViewModel> mMap = new HashMap<>();

	final void put(String key, SKViewModel viewModel) {
		ViewModel oldViewModel = mMap.put(key, viewModel);
		if (oldViewModel != null) {
			oldViewModel.onCleared();
		}
	}

	final SKViewModel get(String key) {
		return mMap.get(key);
	}

	/**
	 * Clears internal storage and notifies ViewModels that they are no longer used.
	 */
	public final void clear() {
		for (SKViewModel vm : mMap.values()) {
			vm.onCleared();
		}
		mMap.clear();
	}
}
