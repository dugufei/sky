package sky.core;

import androidx.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import sky.Impl;
import sky.core.exception.SKYArgumentException;
import sky.core.exception.SKYNullPointerException;

/**
 * @author sky
 * @version 版本
 */
final class SKYUtils {

	/**
	 * 获取泛型类型
	 * 
	 * @param clazz
	 *            参数
	 * @param index
	 *            参数
	 * @return 返回值
	 */
	static Class getClassGenricType(final Class clazz, final int index) {
		Type type = clazz.getGenericSuperclass();

		if (!(type instanceof ParameterizedType)) {
			return null;
		}
		// 强制类型转换
		ParameterizedType pType = (ParameterizedType) type;

		Type[] tArgs = pType.getActualTypeArguments();

		if (tArgs.length < 1) {
			return null;
		}

		return (Class) tArgs[index];
	}

	/**
	 * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. 1.因为获取泛型类型-所以增加逻辑判定
	 * 
	 * @param clazz
	 *            参数
	 * @param index
	 *            参数
	 * @return 返回值
	 */
	static Class<Object> getSuperClassGenricType(final Class clazz, final int index) {

		// 返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
		Type[] genType = clazz.getGenericInterfaces();
		Type[] params = null;
		Type baseType = clazz.getGenericSuperclass();
		// 父类
		if (baseType != null && (baseType instanceof ParameterizedType)) {
			params = ((ParameterizedType) baseType).getActualTypeArguments();
			if (index >= params.length || index < 0) {
				return Object.class;
			}
			if (!(params[index] instanceof Class)) {
				return Object.class;
			}

			return (Class<Object>) params[index];
		}
		// 接口
		if (genType == null || genType.length < 1) {
			Type testType = clazz.getGenericSuperclass();
			if (!(testType instanceof ParameterizedType)) {
				return Object.class;
			}
			// 返回表示此类型实际类型参数的 Type 对象的数组。
			params = ((ParameterizedType) testType).getActualTypeArguments();
		} else {
			if (!(genType[index] instanceof ParameterizedType)) {
				return Object.class;
			}
			// 返回表示此类型实际类型参数的 Type 对象的数组。
			params = ((ParameterizedType) genType[index]).getActualTypeArguments();
		}

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}

		return (Class<Object>) params[index];
	}

	/**
	 * 获取实现类
	 *
	 * @param service
	 * @param <D>
	 * @return
	 */
	static <D> Object getImplClass(@NonNull Class<D> service) {
		validateServiceInterface(service);
		try {
			// 获取注解
			Impl impl = service.getAnnotation(Impl.class);
			checkNotNull(impl, "该接口没有指定实现类～");
			/** 加载类 **/
			Class clazz = Class.forName(impl.value().getName());
			Constructor c = clazz.getDeclaredConstructor();
			c.setAccessible(true);
			checkNotNull(clazz, "业务类为空～");
			/** 创建类 **/
			return c.newInstance();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，没有找到业务类！" + e.getMessage());
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，实例化异常！" + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，访问权限异常！" + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，没有找到构造方法！" + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，反射异常！" + e.getMessage());
		}
	}

	static <D> Object getImplClassNotInf(@NonNull Class<D> service) {
		try {
			/** 加载类 **/
			Constructor c = service.getDeclaredConstructor();
			c.setAccessible(true);
			checkNotNull(service, "业务类为空～");
			/** 创建类 **/
			return c.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，实例化异常！" + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，访问权限异常！" + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，没有找到构造方法！" + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，反射异常！" + e.getMessage());
		}
	}

	static <D> Object getImplClass(@NonNull Class<D> service, Class argsClass, Object argsParam) {
		validateServiceInterface(service);
		try {
			// 获取注解
			Impl impl = service.getAnnotation(Impl.class);
			checkNotNull(impl, "该接口没有指定实现类～");
			/** 加载类 **/
			Class clazz = Class.forName(impl.value().getName());
			Constructor c = clazz.getConstructor(argsClass);
			c.setAccessible(true);
			checkNotNull(clazz, "业务类为空～");
			/** 创建类 **/
			return c.newInstance(argsParam);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，没有找到业务类！" + e.getMessage());
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，实例化异常！" + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，访问权限异常！" + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，没有找到构造方法！" + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(String.valueOf(service) + "，反射异常！" + e.getMessage());
		}
	}

	/**
	 * 验证
	 *
	 * @param <T>
	 *            参数
	 *
	 * @param service
	 *            参数
	 * @param <T>
	 *            参数
	 */
	static <T> void validateServiceInterface(Class<T> service) {
		if (!service.isInterface()) {
			throw new IllegalArgumentException("该类不是接口");
		}
	}

	/**
	 * 检查是否为空
	 *
	 * @param <T>
	 *            参数
	 *
	 * @param reference
	 *            参数
	 *
	 * @param errorMessageTemplate
	 *            参数
	 * @return 返回值
	 */
	static <T> T checkNotNull(T reference, String errorMessageTemplate) {
		if (reference == null) {
			throw new SKYNullPointerException(errorMessageTemplate);
		}
		return reference;
	}

	/**
	 * 检查参数
	 *
	 * @param expression
	 *            参数
	 * @param errorMessageTemplate
	 *            参数
	 */
	static void checkArgument(boolean expression, String errorMessageTemplate) {
		if (!expression) {
			throw new SKYArgumentException(errorMessageTemplate);
		}
	}

}
