package sky.kotlin.core

import sky.kotlin.core.exception.SKNullPointerException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * @author sky
 * @version 1.0 on 2018-01-26 下午4:09
 * @see SKUtils
 */
object SKUtils {
    /**
     * 获取泛型类型
     *
     * @param clazz
     * 参数
     * @param index
     * 参数
     * @return 返回值
     */
    fun getClassGenricType(clazz: Class<*>, index: Int): Class<*>? {
        val type = clazz.genericSuperclass as? ParameterizedType ?: return null

// 强制类型转换

        val tArgs = type.actualTypeArguments

        return if (tArgs.size < 1) {
            null
        } else tArgs[index] as Class<*>

    }

    /**
     * 创建泛型类
     */
    fun <D> createClassGenricType(service: Class<D>): D {
        try {
            checkNotNull(service, "没有指定泛型类～")
            /** 加载类  */
            val c = service.getDeclaredConstructor()
            c.isAccessible = true
            /** 创建类  */
            return c.newInstance()
        } catch (e: InstantiationException) {
            throw IllegalArgumentException(service.toString() + "，实例化异常！" + e.message)
        } catch (e: IllegalAccessException) {
            throw IllegalArgumentException(service.toString() + "，访问权限异常！" + e.message)
        } catch (e: NoSuchMethodException) {
            throw IllegalArgumentException(service.toString() + "，没有找到构造方法！" + e.message)
        } catch (e: InvocationTargetException) {
            throw IllegalArgumentException(service.toString() + "，反射异常！" + e.message)
        }

    }

    /**
     * 检查是否为空
     *
     * @param <T>
     * 参数
     *
     * @param reference
     * 参数
     *
     * @param errorMessageTemplate
     * 参数
     * @return 返回值
    </T> */
    fun <T> checkNotNull(reference: T?, errorMessageTemplate: String): T {
        if (reference == null) {
            throw SKNullPointerException(errorMessageTemplate)
        }
        return reference
    }
}
