package sky;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author sky
 * @version 1.0 on 2017-07-02 下午1:12
 * @see OpenDisplay 方法定义
 */

@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface OpenDisplay {

	/**
	 * biz 名称
	 */
	String name() default "undefined";

}
