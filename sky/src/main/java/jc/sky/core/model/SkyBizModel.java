package jc.sky.core.model;

import java.util.concurrent.ConcurrentHashMap;

import jc.sky.SKYHelper;
import jc.sky.core.SKYIMethodRun;
import jc.sky.core.SKYIModuleBiz;
import jc.sky.modules.log.L;

/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午8:40
 * @see SkyBizModel
 */
public class SkyBizModel implements SKYIModuleBiz {

	Class											clazz;

	ConcurrentHashMap<String, SkyBizMethodModel>	methods;	// 模块biz

	public SkyBizModel(Class clazz) {
		this.clazz = clazz;
		methods = new ConcurrentHashMap<>();
	}

	public ConcurrentHashMap<String, SkyBizMethodModel> add(String method, SkyBizMethodModel skyBizMethodModel) {
		methods.put(method, skyBizMethodModel);
		return methods;
	}

	@Override public SKYIMethodRun method(String method) {
		SkyBizMethodModel skyBizMethodModel = methods.get(method);
		if (skyBizMethodModel == null) {
			if (SKYHelper.isLogOpen()) {
				L.i("没有找到module对应的biz里的" + method + "方法");
			}
			return SKYIMethodRun.NONE;
		}
		return skyBizMethodModel;
	}
}