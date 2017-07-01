package jc.sky.core;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author sky
 * @version 版本
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Impl {
	Class value();
}
