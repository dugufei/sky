package sky.example.helper;

import sk.SKDefaultManager;
import sky.SKInput;
import sky.di.SKLazy;
import sky.example.helper.modules.ABC;

/**
 * @author sky
 * @version 1.0 on 2018-07-11 下午5:36
 * @see TextManager
 */
public class TextManager extends SKDefaultManager {

	@SKInput SKLazy<ABC> abc;
}
