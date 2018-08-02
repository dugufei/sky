package sky.example.repository;

import java.util.List;

import retrofit2.SKCall;
import sk.SKAppExecutors;
import sk.SKData;
import sk.SKHelper;
import sk.SKRepository;
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

		User user = userSKData.getValue();
		user.name = "金灿是神" + Math.random();
		userSKData.postValue(user);

		SKHelper.toast().show("执行啦");

	}

	@SKHTTP public void changeUser(SKData<User> skData, String one) {
		SKCall<List<Model>> skCall = http(GithubHttp.class).rateLimit();


		List<Model> list = skCall.get();
		List<Model> list1 = skCall.get();

		SKHelper.toast().show(list.size()+"::::");
//		User user = skData.getValue();
//		user.name = "哈哈哈哈 改变了" + one;
//		skData.setValue(user);
	}
}
