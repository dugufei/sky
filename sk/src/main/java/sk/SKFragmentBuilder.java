package sk;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.SKViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import sk.builder.SKLayoutBuilder;
import sk.builder.SKRecyclerViewBuilder;
import sk.builder.SKViewStub;
import sk.view.sticky.stickyheader.StickyRecyclerHeadersTouchListener;

/**
 * @author sky
 * @version 1.0 on 2018-06-13 下午9:03
 * @see SKFragmentBuilder
 */
public final class SKFragmentBuilder implements LifecycleObserver, View.OnTouchListener {

	private final SKFragment		skFragment;

	private final LayoutInflater	mInflater;

	private final Context			context;

	private final Bundle			savedInstanceState;

	private Unbinder				unbinder;

	SKFragmentBuilder(@NonNull SKFragment skFragment, @NonNull Lifecycle lifecycle, Bundle savedInstanceState) {
		this.skFragment = skFragment;
		this.context = skFragment.getContext();
		this.mInflater = LayoutInflater.from(context);
		this.savedInstanceState = savedInstanceState;
		lifecycle.addObserver(this);
	}

	// 布局编辑器
	final SKLayoutBuilder	skLayoutBuilder	= new SKLayoutBuilder();

	// recycler编辑器
	SKRecyclerViewBuilder	skRecyclerViewBuilder;

	/**
	 * 初始化布局
	 */
	private void init() {
		skLayoutBuilder.createRoot(context, mInflater);
		/** recyclerview **/
		if (this.skRecyclerViewBuilder != null) {
			this.skRecyclerViewBuilder.createRecyclerView(skLayoutBuilder.contentRoot);
		}
	}

	/**
	 * 初始化ViewModel
	 */

	SKViewModel skViewModel;

	private void initViewModel() {
		Class clazz = SKCoreUtils.getClassGenricType(skFragment.getClass(), 0);
		Bundle bundle = skFragment.getArguments();
		skViewModel = SKViewModelProviders.of(skFragment, skFragment.skViewModelFactory).get(SKViewModel.class, clazz, bundle);
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
		/** 打开开关触发菜单项 **/
		skFragment.setHasOptionsMenu(true);
		/** 注入 **/
		SKInputs.input(skFragment);
	}

	View viewCreate() {
		/** layout **/
		skFragment.build(this).init();
		/** 初始化所有组建 **/
		unbinder = ButterKnife.bind(skFragment, skLayoutBuilder.contentRoot);
		/** 初始化点击事件 **/
		skLayoutBuilder.contentRoot.setOnTouchListener(this);// 设置点击事件
		/** 初始化ViewModel **/
		initViewModel();
		return skLayoutBuilder.contentRoot;
	}

	void activityCreate() {
		SKHelper.interceptor().fragmentInterceptor().onFragmentCreated(skFragment,skFragment.getArguments(),savedInstanceState);
		/** 初始化数据 **/
		skFragment.initData(skFragment.getArguments());
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_START) void start() {
		SKHelper.interceptor().fragmentInterceptor().onFragmentStart(skFragment);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_RESUME) void resume() {
		SKHelper.interceptor().fragmentInterceptor().onFragmentResume(skFragment);
	}

	public void requestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		SKHelper.interceptor().activityInterceptor().onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE) void pause() {
		SKHelper.interceptor().fragmentInterceptor().onFragmentPause(skFragment);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_STOP) void stop() {
		SKHelper.interceptor().fragmentInterceptor().onFragmentStop(skFragment);
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) void destroy() {
		SKHelper.interceptor().fragmentInterceptor().onFragmentDestroy(skFragment);
		/** 清空注解view **/
		unbinder.unbind();
	}

	@Override public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
