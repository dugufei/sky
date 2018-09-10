package sk.livedata;

import android.arch.paging.PageKeyedDataSource;

/**
 * @author sky
 * @version 1.0 on 2018-08-03 下午8:01
 * @see SKPageDataSource
 */
abstract class SKPageDataSource<K, T> extends PageKeyedDataSource<K, T> implements SKRetryInterface {
}