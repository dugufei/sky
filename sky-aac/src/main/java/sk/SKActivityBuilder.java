package sk;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.SKViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;
import sk.builder.SKLayoutBuilder;
import sk.builder.SKRecyclerViewBuilder;
import sk.builder.SKTintBuilder;

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
	private void initViewModel() {
		Class clazz = SKCoreUtils.getClassGenricType(skActivity.getClass(), 0);
		skActivity.model = (SKViewModel) SKViewModelProviders.of(skActivity, skActivity.skViewModelFactory).get(clazz);
		skActivity.model.init();
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

	public void onPostCreate(Bundle savedInstanceState) {
		SKHelper.interceptor().activityInterceptor().onPostCreate(skActivity, savedInstanceState);
	}

	public void onPostResume() {
		SKHelper.interceptor().activityInterceptor().onPostResume(skActivity);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		SKHelper.screen().onActivityResult(skActivity);
		SKHelper.interceptor().activityInterceptor().onActivityResult(requestCode, resultCode, data);
	}

	public void onRestart() {
		SKHelper.interceptor().activityInterceptor().onRestart(skActivity);
	}

	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		SKHelper.interceptor().activityInterceptor().onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	/**
	 * 隐藏键盘
	 */

	public void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) skActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(skActivity.getWindow().getDecorView().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
