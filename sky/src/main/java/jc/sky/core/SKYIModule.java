package jc.sky.core;

import java.util.concurrent.ConcurrentHashMap;

import jc.sky.core.model.SkyBizModel;

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
	void loadInto(ConcurrentHashMap<String, SkyBizModel> concurrentHashMap);
}
