package android.arch.lifecycle;

import android.support.annotation.NonNull;

/**
 * @author sky
 * @version 1.0 on 2018-07-31 下午1:36
 * @see SKViewModelStoreOwner
 */
public interface SKViewModelStoreOwner {

	/**
	 * Returns owned {@link ViewModelStore}
	 *
	 * @return a {@code ViewModelStore}
	 */
	@NonNull SKViewModelStore getViewModelStore();
}
