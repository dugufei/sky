package sky.di;

/**
 * @author sky
 * @version 1.0 on 2018-06-23 下午9:56
 * @see SKLazy 懒加载
 */
public interface SKLazy<T> {

	T get();
}
