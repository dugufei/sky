package sky.example.repository;

import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import sk.L;
import sk.SKHelper;
import sk.SKRepository;
import sk.livedata.SKData;
import sk.livedata.SKItemSourceFactory;
import sk.livedata.SKPaged;
import sk.livedata.SKPagedBuilder;
import sk.livedata.SKSourceState;
import sky.Interceptor;
import sky.SKHTTP;
import sky.SKIO;
import sky.SKInput;
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

	List<Model> getInit() {
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

	List<Model> geAfterData(ItemKeyedDataSource.LoadParams<String> params) {
		List<Model> list = http(GithubHttp.class).rateLimit().get();

		for (int i = 0; i < list.size(); i++) {
			list.get(i).id = "追加数据---" + (params.requestedLoadSize + i) + " ::----" + params.requestedLoadSize;
			list.get(
					i).img = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509016656952&di=7ba1379ee3ea1983fe347b71bd46477e&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fac345982b2b7d0a223890680c1ef76094b369a6e.jpg";
		}
		return list;
	}

	public SKData initPaged() {
		SKPagedBuilder skPagedBuilder = skPaged.pagedBuilder();
		skPagedBuilder.setPageSie(25);
		skPagedBuilder.setSource(new SKItemSourceFactory<String, Model>() {

			@Override public void init(@NonNull ItemKeyedDataSource.LoadInitialParams<String> params, @NonNull ItemKeyedDataSource.LoadInitialCallback<Model> callback) {
				loading();
				callback.onResult(getInit());
				layoutContent();
				closeLoading();
			}

			@Override public void before(@NonNull ItemKeyedDataSource.LoadParams<String> params, @NonNull ItemKeyedDataSource.LoadCallback<Model> callback) {

			}

			@Override public void after(@NonNull ItemKeyedDataSource.LoadParams<String> params, @NonNull ItemKeyedDataSource.LoadCallback<Model> callback) {
				L.i("after 我执行了~~");
				netWorkRunning();
				callback.onResult(geAfterData(params));
				netWorkSuccess();
			}

			@Override public void error(@NonNull SKSourceState skSourceState) {

				switch (skSourceState) {
					case INIT:
						closeLoading();
						layoutError();
						break;
					case AFTER:
						netWorkFailed("加载失败了");
						break;
					default:
						break;
				}
			}

			@Override public String key(@NonNull Model item) {
				return item.id;
			}

		});

		return skPagedBuilder.build();
	}

}

