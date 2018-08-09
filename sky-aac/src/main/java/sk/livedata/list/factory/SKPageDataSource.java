package sk.livedata.list.factory;

import android.arch.paging.PageKeyedDataSource;

/**
 * @author sky
 * @version 1.0 on 2018-08-03 下午8:01
 * @see SKPageDataSource
 */
public abstract class SKPageDataSource<K, T> extends PageKeyedDataSource<K, T> implements SKRetryInterface {
}