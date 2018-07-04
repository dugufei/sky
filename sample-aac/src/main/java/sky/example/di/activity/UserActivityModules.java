package sky.example.di.activity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import sk.di.SKActivityComponent;
import sky.example.MainActivity;
import sky.example.OneActivity;
import sky.example.di.activity.modules.MainModule;
import sky.example.di.activity.modules.OneModule;
import sky.example.di.fragment.FragmentModules;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午11:52
 * @see UserActivityModules
 */
@Module(subcomponents = { SKActivityComponent.class })
public abstract class UserActivityModules {

	@ContributesAndroidInjector(modules = { MainModule.class}) abstract MainActivity mainInjector();

	@ContributesAndroidInjector(modules = { MainModule.class, OneModule.class}) abstract OneActivity oneInjector();
}
