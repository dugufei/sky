package sk;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 上午11:26
 * @see SKActivity
 */
public class SKActivity<M extends SKViewModel> extends AppCompatActivity implements HasSupportFragmentInjector {

	@Inject DispatchingAndroidInjector<Fragment>	dispatchingAndroidInjector;

	@Inject ViewModelProvider.Factory				viewModelFactory;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		AndroidInjection.inject(this);
		super.onCreate(savedInstanceState);
		initViewModel();
//		setContentView();
	}

	protected M model;

	private void initViewModel() {
		Class clazz = SKCoreUtils.getClassGenricType(this.getClass(), 0);
		model = (M) ViewModelProviders.of(this, viewModelFactory).get(clazz);
	}

	@Override public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
		return dispatchingAndroidInjector;
	}
}
