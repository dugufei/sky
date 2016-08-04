package jc.sky.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYAppUtil;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.common.utils.SKYKeyboardUtils;
import jc.sky.core.SKYIBiz;
import jc.sky.display.SKYIDisplay;
import jc.sky.modules.structure.SKYStructureModel;
import jc.sky.view.adapter.SKYListAdapter;
import jc.sky.view.adapter.recycleview.SKYRVAdapter;

/**
 * @创建人 sky
 * @创建时间 15/7/8 上午12:15
 * @类描述 activity
 */
public abstract class SKYActivity<B extends SKYIBiz> extends AppCompatActivity {

	/**
	 * 定制
	 *
	 * @param initialJ2WBuilder
	 * @return
	 **/
	protected abstract SKYBuilder build(SKYBuilder initialJ2WBuilder);

	/**
	 * 数据
	 * 
	 * @param savedInstanceState
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
	private SKYBuilder j2WBuilder;

	SKYStructureModel j2WStructureModel;

	/**
	 * 初始化
	 *
	 * @param savedInstanceState
	 */
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 初始化结构 **/
		j2WStructureModel = new SKYStructureModel(this);
		SKYHelper.structureHelper().attach(j2WStructureModel);
		/** 初始化堆栈 **/
		SKYHelper.screenHelper().onCreate(this);
		/** 活动拦截器 **/
		SKYHelper.methodsProxy().activityInterceptor().onCreate(this, getIntent().getExtras(), savedInstanceState);
		/** 初始化视图 **/
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		j2WBuilder = new SKYBuilder(this, inflater);
		/** 拦截Builder **/
		SKYHelper.methodsProxy().activityInterceptor().build(j2WBuilder);
		setContentView(build(j2WBuilder).create());
		/** 状态栏高度 **/
		ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
		View parentView = contentFrameLayout.getChildAt(0);
		if (parentView != null && Build.VERSION.SDK_INT >= 14) {
			parentView.setFitsSystemWindows(true);
		}
		/** 状态栏颜色 **/
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// enable status bar tint
		tintManager.setStatusBarTintEnabled(j2WBuilder.getStatusBarTintEnabled());
		// enable navigation bar tint
		tintManager.setNavigationBarTintEnabled(j2WBuilder.getNavigationBarTintEnabled());
		tintManager.setStatusBarTintResource(j2WBuilder.getTintColor());
		/** 初始化所有组建 **/
		ButterKnife.bind(this);
		/** 初始化数据 **/
		createData(savedInstanceState);
		/** 初始化数据 **/
		initData(getIntent().getExtras());
	}

	@Override protected void onStart() {
		super.onStart();
		SKYHelper.methodsProxy().activityInterceptor().onStart(this);
	}

	@Override protected void onResume() {
		super.onResume();
		SKYHelper.screenHelper().onResume(this);
		SKYHelper.methodsProxy().activityInterceptor().onResume(this);

		/** 判断EventBus 是否注册 **/
		if (j2WBuilder.isOpenEventBus()) {
			if (!SKYHelper.eventBus().isRegistered(this)) {
				SKYHelper.eventBus().register(this);
			}
		}
		listLoadMoreOpen();
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SKYHelper.screenHelper().onActivityResult(this);
	}

	/**
	 * 设置输入法
	 *
	 * @param mode
	 */
	public void setSoftInputMode(int mode) {
		getWindow().setSoftInputMode(mode);
	}

	@Override protected void onPause() {
		super.onPause();
		SKYHelper.screenHelper().onPause(this);
		SKYHelper.methodsProxy().activityInterceptor().onPause(this);
		/** 判断EventBus 是否销毁 **/
		if (j2WBuilder.isOpenEventBus()) {
			if (!j2WBuilder.isNotCloseEventBus()) {
				if (SKYHelper.eventBus().isRegistered(this)) {
					SKYHelper.eventBus().unregister(this);
				}
			}
		}
		// 恢复初始化
		listRefreshing(false);
	}

	@Override protected void onRestart() {
		super.onRestart();
		SKYHelper.methodsProxy().activityInterceptor().onRestart(this);
	}

	@Override protected void onStop() {
		super.onStop();
		SKYHelper.methodsProxy().activityInterceptor().onStop(this);
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		detach();
		/** 关闭event **/
		if (SKYHelper.eventBus().isRegistered(this)) {
			SKYHelper.eventBus().unregister(this);
		}
		/** 移除builder **/
		j2WBuilder.detach();
		j2WBuilder = null;
		SKYHelper.structureHelper().detach(j2WStructureModel);
		SKYHelper.screenHelper().onDestroy(this);
		SKYHelper.methodsProxy().activityInterceptor().onDestroy(this);
		/** 关闭键盘 **/
		SKYKeyboardUtils.hideSoftInput(this);
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
	 * @return
	 */
	@Override public boolean onCreateOptionsMenu(Menu menu) {
		if (j2WBuilder != null && j2WBuilder.getToolbarMenuId() > 0) {
			getMenuInflater().inflate(j2WBuilder.getToolbarMenuId(), menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	public <D extends SKYIDisplay> D display(Class<D> eClass) {
		if (j2WStructureModel == null || j2WStructureModel.getView() == null) {
			return SKYHelper.display(eClass);
		}
		return j2WStructureModel.display(eClass);
	}

	public B biz() {
		if (j2WStructureModel == null || j2WStructureModel.getJ2WProxy() == null || j2WStructureModel.getJ2WProxy().proxy == null) {
			Class service = SKYAppUtil.getSuperClassGenricType(getClass(), 0);
			return (B) SKYHelper.structureHelper().createNullService(service);
		}
		return (B) j2WStructureModel.getJ2WProxy().proxy;
	}

	public <C extends SKYIBiz> C biz(Class<C> service) {
		if (j2WStructureModel != null && service.equals(j2WStructureModel.getService())) {
			if (j2WStructureModel == null || j2WStructureModel.getJ2WProxy() == null || j2WStructureModel.getJ2WProxy().proxy == null) {
				return SKYHelper.structureHelper().createNullService(service);
			}
			return (C) j2WStructureModel.getJ2WProxy().proxy;
		}
		return SKYHelper.biz(service);
	}

	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (SKYHelper.structureHelper().onKeyBack(keyCode, getSupportFragmentManager(), this)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**********************
	 * View业务代码
	 *********************/

	public <T> T findFragment(Class<T> clazz) {
		SKYCheckUtils.checkNotNull(clazz, "class不能为空");
		return (T) getSupportFragmentManager().findFragmentByTag(clazz.getName());
	}

	public SKYView j2wView() {
		return j2WBuilder == null ? null : j2WBuilder.getJ2WView();
	}

	/**********************
	 * Actionbar业务代码
	 *********************/

	protected void showContent() {
		if (j2WBuilder != null) {
			j2WBuilder.layoutContent();
		}
	}

	protected void showLoading() {
		if (j2WBuilder != null) {
			j2WBuilder.layoutLoading();
		}
	}

	protected void showBizError() {
		if (j2WBuilder != null) {
			j2WBuilder.layoutBizError();
		}
	}

	protected void showEmpty() {
		if (j2WBuilder != null) {
			j2WBuilder.layoutEmpty();
		}
	}

	protected void showHttpError() {
		if (j2WBuilder != null) {
			j2WBuilder.layoutHttpError();
		}
	}

	/**********************
	 * Actionbar业务代码
	 *********************/
	public Toolbar toolbar() {
		return j2WBuilder == null ? null : j2WBuilder.getToolbar();
	}

	/**********************
	 * RecyclerView业务代码
	 *********************/

	protected SKYRVAdapter recyclerAdapter() {
		return j2WBuilder == null ? null : j2WBuilder.getJ2WRVAdapterItem2();
	}

	protected RecyclerView.LayoutManager recyclerLayoutManager() {
		return j2WBuilder == null ? null : j2WBuilder.getLayoutManager();
	}

	protected RecyclerView recyclerView() {
		return j2WBuilder == null ? null : j2WBuilder.getRecyclerView();
	}

	/**********************
	 * ListView业务代码
	 *********************/

	protected void addListHeader() {
		if (j2WBuilder != null) {
			j2WBuilder.addListHeader();
		}
	}

	protected void addListFooter() {
		if (j2WBuilder != null) {
			j2WBuilder.addListFooter();
		}
	}

	protected void removeListHeader() {
		if (j2WBuilder != null) {
			j2WBuilder.removeListHeader();
		}
	}

	protected void removeListFooter() {
		if (j2WBuilder != null) {
			j2WBuilder.removeListFooter();
		}

	}

	protected void listRefreshing(boolean bool) {
		if (j2WBuilder != null) {
			j2WBuilder.listRefreshing(bool);
		}
	}

	protected void listLoadMoreOpen() {
		if (j2WBuilder != null) {
			j2WBuilder.loadMoreOpen();
		}
	}

	protected SKYListAdapter adapter() {
		return j2WBuilder == null ? null : j2WBuilder.getAdapter();
	}

	protected ListView listView() {
		return j2WBuilder == null ? null : j2WBuilder.getListView();
	}

	public boolean onKeyBack() {
		onBackPressed();
		return true;
	}

}