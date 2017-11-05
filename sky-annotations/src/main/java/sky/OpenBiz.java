package sky;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sky
 * @version 1.0 on 2017-07-02 下午1:12
 * @see OpenBiz 方法定义
 */

@Target({ ElementType.TYPE,ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface OpenBiz {

	/**
	 * biz 名称
	 */
	String name() default "undefined";

}
