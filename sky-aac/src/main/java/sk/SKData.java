package sk;

import android.arch.lifecycle.LiveData;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 下午12:16
 * @see SKData 数据结构
 */
public class SKData<T> extends LiveData<T> {

	@Override public void postValue(T value) {
		super.postValue(value);
	}

	@Override public void setValue(T value) {
		super.setValue(value);
	}
}
