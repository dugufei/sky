package sky.example.common;

import android.os.Bundle;

import sk.SKHelper;
import sk.SKPublicBiz;

/**
 * @author sky
 * @version 1.0 on 2019-02-26 9:57 AM
 * @see UserPublicBiz
 */
public class UserPublicBiz extends SKPublicBiz {

	@Override public void initBiz(Bundle bundle) {

	}

	public void showTip(){
        SKHelper.toast().show("我执行了");
    }
}
