package sk;

import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;

/**
 * @author sky
 * @version 1.0 on 2018-04-25 下午2:18
 * @see SKListing 数据列表
 */
public class SKListing<T> extends LiveData<T> {

	public SKData<PagedList<T>>	pagedList;

	public SKData<SKWorkState>	workState	= new SKData<>();

	public SKData<SKViewState>	viewState	= new SKData<>();

}
