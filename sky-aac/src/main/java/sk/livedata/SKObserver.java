package sk.livedata;

import android.arch.lifecycle.Observer;

/**
 * @author sky
 * @version 1.0 on 2018-08-02 下午8:56
 * @see SKObserver
 */
public interface SKObserver<T> extends Observer<T>, SKActionHandler {}
