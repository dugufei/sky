package sky;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * @author sky
 * @version 1.0 on 2017-07-02 下午1:12
 * @see IUI 方法定义
 */

@Target(METHOD)
@Retention(CLASS)
public @interface IUI {

	IType value() default IType.UI;

}
