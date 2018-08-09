package sky.example;

import android.arch.paging.PagedList;
import android.os.Bundle;

import java.util.List;

import sk.SKBiz;
import sk.livedata.SKData;
import sky.SKInput;
import sky.example.http.model.Model;
import sky.example.repository.HomeRepository;
import sky.example.repository.UserRepository;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午3:36
 * @see MainBiz
 */
public class MainBiz extends SKBiz {

	@SKInput UserRepository			userProvider;

	@SKInput HomeRepository			homeRepository;

	SKData<PagedList<List<Model>>>	listSKData;

	@Override public void initBiz(Bundle bundle) {
		listSKData = userProvider.loadModel();
	}

	public void reload() {
		listSKData.retry();
	}
}
