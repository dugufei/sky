package sky;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author sky
 * @version 1.0 on 2017-07-02 下午1:12
 * @see SKDIApp 来源
 */
@Documented
@Target(TYPE)
@Retention(CLASS)
public @interface SKDIApp {
    Class[] value();
}
