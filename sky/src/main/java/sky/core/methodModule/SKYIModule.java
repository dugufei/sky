package sky.core.methodModule;

import android.util.SparseArray;


/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午9:46
 * @see SKYIModule
 */
public interface SKYIModule {

	/**
	 * 加载biz info
	 * 
	 * @param concurrentHashMap
	 */
	void loadInto(SparseArray<SKYIMethodRun> concurrentHashMap);
}
