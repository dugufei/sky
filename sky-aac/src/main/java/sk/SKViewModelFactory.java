package sk;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.Map;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午9:16
 * @see SKViewModelFactory
 */
public class SKViewModelFactory extends ViewModelProvider.NewInstanceFactory {

	@NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		T t;
		try {
			t = modelClass.newInstance();
			SKInputs.input(t);
		} catch (InstantiationException e) {
			throw new RuntimeException("Cannot create an instance of " + modelClass, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Cannot create an instance of " + modelClass, e);
		}

		return t;
	}

}
