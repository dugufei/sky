package jc.sky.view.adapter.recycleview;

import android.support.v7.widget.RecyclerView;

/**
 * @author sky
 * @version 1.0 on 2017-04-07 上午10:38
 * @see SKYRecycleviewUtils
 */
public class SKYRecycleviewUtils {

	/**
	 * 底部
	 * 
	 * @param recyclerView
	 *            列表
	 * @return 结果
	 */
	public static boolean isSlideToBottom(RecyclerView recyclerView) {
		if (recyclerView == null) return false;
		if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()) return true;
		return false;
	}
}
