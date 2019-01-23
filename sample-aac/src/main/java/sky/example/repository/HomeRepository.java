package sky.example.repository;

import java.util.ArrayList;
import java.util.List;

import sk.SKAppExecutors;
import sk.livedata.SKData;
import sk.SKRepository;
import sky.SKIO;
import sky.SKInput;
import sky.SKProvider;
import sky.SKSingleton;
import sky.example.bean.User;
import sky.example.http.model.Model;

/**
 * @author sky
 * @version 1.0 on 2018-06-07 下午4:13
 * @see HomeRepository
 */
@SKSingleton
@SKProvider
public class HomeRepository extends SKRepository<HomeRepository> {

	@SKInput SKAppExecutors skAppExecutors;

	public SKData<List<Model>> getMM() {

		SKData<List<Model>> listSKData = new SKData<>();

		List<Model> list = new ArrayList<>();
		for (int i = 0; i < 31; i++) {
			Model model = new Model();
			model.id = "初始化---" + i;
			model.img = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509016656952&di=7ba1379ee3ea1983fe347b71bd46477e&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fac345982b2b7d0a223890680c1ef76094b369a6e.jpg";
			list.add(model);
		}

		listSKData.setValue(list);

		return listSKData; // return a LiveData directly from the database.
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
