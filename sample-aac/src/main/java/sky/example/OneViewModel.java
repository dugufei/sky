package sky.example;

import sk.SKData;
import sk.SKViewModel;
import sky.example.bean.User;
import sky.example.repository.UserRepository;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午3:36
 * @see OneViewModel
 */
public class OneViewModel extends SKViewModel {

	public OneViewModel(UserRepository userProvider) {
		this.userProvider = userProvider;
	}

	UserRepository userProvider;

	public SKData<User> load() {
		return userProvider.getUser();
	}
}
