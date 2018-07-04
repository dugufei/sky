package sky.example.di.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import sk.SKViewModelFactory;
import sk.di.SKViewModelKey;
import sk.di.SKViewModelModule;
import sky.example.MainViewModel;
import sky.example.OneViewModel;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午10:37
 * @see ViewModelModules
 */
@Module(includes = SKViewModelModule.class)
public abstract class ViewModelModules {

	@Binds @IntoMap @SKViewModelKey(MainViewModel.class) abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

	@Binds @IntoMap @SKViewModelKey(OneViewModel.class) abstract ViewModel bindOneViewModel(OneViewModel oneViewModel);


}
