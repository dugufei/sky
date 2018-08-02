package sk;

/**
 * @author sky
 * @version 1.0 on 2018-07-31 下午5:53
 * @see SKRepository
 */
public class SKRepository<R extends SKRepository> {

	public R repository;

	public static final <D> D http(Class<D> httpClazz) {
		return SKHelper.http(httpClazz);
	}

}
