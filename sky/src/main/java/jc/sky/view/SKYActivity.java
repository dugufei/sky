package jc.sky.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYAppUtil;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.common.utils.SKYKeyboardUtils;
import jc.sky.common.utils.SKYSwipeWindowHelper;
import jc.sky.core.SKYIBiz;
import jc.sky.core.SKYIView;
import jc.sky.display.SKYIDisplay;
import jc.sky.modules.structure.SKYStructureModel;
import jc.sky.view.adapter.recycleview.SKYRVAdapter;
/**
 * @author sky
 * @version 版本
 */
public abstract class SKYActivity<B extends SKYIBiz> extends AppCompatActivity implements SKYIView {

	/**
	 * 定制
	 *
	 * @param initialSKYBuilder
	 *            参数
	 * @return 返回值
	 **/
	protected abstract SKYBuilder build(SKYBuilder initialSKYBuilder);

	/**
	 * 编译
	 *
	 * @param bundle
	 *            参数
	 */
	protected void buildBefore(Bundle bundle) {}

	/**
	 * 初始化dagger
	 */
	protected void initDagger() {

	}

	/**
	 * 数据
	 *
	 * @param savedInstanceState
	 *            参数
	 */
	protected void createData(Bundle savedInstanceState) {

	}

	/**
	 * 初始化数据
	 *
	 * @param savedInstanceState
	 *            数据
	 */
	protected abstract void initData(Bundle savedInstanceState);

	/**
	 * View层编辑器
	 **/
	public SKYBuilder				SKYBuilder;

	SKYStructureModel				SKYStructureModel;

	private SystemBarTintManager	tintManager;

	private SKYSwipeWindowHelper	mSwipeWindowHelper;

	private boolean					isFinish;

	/**
	 * 初始化
	 *
	 * @param savedInstanceState
	 *            参数
	 */
	@Override protected void onCreate(Bundle savedInstanceState) {
		buildBefore(savedInstanceState);
		super.onCreate(savedInstanceState);
		/** 初始化核心 **/
		initCore();
		/** 初始化堆栈 **/
		SKYHelper.screenHelper().onCreate(this);
		/** 活动拦截器 **/
		SKYHelper.methodsProxy().activityInterceptor().onCreate(this, getIntent().getExtras(), savedInstanceState);
		/** 初始化视图 **/
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		SKYBuilder = new SKYBuilder(this, inflater);
		/** 拦截Builder **/
		SKYHelper.methodsProxy().activityInterceptor().build(this, SKYBuilder);
		setContentView(build(SKYBuilder).create());
		/** 状态栏高度 **/
		if (SKYBuilder.isFitsSystem()) {
//			ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
//			View parentView = contentFrameLayout.getChildAt(0);
//			if (parentView != null && Build.VERSION.SDK_INT >= 14) {
//				parentView.setFitsSystemWindows(true);
//			}
		}
		/** 状态栏颜色 **/
		if (SKYBuilder.isTint()) {
			tintManager = new SystemBarTintManager(this);
			// enable status bar tint
			tintManager.setStatusBarTintEnabled(SKYBuilder.getStatusBarTintEnabled());
			// enable navigation bar tint
			tintManager.setNavigationBarTintEnabled(SKYBuilder.getNavigationBarTintEnabled());
			tintManager.setStatusBarTintResource(SKYBuilder.getTintColor());
		}

		/** 初始化所有组建 **/
		ButterKnife.bind(this);
		/** 初始化业务数据 **/
		if (SKYStructureModel != null) {
			SKYStructureModel.initBizBundle();
		}
		/** 初始化dagger **/
		initDagger();
		/** 初始化数据 **/
		createData(savedInstanceState);
		/** 初始化数据 **/
		initData(getIntent().getExtras());

	}

	/**
	 * 核心
	 */
	protected void initCore() {
		SKYStructureModel = new SKYStructureModel(this, getIntent() == null ? null : getIntent().getExtras());
		SKYHelper.structureHelper().attach(SKYStructureModel);
	}

	public Object model() {
		return SKYStructureModel.getSKYProxy().impl;
	}

	public B biz() {
		if (SKYStructureModel == null || SKYStructureModel.getSKYProxy() == null || SKYStructureModel.getSKYProxy().proxy == null) {
			Class service = SKYAppUtil.getSuperClassGenricType(getClass(), 0);
			return (B) SKYHelper.structureHelper().createNullService(service);
		}
		return (B) SKYStructureModel.getSKYProxy().proxy;
	}

	public <C extends SKYIBiz> C biz(Class<C> service) {
		if (SKYStructureModel != null && service.equals(SKYStructureModel.getService())) {
			if (SKYStructureModel == null || SKYStructureModel.getSKYProxy() == null || SKYStructureModel.getSKYProxy().proxy == null) {
				return SKYHelper.structureHelper().createNullService(service);
			}
			return (C) SKYStructureModel.getSKYProxy().proxy;
		}
		return SKYHelper.biz(service);
	}

	public <D extends SKYIDisplay> D display(Class<D> eClass) {
		return SKYHelper.display(eClass);
	}

	/**
	 * 设置输入法
	 *
	 * @param mode
	 *            参数
	 */
	public void setSoftInputMode(int mode) {
		getWindow().setSoftInputMode(mode);
	}

	@Override protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		SKYHelper.methodsProxy().activityInterceptor().onPostCreate(this, savedInstanceState);
	}

	@Override protected void onPostResume() {
		super.onPostResume();
		SKYHelper.methodsProxy().activityInterceptor().onPostResume(this);
	}

	@Override protected void onStart() {
		super.onStart();
		SKYHelper.methodsProxy().activityInterceptor().onStart(this);
	}

	@Override protected void onResume() {
		super.onResume();
		SKYHelper.screenHelper().onResume(this);
		SKYHelper.methodsProxy().activityInterceptor().onResume(this);
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SKYHelper.screenHelper().onActivityResult(this);
		SKYHelper.methodsProxy().activityInterceptor().onActivityResult(requestCode, resultCode, data);
	}

	@Override protected void onPause() {
		super.onPause();
		SKYHelper.screenHelper().onPause(this);
		SKYHelper.methodsProxy().activityInterceptor().onPause(this);
		// 恢复初始化
		recyclerRefreshing(false);

		if (isFinishing()) {

			isFinish = true;

			detach();
			/** 移除builder **/
			if (SKYBuilder != null) {
				SKYBuilder.detach();
				SKYBuilder = null;
			}
			if (SKYStructureModel != null) {
				SKYHelper.structureHelper().detach(SKYStructureModel);
			}
			SKYHelper.screenHelper().onDestroy(this);
			SKYHelper.methodsProxy().activityInterceptor().onDestroy(this);
			/** 关闭键盘 **/
			SKYKeyboardUtils.hideSoftInput(this);
		}
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		if (!isFinish) {
			detach();
			/** 移除builder **/
			if (SKYBuilder != null) {
				SKYBuilder.detach();
				SKYBuilder = null;
			}
			if (SKYStructureModel != null) {
				SKYHelper.structureHelper().detach(SKYStructureModel);
			}
			SKYHelper.screenHelper().onDestroy(this);
			SKYHelper.methodsProxy().activityInterceptor().onDestroy(this);
			/** 关闭键盘 **/
			SKYKeyboardUtils.hideSoftInput(this);
		}
	}

	@Override protected void onRestart() {
		super.onRestart();
		SKYHelper.methodsProxy().activityInterceptor().onRestart(this);
	}

	@Override protected void onStop() {
		super.onStop();
		SKYHelper.methodsProxy().activityInterceptor().onStop(this);
	}

	/**
	 * 清空
	 */
	protected void detach() {}

	public void setLanding() {
		SKYHelper.screenHelper().setAsLanding(this);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 创建menu
	 *
	 * @param menu
	 *            参数
	 * @return 返回值
	 */
	@Override public boolean onCreateOptionsMenu(Menu menu) {
		if (SKYBuilder != null && SKYBuilder.getToolbarMenuId() > 0) {
			getMenuInflater().inflate(SKYBuilder.getToolbarMenuId(), menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (SKYHelper.structureHelper().onKeyBack(keyCode, getSupportFragmentManager(), this)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**********************
	 * View业务代码
	 *******************
	 */

	/**
	 * @param <T>
	 *            参数
	 * @param clazz
	 *            参数
	 * @return 返回值
	 */
	public <T> T findFragment(Class<T> clazz) {
		SKYCheckUtils.checkNotNull(clazz, "class不能为空");
		return (T) getSupportFragmentManager().findFragmentByTag(clazz.getName());
	}

	public SKYView SKYView() {
		return SKYBuilder == null ? null : SKYBuilder.getSKYView();
	}

	/**********************
	 * Actionbar业务代码
	 *********************/

	@Override public void showContent() {
		if (SKYHelper.methodsProxy().layoutInterceptor() != null) {
			SKYHelper.methodsProxy().layoutInterceptor().showContent(this);
		}
		if (SKYBuilder != null) {
			SKYBuilder.layoutContent();
		}
	}

	@Override public void showLoading() {
		if (SKYHelper.methodsProxy().layoutInterceptor() != null) {
			SKYHelper.methodsProxy().layoutInterceptor().showLoading(this);
		}
		if (SKYBuilder != null) {
			SKYBuilder.layoutLoading();
		}
	}

	@Override public void showBizError() {
		if (SKYHelper.methodsProxy().layoutInterceptor() != null) {
			SKYHelper.methodsProxy().layoutInterceptor().showBizError(this);
		}
		if (SKYBuilder != null) {
			SKYBuilder.layoutBizError();
		}
	}

	@Override public void showEmpty() {
		if (SKYHelper.methodsProxy().layoutInterceptor() != null) {
			SKYHelper.methodsProxy().layoutInterceptor().showEmpty(this);
		}
		if (SKYBuilder != null) {
			SKYBuilder.layoutEmpty();
		}
	}

	@Override public void showHttpError() {
		if (SKYHelper.methodsProxy().layoutInterceptor() != null) {
			SKYHelper.methodsProxy().layoutInterceptor().showHttpError(this);
		}
		if (SKYBuilder != null) {
			SKYBuilder.layoutHttpError();
			recyclerRefreshing(false);
		}
	}

	@Override public int showState() {
		if (SKYBuilder != null) {
			return SKYBuilder.getLayoutState();
		} else {
			return SKYIView.STATE_CONTENT;
		}
	}

	@Override public <O extends SKYRVAdapter> O adapter() {
		return SKYBuilder == null ? null : (O) SKYBuilder.getSKYRVAdapterItem();
	}

	/**
	 * @return 参数
	 */
	public Toolbar toolbar() {
		return SKYBuilder == null ? null : SKYBuilder.getToolbar();
	}

	/**
	 * @return 返回值
	 */
	public RecyclerView.LayoutManager layoutManager() {
		return SKYBuilder == null ? null : SKYBuilder.getLayoutManager();
	}

	/**
	 * @return 返回值
	 */
	public <R extends RecyclerView> R recyclerView() {
		return SKYBuilder == null ? null : (R) SKYBuilder.getRecyclerView();
	}

	public void recyclerRefreshing(boolean bool) {
		if (SKYBuilder != null) {
			SKYBuilder.recyclerRefreshing(bool);
		}
	}

	public SwipeRefreshLayout swipRefesh() {
		if (SKYBuilder == null) {
			return null;
		}
		return SKYBuilder.getSwipeContainer();
	}

	public boolean onKeyBack() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			finishAfterTransition();
		} else {
			finish();
		}
		return true;
	}

	/**
	 * @param requestCode
	 *            参数
	 * @param permissions
	 *            参数
	 * @param grantResults
	 *            参数
	 */
	@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		SKYHelper.methodsProxy().activityInterceptor().onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	protected SystemBarTintManager tintManager() {
		return tintManager;
	}

	/**
	 * 滑动返回
	 *
	 * @param ev
	 *            事件
	 * @return 结果
	 */
	@Override public boolean dispatchTouchEvent(MotionEvent ev) {
		if (!supportSlideBack()) {
			return super.dispatchTouchEvent(ev);
		}

		if (mSwipeWindowHelper == null) {
			mSwipeWindowHelper = new SKYSwipeWindowHelper(getWindow());
		}
		return mSwipeWindowHelper.processTouchEvent(ev) || super.dispatchTouchEvent(ev);
	}

	protected void swipeWindowEdge(int edge) {
		if (mSwipeWindowHelper == null) {
			return;
		}
		mSwipeWindowHelper.setEdgeSize(edge);
	}

	protected int swipeEdgeDefaultSize() {
		if (mSwipeWindowHelper == null) {
			return 0;
		}
		return mSwipeWindowHelper.getEdgeDefalutSize();
	}

	/**
	 * 是否支持滑动返回
	 *
	 * @return true 支持 false 不支持
	 */
	protected boolean supportSlideBack() {
		if (SKYBuilder == null) {
			return false;
		}
		return SKYBuilder.isOpenSwipBackLayout();
	}

	/**
	 * 能否滑动返回至当前Activity
	 *
	 * @return true 支持 false 不支持
	 */
	public boolean canBeSlideBack() {
		return true;
	}

	/**
	 * 获取内容视图
	 *
	 * @return 视图
	 */
	protected View contentView() {
		return SKYBuilder.getContentRootView();
	}
}