package sky.example;

import sk.SKData;
import sk.SKViewModel;
import sky.SKInput;
import sky.example.bean.User;
import sky.example.repository.HomeRepository;
import sky.example.repository.UserRepository;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午3:36
 * @see OneViewModel
 */
public class OneViewModel extends SKViewModel {

	@SKInput UserRepository	userProvider;

	SKData<User>			skData;

	public void change(String one) {
		userProvider.changeUser(skData, one);
	}

	@Override public void init() {
		skData = userProvider.getUser();
	}

	public void update(){
		userProvider.changeUser(skData, "改改");
	}
}
