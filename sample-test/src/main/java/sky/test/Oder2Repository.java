package sky.test;

import sk.SKRepository;
import sk.livedata.SKPaged;
import sky.SKInput;
import sky.SKProvider;
import sky.SKSingleton;
import sky.test.repository.OderRepository;

/**
 * @author sky
 * @version 1.0 on 2019-02-12 4:10 PM
 * @see Oder2Repository
 */
@SKProvider
public class Oder2Repository extends SKRepository<Oder2Repository> {

	@SKInput SKPaged		skPaged;

	@SKInput OderRepository	oderRepository;

	public void inta() {
		skPaged.pagedBuilder();
	}

	// public SKData<ArrayList<OderAdapter.Model>> oderList() {
	// SKData<ArrayList<OderAdapter.Model>> skData = new SKData<>();
	// repository.oderListHttp(skData);
	// return skData;
	// }
	//
	// @SKHTTP
	// void oderListHttp(SKData<ArrayList<OderAdapter.Model>> skData) {
	//
	// Object limitModel = http(GithubHttp.class).rateLimit().get();
	//
	// L.i("网络数据:" + limitModel);
	//
	// ArrayList<OderAdapter.Model> arrayList = new ArrayList<>();
	// for (int i = 0; i < 100; i++) {
	// OderAdapter.Model model = new OderAdapter.Model();
	// model.img =
	// "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509016656952&di=7ba1379ee3ea1983fe347b71bd46477e&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fac345982b2b7d0a223890680c1ef76094b369a6e.jpg";
	// arrayList.add(model);
	// }
	//
	// skData.postValue(arrayList);
	// }

}
