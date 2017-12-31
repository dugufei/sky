package sky.core.methodModule;

import java.util.concurrent.ConcurrentHashMap;

import sky.core.L;
import sky.core.SKYHelper;

/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午8:40
 * @see SkyMethodModel
 */
public class SkyMethodModel implements SKYIModuleMethod {

	Class										clazz;

	ConcurrentHashMap<String, SkyBizMethod>		bizMethods;		// 模块biz

	ConcurrentHashMap<String, SkyDisplayMethod>	displayMethods;	// 模块biz

	int											type;			// 0 biz,1 display

	public SkyMethodModel(Class clazz, int type) {
		this.clazz = clazz;
		this.type = type;
	}

	public ConcurrentHashMap<String, SkyBizMethod> add(String method, SkyBizMethod skyBizMethodModel) {
		if (bizMethods == null) {
			bizMethods = new ConcurrentHashMap<>();
		}
		bizMethods.put(method, skyBizMethodModel);
		return bizMethods;
	}

	public ConcurrentHashMap<String, SkyDisplayMethod> add(String method, SkyDisplayMethod skyBizMethodModel) {
		if (displayMethods == null) {
			displayMethods = new ConcurrentHashMap<>();
		}
		displayMethods.put(method, skyBizMethodModel);
		return displayMethods;
	}

	@Override public SKYIMethodRun method(String method) {
		switch (type) {
			case 0:
				SkyBizMethod skyBizMethodModel = bizMethods.get(method);
				if (skyBizMethodModel != null) {
					return skyBizMethodModel;
				}
				if (SKYHelper.isLogOpen()) {
					L.d("没有找到module对应的biz里的" + method + "方法");
				}
				break;
			case 1:
				SkyDisplayMethod skyDisplayMethod = displayMethods.get(method);
				if (skyDisplayMethod != null) {
					return skyDisplayMethod;
				}
				if (SKYHelper.isLogOpen()) {
					L.d("没有找到module对应的display里的" + method + "方法");
				}
				break;
		}
		return SKYIMethodRun.NONE;
	}
}
