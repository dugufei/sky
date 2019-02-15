package sky.example.fragment;

import androidx.paging.PagedList;
import android.os.Bundle;

import java.util.List;

import sk.SKBiz;
import sk.SKHelper;
import sk.livedata.SKData;
import sky.SKHTTP;
import sky.SKInput;
import sky.example.helper.TextHelper;
import sky.example.http.model.Model;
import sky.example.repository.HomeRepository;
import sky.example.repository.UserRepository;
import sky.test.Oder2Repository;
import sky.test.model.Model1;
import sky.test.model.Model2;
import sky.test.model.Model3;
import sky.test.repository.OderRepository;

/**
 * @author sky
 * @version 1.0 on 2019-01-24 9:56 PM
 * @see HelloBiz
 */
public class HelloBiz extends SKBiz {

	@SKInput UserRepository					userProvider;

	@SKInput HomeRepository					homeRepository;

	@SKInput OderRepository					oderRepository;

	@SKInput Oder2Repository				oder2Repository;

	@SKInput Model							model;

	@SKInput Model1							model1;

	@SKInput Model2							model2;

	@SKInput Model3							model3;

	private SKData<PagedList<List<Model>>>	listSKData;

	private SKData<List<Model>>				listModel;

	private SKData<Integer>					itemPositoin;

	@Override public void initBiz(Bundle bundle) {
		listSKData = userProvider.initPaged();
		listModel = homeRepository.getMM();
		itemPositoin = new SKData<>();
		oderRepository.inta();
		oder2Repository.inta();
	}

	public SKData<PagedList<List<Model>>> getListSKData() {
		return listSKData;
	}

	public SKData<List<Model>> getListModel() {
		return listModel;
	}

	public SKData<Integer> getItemPositoin() {
		return itemPositoin;
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
		model.id = "改了改了" + TextHelper.abc().MM;

		itemPositoin.setValue(position);
	}

	public void refresh() {
		listSKData.getValue().getDataSource().invalidate();
	}

}
