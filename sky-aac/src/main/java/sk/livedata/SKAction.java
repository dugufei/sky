package sk.livedata;

import sk.livedata.list.SKNetworkState;

/**
 * @author sky
 * @version 1.0 on 2018-08-02 下午8:54
 * @see SKAction
 */
public interface SKAction {

	void action(int state, Object... args);

	void viewState(SKViewState skViewState);

	void networkState(SKNetworkState skNetworkState);

	void layoutLoading();

	void layoutContent();

	void layoutEmpty();

	void layoutError();

	void loading();

	void closeLoading();

	void netWorkRunning();

	void netWorkSuccess();

	void netWorkFailed(String message);
}
