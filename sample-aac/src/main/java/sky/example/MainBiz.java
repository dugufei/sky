package sky.example;

import android.arch.paging.PagedList;
import android.os.Bundle;

import java.util.List;

import sk.SKBiz;
import sk.SKHelper;
import sk.livedata.SKData;
import sky.SKHTTP;
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

	SKData<List<Model>>				listModel;

	SKData<Integer>					itemPositoin;

	@Override public void initBiz(Bundle bundle) {
		listSKData = userProvider.loadModel();
		listModel = homeRepository.getMM();
		itemPositoin = new SKData<>();
	}

	public void reload() {
		listSKData.retry();
	}

	@SKHTTP public void test() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SKHelper.toast().show("提示 提示");
	}

	public void change(int position) {
		PagedList<List<Model>> pagedList = listSKData.getValue();

		Model model = (Model) pagedList.get(position);
		model.id = "改了改了";

		itemPositoin.setValue(position);
	}

	public void refresh() {
		listSKData.getValue().getDataSource().invalidate();
	}
}
