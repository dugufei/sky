package sk.plugins;

import java.lang.reflect.Method;

import android.os.Bundle;

/**
 * @author sky
 * @version 版本
 */
public interface SKDisplayEndInterceptor {

    void interceptEnd(String clazzName, Bundle bundle, Object backgroundResult);
}
