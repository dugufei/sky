package sky.example.repository;

import sk.SKAppExecutors;
import sk.SKData;
import sk.SKHelper;
import sk.SKRepository;
import sky.SKIO;
import sky.SKInput;
import sky.example.bean.User;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午10:03
 * @see UserRepository
 */
public class UserRepository extends SKRepository<UserRepository> {

	@SKInput SKAppExecutors skAppExecutors;

	public SKData<User> getUser() {
		SKData<User> userSKData = new SKData<>();
		User user = userSKData.getValue();
		if (user == null) {
			user = new User();
		}
		user.name = "开始";
		userSKData.setValue(user);

		repository.refreshUser(userSKData); // try to refresh data if possible from Github Api

		return userSKData; // return a LiveData directly from the database.
	}

	@SKIO public void refreshUser(SKData<User> userSKData) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		User user = userSKData.getValue();
		user.name = "金灿是神" + Math.random();
		userSKData.postValue(user);

        SKHelper.toast().show("执行啦");

	}

	public void changeUser(SKData<User> skData, String one) {
		User user = skData.getValue();
		user.name = "哈哈哈哈 改变了" + one;
		skData.setValue(user);
	}
}
