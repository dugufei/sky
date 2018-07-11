package sky;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author sky
 * @version 1.0 on 2018-06-14 下午10:22
 * @see SKInput
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface SKInput {}
