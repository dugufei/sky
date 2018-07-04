package sky.example.providers;

import javax.inject.Inject;
import javax.inject.Singleton;

import sk.SKAppExecutors;
import sk.SKData;
import sky.example.bean.User;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午10:03
 * @see UserProvider
 */
@Singleton
public class UserProvider {

	SKAppExecutors skAppExecutors;

	@Inject public UserProvider(SKAppExecutors skAppExecutors) {
		this.skAppExecutors = skAppExecutors;
	}


	public SKData<User> getUser() {
		SKData<User> skListing = new SKData<>();

		User user = new User();
		user.name = "开始" ;
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
			user.name = "金灿是神"+Math.random();
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
