package com.example.sky.oder;

import android.os.Bundle;

import com.example.sky.http.GithubHttp;
import com.example.sky.http.model.Model;
import com.example.sky.oder.adapter.OderAdapter;

import java.util.ArrayList;

import jc.sky.core.SKYBiz;
import retrofit2.Call;
import sky.Background;
import sky.BackgroundType;

public class OderBiz extends SKYBiz<OderActivity> {

	@Override protected void initBiz(Bundle bundle) {
		super.initBiz(bundle);
	}

	@Background(BackgroundType.SINGLEWORK) public void load() {

		Call<Model> limitModelCall = http(GithubHttp.class).rateLimit();
		Model limitModel = httpBody(limitModelCall);

		ArrayList<OderAdapter.Model> arrayList = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			OderAdapter.Model model = new OderAdapter.Model();
			model.limitModel = limitModel;
			model.img = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509016656952&di=7ba1379ee3ea1983fe347b71bd46477e&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fac345982b2b7d0a223890680c1ef76094b369a6e.jpg";
			arrayList.add(model);
		}

		ui().setData(arrayList);
	}
}