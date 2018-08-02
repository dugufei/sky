package sky.example.repository;

import java.util.List;

import retrofit2.SKCall;
import sk.SKAppExecutors;
import sk.livedata.SKData;
import sk.SKHelper;
import sk.SKRepository;
import sky.Interceptor;
import sky.SKHTTP;
import sky.SKIO;
import sky.SKInput;
import sky.example.bean.User;
import sky.example.http.GithubHttp;
import sky.example.http.model.Model;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午10:03
 * @see UserRepository
 */
public class UserRepository extends SKRepository<UserRepository> {

	@SKInput SKAppExecutors skAppExecutors;

	public SKData<User> load() {
		SKData<User> userSKData = new SKData<>();
		User user = new User();
		user.name = "开始";
		userSKData.showLoading();
		userSKData.setValue(user);

		repository.refreshUser(userSKData); // try to refresh data if possible from Github Api

		return userSKData; // return a LiveData directly from the database.
	}

	@SKIO public void refreshUser(SKData<User> userSKData) {

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		User user = userSKData.getValue();
		user.name = "金灿是神" + Math.random();
		userSKData.showContent();
		userSKData.postValue(user);
	}

	@Interceptor(1000) @SKHTTP public void changeUser(SKData<User> skData, String one) {
		skData.loading();

		List<Model> list = http(GithubHttp.class).rateLimit().get();

		SKHelper.toast().show(list.size() + "::::");

		skData.closeloading();
	}
}
