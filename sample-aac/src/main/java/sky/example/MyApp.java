package sky.example;

import sk.SKApp;
import sk.SKCommonView;
import sk.SKDI;
import sk.SKDefaultLibrary;
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
				return R.layout.layout_loading;
			}

			@Override public int layoutEmpty() {
				return R.layout.layout_empty;
			}

			@Override public int layoutError() {
				return R.layout.layout_error;
			}

		}).build();
	}
}
