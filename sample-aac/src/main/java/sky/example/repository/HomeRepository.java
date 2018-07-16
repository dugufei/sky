package sky.example.repository;

import sk.SKAppExecutors;
import sk.SKData;
import sky.SKInput;
import sky.example.bean.User;

/**
 * @author sky
 * @version 1.0 on 2018-06-07 下午4:13
 * @see HomeRepository
 */
public class HomeRepository {

	@SKInput SKAppExecutors skAppExecutors;

	public SKData<User> getMM(String value) {
		SKData<User> skListing = new SKData<>();

		if (value == "你好") {

		} else if (value == "不好") {
			User user = new User();
			user.name = "开始";
			skListing.setValue(user);
			refreshUser(skListing);
		}

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
}
