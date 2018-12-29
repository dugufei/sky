package sky;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author sky
 * @version 1.0 on 2018-12-29 3:07 PM
 * @see AutoExplain
 */
@Retention(SOURCE)
@Target(FIELD)
public @interface AutoExplain {

	String describe() default "";

	Class[] params() default {};
}
