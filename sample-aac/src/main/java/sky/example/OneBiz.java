package sky.example;

import android.os.Bundle;

import sk.SKBiz;
import sk.SKData;
import sk.SKViewModel;
import sky.SKInput;
import sky.example.bean.User;
import sky.example.repository.UserRepository;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午3:36
 * @see OneBiz
 */
public class OneBiz extends SKBiz {

	@SKInput UserRepository	userProvider;

	public SKData<User> getSkData() {
		return skData;
	}

	private SKData<User>			skData;

	public void change(String one) {
		userProvider.changeUser(skData, one);
	}

	public void update() {
		userProvider.changeUser(skData, "改改");
	}

	@Override public void initBiz(Bundle bundle) {
		skData = userProvider.getUser();
	}
}
