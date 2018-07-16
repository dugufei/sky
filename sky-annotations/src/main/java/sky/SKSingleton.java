package sky;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author sky
 * @version 1.0 on 2018-06-22 下午7:16
 * @see SKSingleton
 */

@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface SKSingleton {}