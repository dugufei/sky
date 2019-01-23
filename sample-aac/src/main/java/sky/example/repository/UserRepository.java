package sky.example.repository;

import android.arch.paging.ItemKeyedDataSource;

import java.util.ArrayList;
import java.util.List;

import sk.SKHelper;
import sk.SKRepository;
import sk.livedata.SKData;
import sky.Interceptor;
import sky.SKHTTP;
import sky.SKIO;
import sky.SKProvider;
import sky.SKSingleton;
import sky.example.bean.User;
import sky.example.http.GithubHttp;
import sky.example.http.model.Model;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午10:03
 * @see UserRepository
 */
@SKSingleton
@SKProvider
public class UserRepository extends SKRepository<UserRepository> {

	public SKData<User> load() {
		SKData<User> userSKData = new SKData<>();
		User user = new User();
		user.name = "开始";
		userSKData.layoutLoading();
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
		userSKData.layoutContent();
		userSKData.postValue(user);
	}

	@Interceptor(1000) @SKHTTP public void changeUser(SKData<User> skData, String one) {
		skData.loading();

		List<Model> list = http(GithubHttp.class).rateLimit().get();

		SKHelper.toast().show(list.size() + "::::");

		skData.closeLoading();
	}

	public List<Model> getInit() {
		// List<Model> list = http(GithubHttp.class).rateLimit().get();
		List<Model> list = new ArrayList<>();
		for (int i = 0; i < 31; i++) {
			Model model = new Model();
			model.id = "初始化---" + i;
			model.img = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509016656952&di=7ba1379ee3ea1983fe347b71bd46477e&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fac345982b2b7d0a223890680c1ef76094b369a6e.jpg";
			list.add(model);
		}
		return list;
	}

	public List<Model> geAfterData(ItemKeyedDataSource.LoadParams<String> params) {
		List<Model> list = http(GithubHttp.class).rateLimit().get();

		for (int i = 0; i < list.size(); i++) {
			list.get(i).id = "追加数据---" + (params.requestedLoadSize + i) + " ::----" + params.requestedLoadSize;
			list.get(
					i).img = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509016656952&di=7ba1379ee3ea1983fe347b71bd46477e&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fac345982b2b7d0a223890680c1ef76094b369a6e.jpg";
		}
		return list;
	}
}
