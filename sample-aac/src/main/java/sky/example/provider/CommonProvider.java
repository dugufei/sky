package sky.example.provider;

import sky.SKProvider;
import sky.SKSingleton;
import sky.example.bean.User;
import sky.example.repository.HomeRepository;
import sky.example.repository.UserRepository;

/**
 * @author sky
 * @version 1.0 on 2018-07-14 上午10:41
 * @see CommonProvider
 */
public class CommonProvider {

	@SKSingleton @SKProvider public HomeRepository providerHome() {
		return new HomeRepository();
	}

	@SKSingleton @SKProvider public UserRepository providerUser() {
		return new UserRepository();
	}
}
