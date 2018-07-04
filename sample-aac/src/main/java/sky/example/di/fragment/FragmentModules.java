package sky.example.di.fragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import sky.example.MainFragment;

/**
 * @author sky
 * @version 1.0 on 2018-04-29 下午1:58
 * @see FragmentModules
 */
@Module
public abstract class FragmentModules {

	@ContributesAndroidInjector abstract MainFragment mainFragmentInjector();

}
