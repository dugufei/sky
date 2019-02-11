package sk.livedata;

import androidx.paging.PositionalDataSource;

/**
 * @author sky
 * @version 1.0 on 2018-08-03 下午8:01
 * @see SKPositionDataSource
 */
abstract class SKPositionDataSource<T> extends PositionalDataSource<T> implements SKRetryInterface {
}