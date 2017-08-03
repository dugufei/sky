package sky;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author sky
 * @version 1.0 on 2017-07-28 下午7:15
 * @see IParent 指定接口夫类
 */
@Retention(RetentionPolicy.CLASS)
public @interface IParent {

	String value();
}
