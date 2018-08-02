package sk.livedata;

/**
 * @author sky
 * @version 1.0 on 2018-08-02 下午8:57
 * @see SKActionHandler
 */
public interface SKActionHandler {

	void onAction(int state, Object... args);
}
