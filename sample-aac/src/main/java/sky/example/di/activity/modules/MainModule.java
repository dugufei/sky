package sky.example.di.activity.modules;

import dagger.Module;
import dagger.Provides;
import sky.example.bean.User;
import sky.example.di.viewmodel.ViewModelModules;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:22
 * @see MainModule
 */
@Module
public class MainModule {

	@Provides User provideUser() {
		User user = new User();
		user.name = "金灿";
		return user;
	}
}
