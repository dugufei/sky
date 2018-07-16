package sk;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import sk.builder.SKLayoutBuilder;
import sk.builder.SKRecyclerViewBuilder;
import sk.builder.SKTintBuilder;

/**
 * @author sky
 * @version 1.0 on 2018-06-13 下午9:03
 * @see SKBuilder
 */
public final class SKBuilder implements LifecycleObserver {

	private final SKActivity		skActivity;

	private final LayoutInflater	mInflater;

	private final SKFragment		skFragment;

	private final Context			context;

	SKBuilder(@NonNull SKActivity skActivity, @NonNull Lifecycle lifecycle) {
		this.skActivity = skActivity;
		this.mInflater = LayoutInflater.from(skActivity);
		this.skFragment = null;
		this.context = skActivity;
		lifecycle.addObserver(this);
	}

	SKBuilder(@NonNull SKFragment skFragment, @NonNull Lifecycle lifecycle) {
		this.skActivity = (SKActivity) skFragment.getActivity();
		this.mInflater = LayoutInflater.from(skActivity);
		this.skFragment = skFragment;
		this.context = skFragment.getContext();
		lifecycle.addObserver(this);
	}

	// 布局编辑器
	final SKLayoutBuilder	skLayoutBuilder	= new SKLayoutBuilder();

	// recycler编辑器
	SKRecyclerViewBuilder	skRecyclerViewBuilder;

	// 状态栏编辑器
	SKTintBuilder			skTintBuilder;

	/**
	 * 初始化布局
	 */
	private void init() {
		skLayoutBuilder.createLayout(skActivity,context, mInflater);
		/** recyclerview **/
		if (this.skRecyclerViewBuilder != null) {
			this.skRecyclerViewBuilder.createRecyclerView(skLayoutBuilder.contentRoot);
		}
		/** tint */
		if (this.skTintBuilder != null) {
			this.skTintBuilder.createTint(skActivity);
		}

	}

	/**
	 * 布局
	 */

	public void layoutId(@LayoutRes int layoutId) {
		this.skLayoutBuilder.layoutId = layoutId;
	}

	public void layoutStateId(@IdRes int layoutId) {
		this.skLayoutBuilder.layoutStateId = layoutId;
	}

	public void tintFitsSystem(boolean isFitsSystem) {
		this.skLayoutBuilder.fitsSystem = isFitsSystem;
	}

	/**
	 * 状态栏
	 */

	public void tintOpen(boolean isOpen) {
		this.skTintBuilder = isOpen ? new SKTintBuilder() : null;
	}

	public void tintColor(@ColorRes int tintColor) {
		this.skTintBuilder.tintColor = tintColor;
	}

	public void tintStatusBarEnabled(boolean isStatusBar) {
		this.skTintBuilder.statusBarEnabled = isStatusBar;
	}

	public void tintNavigationBarEnabled(boolean isNavigationBar) {
		this.skTintBuilder.navigationBarTintEnabled = isNavigationBar;
	}

	// 设置
	public void recyclerviewId(@IdRes int recyclerviewId) {
		skRecyclerViewBuilder = new SKRecyclerViewBuilder(recyclerviewId);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_CREATE) void create() {
		/** layout **/
		skActivity.build(this).init();
		/** 初始化所有组建 **/
		ButterKnife.bind(skActivity);

		/** 初始化数据 **/
		skActivity.initData(skActivity.getIntent().getExtras());
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_START) void start() {
		SKHelper.interceptor().activityInterceptor().onStart(skActivity);

	}

	@OnLifecycleEvent(Lifecycle.Event.ON_RESUME) void resume() {
		SKHelper.interceptor().activityInterceptor().onResume(skActivity);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE) void pause() {
		SKHelper.interceptor().activityInterceptor().onPause(skActivity);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_STOP) void stop() {
		SKHelper.interceptor().activityInterceptor().onStop(skActivity);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) void destroy() {
		SKHelper.interceptor().activityInterceptor().onDestroy(skActivity);
	}

}
