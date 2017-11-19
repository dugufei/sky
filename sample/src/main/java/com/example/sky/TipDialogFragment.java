package com.example.sky;

import android.os.Bundle;
import jc.sky.view.SKYDialogFragment;
import jc.sky.view.SKYBuilder;

/**
 * @author sky
 * @date Created on 2017-11-19 下午10:43
 * @version 1.0
 * @Description TipDialogFragment - 描述
 */
public class TipDialogFragment extends SKYDialogFragment<TipBiz> {

    public static final TipDialogFragment getInstance() {
		TipDialogFragment tipdialogfragment = new TipDialogFragment();
		Bundle bundle = new Bundle();
		tipdialogfragment.setArguments(bundle);
		return tipdialogfragment;
	}

    @Override protected SKYBuilder build(SKYBuilder initialSKYBuilder) {
        initialSKYBuilder.layoutId(R.layout.dialogfragment_tip);
        return initialSKYBuilder;
    }
    @Override protected void initData(Bundle savedInstanceState) {

    }
    	
@Override protected int getSKYStyle() {
        return 0;
    }

}