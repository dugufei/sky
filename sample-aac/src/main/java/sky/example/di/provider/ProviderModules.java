package sky.example.di.provider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sk.SKAppExecutors;
import sky.example.providers.UserProvider;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午10:25
 * @see ProviderModules
 */
@Module
public class ProviderModules {

	@Provides UserProvider userProvider(SKAppExecutors skAppExecutors) {
		return new UserProvider(skAppExecutors);
	}
}
