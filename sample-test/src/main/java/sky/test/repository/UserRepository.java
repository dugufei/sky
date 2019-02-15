package sky.test.repository;

import sk.SKRepository;
import sk.livedata.SKPaged;
import sky.SKInput;
import sky.SKProvider;
import sky.SKSingleton;
import sky.test.model.Model;

/**
 * @author sky
 * @version 1.0 on 2019-02-12 4:10 PM
 * @see UserRepository
 */
@SKProvider
@SKSingleton
public class UserRepository extends SKRepository<UserRepository> {

	@SKInput SKPaged	skPaged;

	@SKInput Model		model;

	public void inta() {
		skPaged.pagedBuilder();
	}

}
