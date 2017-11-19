package com.example.sky;

import android.os.Bundle;
import jc.sky.view.SKYFragment;
import jc.sky.view.SKYBuilder;

/**
 * @author sky
 * @date Created on 2017-11-19 下午10:43
 * @version 1.0
 * @Description LoginFragment - 描述
 */
public class LoginFragment extends SKYFragment<LoginBiz> {

    public static final LoginFragment getInstance() {
		LoginFragment loginfragment = new LoginFragment();
		Bundle bundle = new Bundle();
		loginfragment.setArguments(bundle);
		return loginfragment;
	}

    @Override protected SKYBuilder build(SKYBuilder initialSKYBuilder) {
        initialSKYBuilder.layoutId(R.layout.fragment_login);
        return initialSKYBuilder;
    }
    @Override protected void initData(Bundle savedInstanceState) {

    }
    
}