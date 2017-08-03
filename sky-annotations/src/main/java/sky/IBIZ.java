package sky;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author sky
 * @version 1.0 on 2017-07-02 下午1:12
 * @see IBIZ 方法定义
 */

@Target(METHOD)
@Retention(CLASS)
public @interface IBIZ {

	IType value() default IType.HTTP;

}
