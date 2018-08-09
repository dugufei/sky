package sky.example.repository;

import android.arch.paging.ItemKeyedDataSource;
import android.arch.paging.PagedList;
import android.arch.paging.SKPagedList;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import sk.L;
import sk.SKHelper;
import sk.SKRepository;
import sk.livedata.SKPagedBuilder;
import sk.livedata.SKData;
import sk.livedata.list.SKPaged;
import sk.livedata.list.SKSourceState;
import sk.livedata.list.factory.SKItemFactory;
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

	@SKInput SKPaged skPaged;

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

	public SKData loadModel() {
		final SKData<SKPagedList<List<Model>>> skData = new SKData<>();

		SKPagedBuilder skPagedBuilder = skPaged.pagedBuilder();
		skPagedBuilder.setPageSie(25);
		skPagedBuilder.setFactory(new SKItemFactory<String, Model>() {

			@Override public void init(@NonNull ItemKeyedDataSource.LoadInitialParams<String> params, @NonNull ItemKeyedDataSource.LoadInitialCallback<Model> callback) {
				skData.loading();
//				List<Model> list = http(GithubHttp.class).rateLimit().get();
				List<Model> list = new ArrayList<>();
				for (int i = 0; i < 31; i++) {
					Model model = new Model();
					model.id = "初始化---" + i + "::----" + params.requestedInitialKey + " ::----" + params.requestedLoadSize;
					model.img = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509016656952&di=7ba1379ee3ea1983fe347b71bd46477e&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fac345982b2b7d0a223890680c1ef76094b369a6e.jpg";
					list.add(model);
				}

				callback.onResult(list);
				skData.layoutContent();
				skData.closeLoading();
			}

			@Override public void before(@NonNull ItemKeyedDataSource.LoadParams<String> params, @NonNull ItemKeyedDataSource.LoadCallback<Model> callback) {

			}

			@Override public void after(@NonNull ItemKeyedDataSource.LoadParams<String> params, @NonNull ItemKeyedDataSource.LoadCallback<Model> callback) {
				L.i("after 我执行了~~");
				skData.netWorkRunning();
				List<Model> list = http(GithubHttp.class).rateLimit().get();

				for (int i = 0; i < list.size(); i++) {
					list.get(i).id = "追加数据---" + (params.requestedLoadSize + i) + " ::----" + params.requestedLoadSize;
					list.get(
							i).img = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509016656952&di=7ba1379ee3ea1983fe347b71bd46477e&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fac345982b2b7d0a223890680c1ef76094b369a6e.jpg";
				}

				callback.onResult(list);

				skData.netWorkSuccess();
			}

			@Override public void error(@NonNull SKSourceState skSourceState) {

				switch (skSourceState) {
					case INIT:
						skData.closeLoading();
						skData.layoutError();
						break;
					case AFTER:
						skData.netWorkFailed("加载失败了");
						break;
				}
			}

			@Override public String key(@NonNull Model item) {
				return item.id;
			}

		});

		return skPagedBuilder.build(skData);
	}
}
