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

	@SKInput SKAppExecutors	skAppExecutors;

	public SKData<User> getUser() {
		SKData<User> skListing = new SKData<>();

		User user = new User();
		user.name = "开始";
		skListing.setValue(user);
		refreshUser(skListing); // try to refresh data if possible from Github Api
		return skListing; // return a LiveData directly from the database.
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

	public SKData<User> getUser1() {
		SKData<User> skListing = new SKData<>();

		User user = new User();
		user.name = "开始开始 开始1";
		skListing.setValue(user);
		refreshUser(skListing); // try to refresh data if possible from Github Api
		return skListing; // return a LiveData directly from the database.
	}

	public void changeUser(SKData<User> skData) {
		User user = skData.getValue();
		user.name ="哈哈哈哈 改变了";
		skData.setValue(user);
	}
}
