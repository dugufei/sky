package sk.livedata;

import androidx.paging.ItemKeyedDataSource;

/**
 * @author sky
 * @version 1.0 on 2018-08-03 下午8:01
 * @see SKItemDataSource
 */
abstract class SKItemDataSource<K, T> extends ItemKeyedDataSource<K, T> implements SKRetryInterface {
}