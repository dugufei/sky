package jc.sky.core.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jc.sky.SKYHelper;
import jc.sky.core.SKYIMethodRun;
import jc.sky.modules.log.L;

/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午9:03
 * @see SkyBizMethodModel
 */
public class SkyBizMethodModel implements SKYIMethodRun {

	Class	clazz;

	String	methodName;

	Class[]	paramTypes;

	public SkyBizMethodModel(Class clazz, String methodName, Class[] paramTypes) {
		this.methodName = methodName;
		this.paramTypes = paramTypes;
		this.clazz = clazz;
	}
	@Override public void run(Object... params) {
		Object obj = SKYHelper.biz(clazz);
		if (obj == null) {
			if (SKYHelper.isLogOpen()) {
				L.i("项目中的module里名为:" + clazz.getSimpleName() + " 的 biz不存在~所以无法执行");
			}
			return;
		}

		try {
			Method method = clazz.getDeclaredMethod(methodName, paramTypes);
			method.invoke(obj, params);
		} catch (NoSuchMethodException e) {
			if (SKYHelper.isLogOpen()) {
				e.printStackTrace();
				L.i("module里biz执行异常: 方法" + methodName + "获取失败~认真查看一下");
			}
		} catch (InvocationTargetException e) {
			if (SKYHelper.isLogOpen()) {
				e.printStackTrace();
				L.i("module里biz执行异常: 方法" + methodName + "执行失败~认真查看一下");
			}
		} catch (IllegalAccessException e) {
			if (SKYHelper.isLogOpen()) {
				e.printStackTrace();
				L.i("module里biz执行异常: 方法" + methodName + "执行失败~认真查看一下");
			}
		}
	}
}
