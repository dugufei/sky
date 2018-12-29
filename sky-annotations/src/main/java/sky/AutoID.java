package sky;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author sky
 * @version 1.0 on 2018-06-14 下午10:16
 * @see AutoID
 */
@Retention(SOURCE)
@Target(TYPE)
public @interface AutoID {
    int value() default -1;
}