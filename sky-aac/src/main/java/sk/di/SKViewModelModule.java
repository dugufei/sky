package sk.di;

import android.arch.lifecycle.ViewModelProvider;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import sk.SKViewModelFactory;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午9:56
 * @see SKViewModelModule
 */
@Module
public abstract class SKViewModelModule {

	@Singleton @Binds abstract ViewModelProvider.Factory bindViewModelFactory(SKViewModelFactory factory);

}
