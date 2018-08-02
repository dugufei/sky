package sky.example;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;

import sk.SKBiz;
import sk.livedata.SKData;
import sk.livedata.SKTransformations;
import sky.SKInput;
import sky.example.bean.User;
import sky.example.repository.HomeRepository;
import sky.example.repository.UserRepository;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午3:36
 * @see MainBiz
 */
public class MainBiz extends SKBiz {

	@SKInput UserRepository	userProvider;

	@SKInput HomeRepository	homeRepository;

	private SKData<User>	userSKData;

	private SKData<User>	userHomeSKData;

	private SKData<String>	stringSKData;

	@Override public void initBiz(Bundle bundle) {
		userSKData = userProvider.load();
		userHomeSKData = homeRepository.getMM("哈哈哈");
		stringSKData = SKTransformations.map(userSKData, user -> user.name + "" + user.age);
	}

	public SKData<User> getUserSKData() {
		return userSKData;
	}

	public SKData<User> getUserHomeSKData() {
		return userHomeSKData;
	}

	public LiveData<String> getStringSKData() {
		return stringSKData;
	}

	public void change(String one) {
		// userProvider.refreshUser(userSKData);
		userProvider.changeUser(userSKData, one);
	}
}
