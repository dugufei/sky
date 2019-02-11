package sk;

import androidx.lifecycle.ViewModel;

import sk.proxy.SKProxy;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 上午11:28
 * @see SKViewModel
 */
public final class SKViewModel extends ViewModel {

	SKProxy skProxy;

	public SKViewModel(SKProxy skProxy) {
		this.skProxy = skProxy;
		SKHelper.bizStore().add(skProxy);
	}

	@Override public void onCleared() {
		super.onCleared();
		SKHelper.bizStore().remove(skProxy);
		skProxy.clearProxy();
		skProxy = null;
	}
}
