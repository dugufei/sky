package sk;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * @author sky
 * @version 1.0 on 2018-04-29 下午2:09
 * @see SKFragment
 */
public class SKFragment<M extends SKViewModel> extends Fragment {

	@Inject ViewModelProvider.Factory viewModelFactory;

	@Override public void onCreate(@Nullable Bundle savedInstanceState) {
		AndroidSupportInjection.inject(this);
		super.onCreate(savedInstanceState);
		initViewModel();
	}

	protected M model;

	private void initViewModel() {
		Class clazz = SKCoreUtils.getClassGenricType(this.getClass(), 0);
		model = (M) ViewModelProviders.of(this, viewModelFactory).get(clazz);
	}

}
