package sk.di;

import android.arch.lifecycle.ViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;
import sk.SKViewModel;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午9:46
 * @see SKViewModelKey
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@MapKey
public @interface SKViewModelKey {

	Class<? extends ViewModel> value();
}
