package jc.sky.core;

import java.util.concurrent.ConcurrentHashMap;

import jc.sky.common.SKYAppUtil;
import jc.sky.core.model.SkyBizModel;

/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午10:02
 * @see SKYWareHouseManage
 */
public class SKYWareHouseManage {

	static ConcurrentHashMap<String, SkyBizModel>							moduleBiz	= new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String, Class<? extends SKYIModule>>	modules		= new ConcurrentHashMap<>();

	static ConcurrentHashMap<Integer, Boolean>								bizTypes	= new ConcurrentHashMap<>();

	SKYWareHouseManage() {}

	static void clear() {
		moduleBiz.clear();
		modules.clear();
		bizTypes.clear();
	}

	/**
	 * 检查是否是公共方法
	 *
	 * @param bizClazz
	 *            biz
	 * @return true 公共业务 false 不是公共业务
	 */
	static boolean checkBizIsPublic(Class bizClazz) {
		boolean isPublic = false;

		if (bizTypes.get(bizClazz.hashCode()) == null) {
			Class genricType = SKYAppUtil.getClassGenricType(bizClazz, 0);
			if (genricType == null && !bizClazz.isInterface()) { // 表示公共biz
				isPublic = true;
			}
			bizTypes.put(bizClazz.hashCode(), isPublic);
		} else {
			isPublic = bizTypes.get(bizClazz.hashCode());
		}
		return isPublic;
	}
}
