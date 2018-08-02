package sky.example.repository;

import sk.SKAppExecutors;
import sk.livedata.SKData;
import sk.SKRepository;
import sky.SKIO;
import sky.SKInput;
import sky.example.bean.User;

/**
 * @author sky
 * @version 1.0 on 2018-06-07 下午4:13
 * @see HomeRepository
 */
public class HomeRepository extends SKRepository<HomeRepository> {

	@SKInput SKAppExecutors skAppExecutors;

	public SKData<User> getMM(String value) {
		SKData<User> skListing = new SKData<>();
		User user = new User();
		user.name = "开始" + value;
		skListing.setValue(user);

		repository.refreshUser(skListing);
		return skListing; // return a LiveData directly from the database.
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
	}

	public void reloadMM(SKData<User> skListing) {
		User user = skListing.getValue();
		user.name = "重新开始";
		skListing.setValue(user);
	}
}
