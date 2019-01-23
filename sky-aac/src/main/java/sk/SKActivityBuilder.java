package sk;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.SKViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;
import sk.builder.SKLayoutBuilder;
import sk.builder.SKRecyclerViewBuilder;
import sk.builder.SKTintBuilder;
import sk.builder.SKViewStub;
import sk.view.sticky.stickyheader.StickyRecyclerHeadersTouchListener;

/**
 * @author sky
 * @version 1.0 on 2018-06-13 下午9:03
 * @see SKActivityBuilder
 */
public final class SKActivityBuilder implements LifecycleObserver {

	private final SKActivity		skActivity;

	private final LayoutInflater	mInflater;

	private final Context			context;

	private final Bundle			savedInstanceState;

	private boolean					isFinish;

	SKActivityBuilder(@NonNull SKActivity skActivity, @NonNull Lifecycle lifecycle, Bundle savedInstanceState) {
		this.skActivity = skActivity;
		this.mInflater = LayoutInflater.from(skActivity);
		this.context = skActivity;
		this.savedInstanceState = savedInstanceState;
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
		skLayoutBuilder.createLayout(skActivity, context, mInflater);
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
	 * 初始化ViewModel
	 */

	SKViewModel skViewModel;

	private void initViewModel() {
		Class clazz = SKCoreUtils.getClassGenricType(skActivity.getClass(), 0);
		Bundle bundle = skActivity.getIntent() == null ? null : skActivity.getIntent().getExtras();
		skViewModel = SKViewModelProviders.of(skActivity, skActivity.skViewModelFactory).get(SKViewModel.class, clazz, bundle);
//		skActivity.biz = (SKBiz) skViewModel.skProxy.impl;
	}

	@NonNull <B extends SKBiz> B bizProxy() {
		return (B) skViewModel.skProxy.proxy;
	}

	/**
	 * 布局
	 */

	public void layoutId(@LayoutRes int layoutId) {
		this.skLayoutBuilder.layoutId = layoutId;
	}

	public void layoutLoadingId(@LayoutRes int layoutId) {
		this.skLayoutBuilder.layoutLoadingId = layoutId;
	}

	public <V extends SKViewStub> void layoutLoadingViewSub(V v) {
		this.skLayoutBuilder.vsViewLoading = v;
	}

	public void layoutEmptyId(@LayoutRes int layoutId) {
		this.skLayoutBuilder.layoutEmptyId = layoutId;
	}

	public <V extends SKViewStub> void layoutEmptyViewSub(V v) {
		this.skLayoutBuilder.vsViewEmpty = v;
	}

	public void layoutlayoutErrorId(@LayoutRes int layoutId) {
		this.skLayoutBuilder.layoutErrorId = layoutId;
	}

	public <V extends SKViewStub> void layoutErrorViewSub(V v) {
		this.skLayoutBuilder.vsViewError = v;
	}

	public void layoutBackground(@IdRes int colorId) {
		this.skLayoutBuilder.layoutBackground = colorId;
	}

	public void layoutBackground(@NonNull String colorString) {
		this.skLayoutBuilder.layoutBackground = Color.parseColor(colorString);
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
	public void recyclerviewId(@IdRes int recyclerviewId, @NonNull SKAdapter skAdapter) {
		skRecyclerViewBuilder = new SKRecyclerViewBuilder(recyclerviewId);
		skRecyclerViewBuilder.skAdapter = skAdapter;
	}

	public void recyclerviewLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager) {
		skRecyclerViewBuilder.layoutManager = layoutManager;
	}

	public void recyclerviewAnimator(@NonNull RecyclerView.ItemAnimator itemAnimator) {
		skRecyclerViewBuilder.itemAnimator = itemAnimator;
	}

	public void recyclerviewItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
		skRecyclerViewBuilder.itemDecoration = itemDecoration;

	}

	public void recyclerviewStickyHeaderClick(@NonNull StickyRecyclerHeadersTouchListener.OnHeaderClickListener onHeaderClickListener) {
		skRecyclerViewBuilder.onHeaderClickListener = onHeaderClickListener;
	}

	public void recyclerviewGridOpenHeaderFooter(boolean bool) {
		skRecyclerViewBuilder.isHeaderFooter = bool;
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_CREATE) void create() {
		SKInputs.input(skActivity);
		/** 初始化堆栈 **/
		SKHelper.screen().onCreate(skActivity);
		/** 活动拦截器 **/
		SKHelper.interceptor().activityInterceptor().onCreate(skActivity, skActivity.getIntent().getExtras(), savedInstanceState);
		/** layout **/
		skActivity.build(this).init();
		/** 初始化所有组建 **/
		ButterKnife.bind(skActivity);
		/** 初始化ViewModel **/
		initViewModel();
		/** 初始化数据 **/
		skActivity.initData(skActivity.getIntent().getExtras());
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_START) void start() {
		SKHelper.interceptor().activityInterceptor().onStart(skActivity);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_RESUME) void resume() {
		SKHelper.screen().onResume(skActivity);
		SKHelper.interceptor().activityInterceptor().onResume(skActivity);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE) void pause() {
		SKHelper.screen().onPause(skActivity);
		SKHelper.interceptor().activityInterceptor().onPause(skActivity);

		if (skActivity.isFinishing()) {
			isFinish = true;
			SKHelper.screen().onDestroy(skActivity);
			SKHelper.interceptor().activityInterceptor().onDestroy(skActivity);
			/** 关闭键盘 **/
			hideSoftInput();
		}

	}

	@OnLifecycleEvent(Lifecycle.Event.ON_STOP) void stop() {
		SKHelper.interceptor().activityInterceptor().onStop(skActivity);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) void destroy() {
		if (!isFinish) {
			SKHelper.screen().onDestroy(skActivity);
			SKHelper.interceptor().activityInterceptor().onDestroy(skActivity);
			/** 关闭键盘 **/
			hideSoftInput();
		}
	}

	void onPostCreate(Bundle savedInstanceState) {
		SKHelper.interceptor().activityInterceptor().onPostCreate(skActivity, savedInstanceState);
	}

	void onPostResume() {
		SKHelper.interceptor().activityInterceptor().onPostResume(skActivity);
	}

	void onActivityResult(int requestCode, int resultCode, Intent data) {
		SKHelper.screen().onActivityResult(skActivity);
		SKHelper.interceptor().activityInterceptor().onActivityResult(requestCode, resultCode, data);
	}

	void onRestart() {
		SKHelper.interceptor().activityInterceptor().onRestart(skActivity);
	}

	void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		SKHelper.interceptor().activityInterceptor().onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	/**
	 * 隐藏键盘
	 */

	void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) skActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(skActivity.getWindow().getDecorView().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
