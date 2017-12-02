package sky.core.methodModule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sky.core.L;
import sky.core.SKYHelper;


/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午9:03
 * @see SkyBizMethod
 */
public class SkyBizMethod extends SkyBaseModel {

	public SkyBizMethod(Class clazz, String methodName, Class[] paramTypes) {
		super(clazz, methodName, paramTypes);
	}

	@Override public <T> T run(Object... params) {
		Object obj = SKYHelper.biz(clazz);
		if (obj == null) {
			if (SKYHelper.isLogOpen()) {
				L.d("项目中的module里名为:" + clazz.getSimpleName() + " 的 biz不存在~所以无法执行");
			}
			return null;
		}

		try {
			Method method = clazz.getDeclaredMethod(methodName, paramTypes);
			return (T) method.invoke(obj, params);
		} catch (NoSuchMethodException e) {
			if (SKYHelper.isLogOpen()) {
				L.d("module里biz执行异常: 方法" + methodName + "获取失败~认真查看一下");
				e.printStackTrace();
			}
		} catch (InvocationTargetException e) {
			if (SKYHelper.isLogOpen()) {
				L.d("module里biz执行异常: 方法" + methodName + "执行失败~认真查看一下");
				e.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			if (SKYHelper.isLogOpen()) {
				L.d("module里biz执行异常: 方法" + methodName + "执行失败~认真查看一下");
				e.printStackTrace();
			}
		}
		return null;
	}
}
