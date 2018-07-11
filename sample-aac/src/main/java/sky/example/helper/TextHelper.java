package sky.example.helper;

import sk.SKHelper;
import sky.example.helper.modules.ABC;

/**
 * @author sky
 * @version 1.0 on 2018-07-11 下午5:36
 * @see TextHelper
 */
public class TextHelper extends SKHelper {

	public static ABC abc() {
		TextManager textManager = getManage();
		return textManager.abc.get();
	}
}
