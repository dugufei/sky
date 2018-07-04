package sky;

import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author sky
 * @version 1.0 on 2017-07-02 下午1:12
 * @see SKSource 来源
 */

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(CLASS)
public @interface SKSource {

	Class<?>[] value() default {};
}
