package com.example.sky.dialog;

import com.example.sky.R;

import android.os.Bundle;

import sky.core.SKYBuilder;
import sky.core.SKYDialogFragment;


/**
 * @author sky
 * @date Created on 2017-11-23 下午11:54
 * @version 1.0
 * @Description LoadingDialogFragment - 描述
 */
public class LoadingDialogFragment extends SKYDialogFragment<LoadingBiz> {

	public static final LoadingDialogFragment getInstance() {
		LoadingDialogFragment loadingdialogfragment = new LoadingDialogFragment();
		Bundle bundle = new Bundle();
		loadingdialogfragment.setArguments(bundle);
		return loadingdialogfragment;
	}

	@Override protected SKYBuilder build(SKYBuilder initialSKYBuilder) {
		initialSKYBuilder.layoutId(R.layout.dialogfragment_loading);
		return initialSKYBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {

	}

	@Override protected int getSKYStyle() {
		return R.style.Dialog_Fullscreen;
	}

}