package sky.example;

import sk.SKData;
import sk.SKViewModel;
import sky.SKInput;
import sky.example.bean.User;
import sky.example.repository.UserRepository;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午3:36
 * @see MainViewModel
 */
public class MainViewModel extends SKViewModel {

	@SKInput UserRepository userProvider;

	SKData<User> skData;


	public SKData<User> load() {
		skData = userProvider.getUser();
		return skData;
	}


	public void change(){
		userProvider.changeUser(skData);
	}
}
