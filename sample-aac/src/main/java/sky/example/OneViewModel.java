package sky.example;

import javax.inject.Inject;

import sk.SKData;
import sk.SKViewModel;
import sky.example.bean.User;
import sky.example.providers.UserProvider;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午3:36
 * @see OneViewModel
 */
public class OneViewModel extends SKViewModel {

	@Inject public OneViewModel(UserProvider userProvider) {
		this.userProvider = userProvider;
	}

	UserProvider userProvider;

	public SKData<User> load() {
		return userProvider.getUser();
	}
}
