package sky.example.helper;

import sk.SKHelper;
import sky.example.helper.modules.BoutModel;

/**
 * @author sky
 * @version 1.0 on 2018-07-11 下午5:36
 * @see TextHelper
 */
public class TextHelper extends SKHelper {

	public static BoutModel abc() {
		TextManager textManager = getManage();
		return textManager.abc.get();
	}
}
