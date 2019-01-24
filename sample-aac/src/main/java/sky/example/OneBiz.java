package sky.example;

import android.arch.paging.PagedList;
import android.os.Bundle;

import java.util.List;

import sk.SKBiz;
import sk.livedata.SKData;
import sky.SKInput;
import sky.example.bean.User;
import sky.example.http.model.Model;
import sky.example.repository.UserRepository;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午3:36
 * @see OneBiz
 */
public class OneBiz extends SKBiz {

	@Override public void initBiz(Bundle bundle) {
		skData = userProvider.load();
		listSKData = userProvider.initPaged();
	}

	@SKInput UserRepository userProvider;

	public SKData<PagedList<List<Model>>> getListSKData() {
		return listSKData;
	}

	private SKData<PagedList<List<Model>>>	listSKData;

	private SKData<User>					skData;

	public SKData<User> getSkData() {
		return skData;
	}

	public void change(String one) {
		userProvider.changeUser(skData, one);
	}

	public void update() {
		userProvider.changeUser(skData, "改改");
	}

	public void reload() {
		listSKData.retry();
	}
}
