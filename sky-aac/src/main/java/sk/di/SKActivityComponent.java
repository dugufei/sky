package sk.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import sk.SKActivity;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 下午4:56
 * @see SKActivityComponent
 */
@Subcomponent(modules = { AndroidSupportInjectionModule.class, SKViewModelModule.class })
public interface SKActivityComponent extends AndroidInjector<SKActivity> {

	@Subcomponent.Builder
	abstract class Builder extends AndroidInjector.Builder<SKActivity> {}

}