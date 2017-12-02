package sky.core.methodModule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import sky.core.L;
import sky.core.SKYHelper;
import sky.core.exception.SKYArgumentException;

/**
 * @author sky
 * @version 1.0 on 2017-10-30 下午9:03
 * @see SkyDisplayMethod
 */
public class SkyDisplayMethod extends SkyBaseModel {

	public SkyDisplayMethod(Class clazz, String methodName, Class[] paramTypes) {
		super(clazz, methodName, paramTypes);
	}

	@Override public <T> T run(Object... params) {
		try {
			Method method = clazz.getDeclaredMethod(methodName, paramTypes);

			if (!Modifier.isStatic(method.getModifiers())) {
				throw new SKYArgumentException();
			}

			return (T) method.invoke(null, params);
		} catch (NoSuchMethodException e) {
			if (SKYHelper.isLogOpen()) {
				L.d("module里display执行异常: 方法" + methodName + "获取失败~认真查看一下");
				e.printStackTrace();
			}
		} catch (InvocationTargetException e) {
			if (SKYHelper.isLogOpen()) {
				L.d("module里display执行异常: 方法" + methodName + "执行失败~认真查看一下");
				e.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			if (SKYHelper.isLogOpen()) {
				L.d("module里display执行异常: 方法" + methodName + "执行失败~认真查看一下");
				e.printStackTrace();
			}
		} catch (SKYArgumentException e){
			if (SKYHelper.isLogOpen()) {
				L.d("module里display执行异常: 方法" + methodName + "必须是静态方法 static");
				e.printStackTrace();
			}
		}
		return null;
	}
}
