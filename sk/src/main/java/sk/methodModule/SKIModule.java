package sk.methodModule;

import android.util.SparseArray;


/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午9:46
 * @see SKIModule
 */
public interface SKIModule {

	/**
	 * 加载biz info
	 * 
	 * @param concurrentHashMap
	 */
	void loadInto(SparseArray<SKIMethodRun> concurrentHashMap);
}
