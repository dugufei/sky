package sky.example.di.activity.modules;

import dagger.Module;
import dagger.Provides;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午1:57
 * @see OneModule
 */
@Module
public class OneModule {

	@Provides String provideString() {
		return "天才";
	}
}
