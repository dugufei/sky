package sky.example;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Subcomponent;
import sk.di.SKHelperModules;
import sky.example.di.activity.UserActivityModules;
import sky.example.di.fragment.FragmentModules;
import sky.example.di.provider.ProviderModules;
import sky.example.di.viewmodel.ViewModelModules;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:26
 * @see MyComponent
 */
@Singleton
@Component(modules = { UserActivityModules.class, FragmentModules.class, SKHelperModules.class, ViewModelModules.class , ProviderModules.class})
public interface MyComponent {

	void inject(MyApp app);
}
