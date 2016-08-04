package jc.sky.view.common;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * @创建人 sky
 * @创建时间 15/7/14 下午4:28
 * @类描述 ListView 下拉和加载更多接口
 */
public interface SKYRefreshListener extends SwipeRefreshLayout.OnRefreshListener {

	boolean onScrolledToBottom();// 到底部
}