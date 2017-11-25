package com.example.sky.oder;

import java.util.List;

import com.example.sky.R;
import com.example.sky.http.model.Model;
import com.example.sky.oder.adapter.OderAdapter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import sky.core.SKYActivity;
import sky.core.SKYBuilder;


public class OderActivity extends SKYActivity<OderBiz> {

	@Override protected SKYBuilder build(SKYBuilder initialSKYBuilder) {
		initialSKYBuilder.layoutId(R.layout.activity_oder);
		initialSKYBuilder.recyclerviewId(R.id.rv_oder);
		initialSKYBuilder.recyclerviewLinearLayoutManager(LinearLayoutManager.VERTICAL, null, null);
		initialSKYBuilder.recyclerviewAdapter(new OderAdapter(this));
		return initialSKYBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
		biz().load();
	}

	public void setData(List<Model> data) {
		adapter().setItems(data);
	}
}