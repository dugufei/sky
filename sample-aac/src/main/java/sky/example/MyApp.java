package sky.example;

import retrofit2.Retrofit;
import sk.ISK;
import sk.SKApp;
import sk.SKCommonView;
import sk.SKDI;
import sk.SKDefaultLibrary;
import sk.SKDefaultManager;
import sky.SKDIApp;
import sky.example.helper.TextBind;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:07
 * @see MyApp
 */

@SKDIApp(SKDefaultLibrary.class)
public class MyApp extends SKApp {

	@Override public void onCreate() {
		super.onCreate();
		SKDI.builder().setApplication(this).setISK(new TextBind()).setSKCommonView(new SKCommonView() {

			@Override public int layoutLoading() {
				return 0;
			}

			@Override public int layoutEmpty() {
				return 0;
			}

			@Override public int layoutBizError() {
				return 0;
			}

			@Override public int layoutHttpError() {
				return 0;
			}
		}).build();
	}
}