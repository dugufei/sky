package sk;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

/**
 * @author sky
 * @version 1.0 on 2018-06-13 下午9:03
 * @see SKBuilder
 */
final class SKBuilder implements LifecycleObserver {

	private final SKActivity		skActivity;

	private final LayoutInflater	mInflater;

	public SKBuilder(SKActivity skActivity, Lifecycle lifecycle) {
		this.skActivity = skActivity;
		this.mInflater = LayoutInflater.from(skActivity);
		lifecycle.addObserver(this);
	}

	/**
	 * 布局
	 */
	private ViewGroup	contentRoot;

	private int			layoutId;

	private int			layoutLoadingId;

	private int			layoutEmptyId;

	private int			layoutBizErrorId;

	private int			layoutHttpErrorId;

	private int			layoutStateId;

	private View		contentRootView;

	private View		layoutContent;

	private View		layoutStateContent;

	private ViewStub	vsLoading;

	private View		layoutLoading;

	private View		layoutEmpty;

	private View		layoutBizError;

	private View		layoutHttpError;

	public void layoutId(@LayoutRes int layoutId) {
		this.layoutId = layoutId;
	}

	public void layoutStateId(@IdRes int layoutId) {
		this.layoutStateId = layoutId;
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_CREATE) void create() {
		/** layout **/
		createLayout();
		/** recyclerview **/
		createRecyclerView();
	}

	private void createLayout() {
		contentRoot = skActivity.findViewById(android.R.id.content);

		// 内容
		if (layoutId != 0) {
			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
			layoutContent = mInflater.inflate(layoutId, null, false);
			if (layoutStateId != 0) {
				layoutStateContent = layoutContent.findViewById(layoutStateId);
			}
			contentRoot.addView(layoutContent, layoutParams);
		}

//		layoutLoadingId = layoutLoadingId > 0 ? layoutLoadingId : SKYHelper.getComnonView() == null ? 0 : SKYHelper.getComnonView().layoutLoading();
	}

	private void createRecyclerView() {}

	@OnLifecycleEvent(Lifecycle.Event.ON_START) void start() {}

	@OnLifecycleEvent(Lifecycle.Event.ON_RESUME) void resume() {}

	@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE) void pause() {}

	@OnLifecycleEvent(Lifecycle.Event.ON_STOP) void stop() {}

	@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) void destroy() {}
}
