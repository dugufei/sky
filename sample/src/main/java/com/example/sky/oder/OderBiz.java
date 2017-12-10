package com.example.sky.oder;

import android.os.Bundle;

import com.example.sky.http.GithubHttp;
import com.example.sky.http.model.Model;

import java.util.List;

import retrofit2.Call;
import sky.Background;
import sky.BackgroundType;
import sky.core.SKYBiz;
import sky.core.exception.SKYHttpException;

public class OderBiz extends SKYBiz<OderActivity> {

	@Override protected void initBiz(Bundle bundle) {
		super.initBiz(bundle);
	}

	@Background(BackgroundType.HTTP) public void load() {

		Call<List<Model>> limitModelCall = http(GithubHttp.class).rateLimit();
		List<Model> limitModel = httpBody(limitModelCall);
		for (int i = 0; i < limitModel.size(); i++) {
			limitModel.get(i).img = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509016656952&di=7ba1379ee3ea1983fe347b71bd46477e&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fac345982b2b7d0a223890680c1ef76094b369a6e.jpg";
		}

		httpCancel();
		ui().setData(limitModel);
	}

	@Override
	public boolean interceptHttpError(String method, SKYHttpException sKYHttpException) {
		return super.interceptHttpError(method, sKYHttpException);
	}
}