package sky;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author sky
 * @version 1.0 on 2018-06-14 下午10:16
 * @see SKProvider
 */
@Retention(SOURCE)
@Target(ElementType.METHOD)
public @interface SKProvider {}