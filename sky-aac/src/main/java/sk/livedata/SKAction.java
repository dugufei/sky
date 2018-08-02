package sk.livedata;

/**
 * @author sky
 * @version 1.0 on 2018-08-02 下午8:54
 * @see SKAction
 */
public interface SKAction {

	void action(int state, Object... args);

	void showLoading(Object... args);

	void showContent(Object... args);

	void showEmpty(Object... args);

	void showError(Object... args);

	void loading();

	void closeloading();
}
