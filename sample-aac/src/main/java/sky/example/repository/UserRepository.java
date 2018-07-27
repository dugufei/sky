package sky.example.repository;

import sk.SKAppExecutors;
import sk.SKData;
import sky.SKInput;
import sky.example.bean.User;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午10:03
 * @see UserRepository
 */
public class UserRepository {

	@SKInput SKAppExecutors skAppExecutors;

	public SKData<User> getUser() {
		SKData<User> userSKData = new SKData<>();

		User user = userSKData.getValue();
		if (user == null) {
			user = new User();
		}
		user.name = "开始";
		userSKData.setValue(user);
		refreshUser(userSKData); // try to refresh data if possible from Github Api
		return userSKData; // return a LiveData directly from the database.
	}

	private void refreshUser(SKData<User> userSKData) {
		skAppExecutors.diskIO().execute(() -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			User user = userSKData.getValue();
			user.name = "金灿是神" + Math.random();
			userSKData.postValue(user);
		});
	}

	public void changeUser(SKData<User> skData, String one) {
		skAppExecutors.diskIO().execute(() -> {

			User user = null;
			user.name = "哈哈哈哈 改变了" + one;
			skData.setValue(user);
		});
	}
}
