package sky;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author sky
 * @version 1.0 on 2018-07-10 上午10:53
 * @see SKDILibrary
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface SKDILibrary {
}
