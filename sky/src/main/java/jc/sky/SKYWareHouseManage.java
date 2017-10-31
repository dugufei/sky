package jc.sky;

import java.util.concurrent.ConcurrentHashMap;

import jc.sky.core.SKYIModule;
import jc.sky.core.model.SkyBizModel;

/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午10:02
 * @see SKYWareHouseManage
 */
public class SKYWareHouseManage {

	static ConcurrentHashMap<String, SkyBizModel>					moduleBiz	= new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String, Class<? extends SKYIModule>>	modules		= new ConcurrentHashMap<>();

	SKYWareHouseManage() {}

	static void clear() {
		moduleBiz.clear();
		modules.clear();
	}
}
