package sk;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;



/**
 * @author sky
 * @version 1.0 on 2018-04-29 下午2:09
 * @see SKFragment
 */
public class SKFragment<M extends SKViewModel> extends Fragment {

//	ViewModelProvider.Factory viewModelFactory;

	@Override public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViewModel();
	}

	protected M model;

	private void initViewModel() {
//		Class clazz = SKCoreUtils.getClassGenricType(this.getClass(), 0);
//		model = (M) ViewModelProviders.of(this, viewModelFactory).get(clazz);
	}

}
