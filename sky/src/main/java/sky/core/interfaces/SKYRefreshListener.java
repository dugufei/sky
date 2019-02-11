package sky.core.interfaces;


import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @author sky
 * @version 版本
 */
public interface SKYRefreshListener extends SwipeRefreshLayout.OnRefreshListener {

	boolean onScrolledToBottom();// 到底部
}