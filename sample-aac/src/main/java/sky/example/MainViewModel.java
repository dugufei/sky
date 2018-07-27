package sky.example;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import sk.L;
import sk.SKData;
import sk.SKViewModel;
import sky.SKInput;
import sky.example.bean.User;
import sky.example.repository.HomeRepository;
import sky.example.repository.UserRepository;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午3:36
 * @see MainViewModel
 */
public class MainViewModel extends SKViewModel {

	@SKInput UserRepository	userProvider;

	@SKInput HomeRepository	homeRepository;

	SKData<User>			userSKData;

	SKData<User>			userHomeSKData;

	LiveData<String>		stringSKData;

	@Override public void init() {
		if(userSKData == null){
			userSKData = userProvider.getUser();
		}
		if(userHomeSKData == null){
			userHomeSKData = homeRepository.getMM("哈哈哈");
		}
		if(stringSKData == null){
			stringSKData = Transformations.map(userSKData, user -> user.name + "" + user.age);
		}
	}

	public void change(String one) {
		try {

			userProvider.changeUser(userSKData, one);
		}catch (Exception e){
			L.i(e+"");
		}
	}
}
