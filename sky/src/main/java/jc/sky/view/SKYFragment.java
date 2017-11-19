package jc.sky.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import jc.sky.core.SKYHelper;
import jc.sky.common.SKYAppUtil;
import jc.sky.common.SKYCheckUtils;
import jc.sky.common.SKYKeyboardUtils;
import jc.sky.core.SKYBiz;
import jc.sky.core.SKYIView;
import jc.sky.core.SKYStructureModel;
import jc.sky.display.SKYIDisplay;

/**
 * @author sky
 * @version 版本
 */
public abstract class SKYFragment<B extends SKYBiz> extends Fragment implements View.OnTouchListener, SKYIView {

	private boolean		targetActivity;

	SKYStructureModel	skyStructureModel;

	private Unbinder	unbinder;

	/**
	 * 定制
	 *
	 * @param initialSKYBuilder
	 *            参数
	 * @return 返回值
	 **/
	protected abstract jc.sky.view.SKYBuilder build(SKYBuilder initialSKYBuilder);

	/**
	 * 编译
	 *
	 * @param view
	 *            参数
	 */
	protected void buildAfter(View view) {}

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
	private SKYBuilder SKYBuilder;

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 打开开关触发菜单项 **/
		setHasOptionsMenu(true);
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/** 初始化结构 **/
		initCore();
		SKYHelper.structureHelper().attach(skyStructureModel);
		/** 初始化视图 **/
		SKYBuilder = new SKYBuilder(this, inflater);
		View view = build(SKYBuilder).create();
		/** 初始化所有组建 **/
		unbinder = ButterKnife.bind(this, view);
		/** 初始化点击事件 **/
		view.setOnTouchListener(this);// 设置点击事件
		/** build 之后 **/
		SKYHelper.methodsProxy().fragmentInterceptor().buildAfter(this);
		buildAfter(view);
		return view;
	}

	protected void initCore() {
		skyStructureModel = new SKYStructureModel(this, getArguments());
	}

	public Object model() {
		return skyStructureModel.getSKYProxy().impl;
	}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SKYHelper.methodsProxy().fragmentInterceptor().onFragmentCreated(this, getArguments(), savedInstanceState);
		/** 初始化业务数据 **/
		skyStructureModel.initBizBundle();
		/** 初始化dagger **/
		initDagger();
		createData(savedInstanceState);
		initData(getArguments());
	}

	@Override public void onStart() {
		super.onStart();
		SKYHelper.methodsProxy().fragmentInterceptor().onFragmentStart(this);
	}

	@Override public void onResume() {
		super.onResume();
		SKYHelper.methodsProxy().fragmentInterceptor().onFragmentResume(this);
		SKYHelper.structureHelper().printBackStackEntry(getFragmentManager());
	}

	@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		SKYHelper.methodsProxy().activityInterceptor().onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override public void onPause() {
		super.onPause();
		SKYHelper.methodsProxy().fragmentInterceptor().onFragmentPause(this);
		// 恢复初始化
		recyclerRefreshing(false);
	}

	@Override public void onStop() {
		super.onStop();
		SKYHelper.methodsProxy().fragmentInterceptor().onFragmentStop(this);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			getActivity().onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override public void onDetach() {
		super.onDetach();
		detach();
		/** 移除builder **/
		if (SKYBuilder != null) {
			SKYBuilder.detach();
			SKYBuilder = null;
		}
		if (skyStructureModel != null) {
			SKYHelper.structureHelper().detach(skyStructureModel);
		}
		/** 清空注解view **/
		unbinder.unbind();
		/** 关闭键盘 **/
		SKYKeyboardUtils.hideSoftInput(getActivity());
	}

	/**
	 * 清空
	 */
	protected void detach() {}

	@Override public void onDestroy() {
		super.onDestroy();
		SKYHelper.methodsProxy().fragmentInterceptor().onFragmentDestroy(this);
	}

	public <D extends SKYIDisplay> D display(Class<D> eClass) {
		return SKYHelper.display(eClass);
	}

	public B biz() {
		if (skyStructureModel == null || skyStructureModel.getSKYProxy() == null || skyStructureModel.getSKYProxy().proxy == null) {
			Class service = SKYAppUtil.getSuperClassGenricType(getClass(), 0);
			return (B) SKYHelper.structureHelper().createNullService(service);
		}
		return (B) skyStructureModel.getSKYProxy().proxy;
	}

	public <C extends SKYBiz> C biz(Class<C> service) {
		if (skyStructureModel != null && service.equals(skyStructureModel.getService())) {
			if (skyStructureModel == null || skyStructureModel.getSKYProxy() == null || skyStructureModel.getSKYProxy().proxy == null) {
				return SKYHelper.structureHelper().createNullService(service);
			}
			return (C) skyStructureModel.getSKYProxy().proxy;
		}
		return SKYHelper.biz(service);
	}

	/**
	 * 是否设置目标活动
	 *
	 * @return 返回值
	 */
	public boolean isTargetActivity() {
		return targetActivity;
	}

	/**
	 * 设置目标活动
	 *
	 * @param targetActivity
	 *            参数
	 */
	public void setTargetActivity(boolean targetActivity) {
		this.targetActivity = targetActivity;
	}

	/**
	 * 防止事件穿透
	 *
	 * @param v
	 *            View
	 * @param event
	 *            事件
	 * @return true 拦截 false 不拦截
	 */
	@Override public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	/**
	 * 返回键
	 *
	 * @return 返回值
	 */
	public boolean onKeyBack() {
		getFragmentManager().popBackStack();
		return true;
	}

	/**
	 * 设置输入法
	 *
	 * @param mode
	 *            参数
	 */
	protected void setSoftInputMode(int mode) {
		getActivity().getWindow().setSoftInputMode(mode);
	}

	/********************** View业务代码 *********************/
	/**
	 * 获取fragment
	 *
	 * @param <T>
	 *            参数
	 * @param clazz
	 *            参数
	 * @return 返回值
	 */
    protected <T> T findFragment(Class<T> clazz) {
		SKYCheckUtils.checkNotNull(clazz, "class不能为空");
		return (T) getFragmentManager().findFragmentByTag(clazz.getName());
	}

	/**
	 * 获取activity
	 *
	 * @param <A>
	 *            参数
	 * @return 返回值
	 */
	protected <A extends SKYActivity> A activity() {
		return (A) getActivity();
	}

    SKYView SKYView() {
		return SKYBuilder == null ? null : SKYBuilder.getSKYView();
	}

	/********************** Actionbar业务代码 *********************/

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

	/**********************
	 * Actionbar业务代码
	 *
	 * @return 返回值
	 *********************/
    protected Toolbar toolbar() {
		return SKYBuilder.getToolbar();
	}

	/**********************
	 * RecyclerView业务代码
	 *
	 * @return 返回值
	 *********************/

    protected RecyclerView.LayoutManager layoutManager() {
		return SKYBuilder == null ? null : SKYBuilder.getLayoutManager();
	}

    protected RecyclerView recyclerView() {
		return SKYBuilder == null ? null : SKYBuilder.getRecyclerView();
	}

    protected void recyclerRefreshing(boolean bool) {
		if (SKYBuilder != null) {
			SKYBuilder.recyclerRefreshing(bool);
		}
	}

    protected SwipeRefreshLayout swipRefesh() {
		if (SKYBuilder == null) {
			return null;
		}
		return SKYBuilder.getSwipeContainer();
	}

	/********************** ViewPager业务代码 *********************/

	/**
	 * 可见
	 */
	public void onVisible() {}

	/**
	 * 不可见
	 */
	public void onInvisible() {}

	/**
	 * 获取内容视图
	 *
	 * @return 视图
	 */
	protected View contentView() {
		return SKYBuilder.getContentRootView();
	}
}