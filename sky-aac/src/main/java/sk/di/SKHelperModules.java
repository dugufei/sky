package sk.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sk.SKAppExecutors;
import sk.SKData;
import sk.SKViewModel;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午2:49
 * @see SKHelperModules
 */
@Module
public class SKHelperModules {

	@Provides public SKAppExecutors provideSKAppExecutors() {
		return new SKAppExecutors();
	}

	@Singleton @Provides public SKData provideSKData(SKAppExecutors skAppExecutors) {
		SKData skData = new SKData();
		return skData;
	}

	@Provides public SKViewModel provideViewModel() {
		SKViewModel skData = new SKViewModel();
		return skData;
	}

}
