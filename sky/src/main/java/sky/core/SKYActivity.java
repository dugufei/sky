package sky.core;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.ButterKnife;
import sky.core.interfaces.SKYIView;
import sky.core.view.swipewindow.SKYSwipeWindowHelper;

import static sky.core.SKYUtils.checkNotNull;

/**
 * @author sky
 * @version 版本
 */
public abstract class SKYActivity<B extends SKYBiz> extends AppCompatActivity implements SKYIView {

	/**
	 * 定制
	 *
	 * @param initialSKYBuilder
	 *            参数
	 * @return 返回值
	 **/
	protected abstract sky.core.SKYBuilder build(SKYBuilder initialSKYBuilder);

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
	private SKYBuilder				SKYBuilder;

	private SKYStructureModel		skyStructureModel;

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
		if (skyStructureModel != null) {
			skyStructureModel.initBizBundle();
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
		skyStructureModel = new SKYStructureModel(this, getIntent() == null ? null : getIntent().getExtras());
		SKYHelper.structureHelper().attach(skyStructureModel);
	}

	Object model() {
		if (skyStructureModel.getSKYProxy() == null) {
			return null;
		}
		return skyStructureModel.getSKYProxy().impl;
	}

	protected final B biz() {
		if (skyStructureModel == null || skyStructureModel.getSKYProxy() == null || skyStructureModel.getSKYProxy().proxy == null) {
			Class service = SKYUtils.getSuperClassGenricType(getClass(), 0);
			return (B) SKYHelper.structureHelper().createNullService(service);
		}
		return (B) skyStructureModel.getSKYProxy().proxy;
	}

	protected  <C extends SKYBiz> C biz(Class<C> service) {
		if (skyStructureModel != null && service.equals(skyStructureModel.getService())) {
			if (skyStructureModel == null || skyStructureModel.getSKYProxy() == null || skyStructureModel.getSKYProxy().proxy == null) {
				return SKYHelper.structureHelper().createNullService(service);
			}
			return (C) skyStructureModel.getSKYProxy().proxy;
		}
		return SKYHelper.biz(service);
	}

	protected final <D extends SKYIDisplay> D display(Class<D> eClass) {
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
			if (skyStructureModel != null) {
				SKYHelper.structureHelper().detach(skyStructureModel);
			}
			SKYHelper.screenHelper().onDestroy(this);
			SKYHelper.methodsProxy().activityInterceptor().onDestroy(this);
			/** 关闭键盘 **/
			hideSoftInput();
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
			if (skyStructureModel != null) {
				SKYHelper.structureHelper().detach(skyStructureModel);
			}
			SKYHelper.screenHelper().onDestroy(this);
			SKYHelper.methodsProxy().activityInterceptor().onDestroy(this);
			/** 关闭键盘 **/
			hideSoftInput();
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
		checkNotNull(clazz, "class不能为空");
		return (T) getSupportFragmentManager().findFragmentByTag(clazz.getName());
	}

	SKYView SKYView() {
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

	@Override public void close() {
		onKeyBack();
	}

	@Override public int showState() {
		if (SKYBuilder != null) {
			return SKYBuilder.getLayoutState();
		} else {
			return STATE_CONTENT;
		}
	}

	@Override public <O extends SKYRVAdapter> O adapter() {
		return SKYBuilder == null ? null : (O) SKYBuilder.getSKYRVAdapterItem();
	}

	/**
	 * @return 参数
	 */
	protected final Toolbar toolbar() {
		return SKYBuilder == null ? null : SKYBuilder.getToolbar();
	}

	/**
	 * @return 返回值
	 */
	protected final RecyclerView.LayoutManager layoutManager() {
		return SKYBuilder == null ? null : SKYBuilder.getLayoutManager();
	}

	/**
	 * @return 返回值
	 */
	protected final <R extends RecyclerView> R recyclerView() {
		return SKYBuilder == null ? null : (R) SKYBuilder.getRecyclerView();
	}

	protected final void recyclerRefreshing(boolean bool) {
		if (SKYBuilder != null) {
			SKYBuilder.recyclerRefreshing(bool);
		}
	}

	protected final SwipeRefreshLayout swipRefesh() {
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

	protected final SystemBarTintManager tintManager() {
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
	@Deprecated public boolean canBeSlideBack() {
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

	/**
	 * 进度条
	 */
	@Override public void loading() {
		SKYHelper.methodsProxy().activityInterceptor().onShowLoading(this);
	}

	/**
	 * 关闭进度条
	 */
	@Override public void closeLoading() {
		SKYHelper.methodsProxy().activityInterceptor().onCloseLoading(this);
	}

	/**
	 * 显示键盘
	 * 
	 * @param et
	 */
	protected void showSoftInput(EditText et) {
		if (et == null) return;
		et.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et, InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	/**
	 * 延迟显示键盘
	 * 
	 * @param et
	 */
	protected void showSoftInputDelay(final EditText et) {
		et.postDelayed(new Runnable() {

			@Override public void run() {
				showSoftInput(et);
			}
		}, 300);
	}

	/**
	 * 隐藏键盘
	 */
	protected void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindow().getDecorView().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}