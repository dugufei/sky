package sky.example;

import android.os.Bundle;

import sk.SKBiz;
import sk.SKHelper;
import sk.SKToast;
import sky.OpenMethod;
import sky.example.di.TestID;
import sky.example.http.model.Model;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午3:36
 * @see MainBiz
 */
public class MainBiz extends SKBiz {

	@Override public void initBiz(Bundle bundle) {}

	@OpenMethod(TestID.A) public void a() {
		SKHelper.toast().show("我被执行了a()");
	}

	@OpenMethod(TestID.B) public void a(int a, Model model) {
		SKHelper.toast().show("我被执行了a()" + a + ":" + model);
	}

	@OpenMethod(TestID.C) public int b() {
		return 222;
	}

	@OpenMethod(TestID.D) public Model c() {
		return new Model();
	}
}
