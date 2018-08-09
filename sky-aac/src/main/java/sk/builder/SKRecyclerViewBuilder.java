package sk.builder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import sk.SKAdapter;
import sk.utils.SKPreconditions;
import sk.view.sticky.stickyheader.SKYStickyHeaders;
import sk.view.sticky.stickyheader.StickyRecyclerHeadersDecoration;
import sk.view.sticky.stickyheader.StickyRecyclerHeadersTouchListener;

/**
 * @author sky
 * @version 1.0 on 2018-07-12 下午11:30
 * @see SKRecyclerViewBuilder
 */
public class SKRecyclerViewBuilder {

	private final int												recyclerviewId;

	public RecyclerView												recyclerView;

	public SKAdapter												skAdapter;

	public RecyclerView.LayoutManager								layoutManager;			// 布局管理器

	public RecyclerView.ItemAnimator								itemAnimator;			// 动画

	public RecyclerView.ItemDecoration								itemDecoration;			// 分割线

	public StickyRecyclerHeadersTouchListener.OnHeaderClickListener	onHeaderClickListener;

	public boolean													isHeaderFooter;

	public SKRecyclerViewBuilder(int recyclerviewId) {
		this.recyclerviewId = recyclerviewId;
	}

	public void createRecyclerView(View view) {
		if (recyclerviewId > 0) {
			recyclerView = view.findViewById(recyclerviewId);
			SKPreconditions.checkNotNull(recyclerView, "无法根据布局文件ID,获取recyclerView");
			SKPreconditions.checkNotNull(skAdapter, "adapter不能为空");

			if(layoutManager == null){
				layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
			}

			recyclerView.setLayoutManager(layoutManager);
			// 扩展适配器
			if (skAdapter instanceof SKYStickyHeaders) {
				SKYStickyHeaders SKYStickyHeaders = (SKYStickyHeaders) skAdapter;
				final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(SKYStickyHeaders);
				recyclerView.addItemDecoration(headersDecor);

				if (onHeaderClickListener != null) {
					StickyRecyclerHeadersTouchListener touchListener = new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
					touchListener.setOnHeaderClickListener(onHeaderClickListener);
					recyclerView.addOnItemTouchListener(touchListener);

				}
				skAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

					@Override public void onChanged() {
						headersDecor.invalidateHeaders();
					}
				});
			}

			if (isHeaderFooter) {
				final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
				SKPreconditions.checkNotNull(gridLayoutManager, "LayoutManger，不是GridLayoutManager");
				gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

					@Override public int getSpanSize(int position) {
						return skAdapter.isHeaderAndFooter(position) ? gridLayoutManager.getSpanCount() : 1;
					}
				});
			}

			recyclerView.setAdapter(skAdapter);

			// 设置Item增加、移除动画
			if(itemAnimator != null){
				recyclerView.setItemAnimator(itemAnimator);
			}
			// 添加分割线
			if(itemDecoration != null){
				recyclerView.addItemDecoration(itemDecoration);
			}
			// 优化
			recyclerView.setHasFixedSize(true);
		}
	}

	public RecyclerView.LayoutManager getLayoutManager() {
		return layoutManager;
	}
}
