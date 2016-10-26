package jc.sky.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.Unbinder;
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
 * @author sky
 * @version 版本
 */
public abstract class SKYFragment<B extends SKYIBiz> extends Fragment implements View.OnTouchListener {

	private boolean		targetActivity;

	SKYStructureModel	SKYStructureModel;

	private Unbinder	unbinder;

	/**
	 * 定制
	 *
	 * @param initialSKYBuilder
	 *            参数
	 * @return 返回值
	 **/
	protected abstract SKYBuilder build(SKYBuilder initialSKYBuilder);

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

	/** View层编辑器 **/
	private SKYBuilder SKYBuilder;

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 打开开关触发菜单项 **/
		setHasOptionsMenu(true);
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/** 初始化结构 **/
		SKYStructureModel = new SKYStructureModel(this);

		SKYHelper.structureHelper().attach(SKYStructureModel);
		/** 初始化视图 **/
		SKYBuilder = new SKYBuilder(this, inflater);
		View view = build(SKYBuilder).create();
		/** 初始化所有组建 **/
		unbinder = ButterKnife.bind(this, view);
		/** 初始化点击事件 **/
		view.setOnTouchListener(this);// 设置点击事件
		return view;
	}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SKYHelper.methodsProxy().fragmentInterceptor().onFragmentCreated(this, getArguments(), savedInstanceState);
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
		listLoadMoreOpen();
	}

	@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		SKYHelper.methodsProxy().activityInterceptor().onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override public void onPause() {
		super.onPause();
		SKYHelper.methodsProxy().fragmentInterceptor().onFragmentPause(this);
		// 恢复初始化
		listRefreshing(false);
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
		SKYBuilder.detach();
		SKYBuilder = null;
		SKYHelper.structureHelper().detach(SKYStructureModel);
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
		getActivity().onBackPressed();
		return true;
	}

	/**
	 * 设置输入法
	 * 
	 * @param mode
	 *            参数
	 */
	public void setSoftInputMode(int mode) {
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
	public <T> T findFragment(Class<T> clazz) {
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

	public SKYView SKYView() {
		return SKYBuilder == null ? null : SKYBuilder.getSKYView();
	}

	/********************** Actionbar业务代码 *********************/

	protected void showContent() {
		if (SKYBuilder != null) {
			SKYBuilder.layoutContent();
		}
	}

	protected void showLoading() {
		if (SKYBuilder != null) {
			SKYBuilder.layoutLoading();
		}
	}

	protected void showBizError() {
		if (SKYBuilder != null) {
			SKYBuilder.layoutBizError();
		}
	}

	protected void showEmpty() {
		if (SKYBuilder != null) {
			SKYBuilder.layoutEmpty();
		}
	}

	protected void showHttpError() {
		if (SKYBuilder != null) {
			SKYBuilder.layoutHttpError();
		}
	}

	/**********************
	 * Actionbar业务代码
	 * 
	 * @return 返回值
	 *********************/
	public Toolbar toolbar() {
		return SKYBuilder.getToolbar();

	}

	/**********************
	 * RecyclerView业务代码
	 * 
	 * @return 返回值
	 *********************/

	protected SKYRVAdapter recyclerAdapter() {
		return SKYBuilder == null ? null : SKYBuilder.getSKYRVAdapterItem2();
	}

	protected RecyclerView.LayoutManager recyclerLayoutManager() {
		return SKYBuilder == null ? null : SKYBuilder.getLayoutManager();
	}

	public RecyclerView recyclerView() {
		return SKYBuilder == null ? null : SKYBuilder.getRecyclerView();
	}

	/********************** ListView业务代码 *********************/

	protected void addListHeader() {
		if (SKYBuilder != null) {
			SKYBuilder.addListHeader();
		}
	}

	protected void addListFooter() {
		if (SKYBuilder != null) {
			SKYBuilder.addListFooter();
		}
	}

	protected void removeListHeader() {
		if (SKYBuilder != null) {
			SKYBuilder.removeListHeader();
		}
	}

	protected void removeListFooter() {
		if (SKYBuilder != null) {
			SKYBuilder.removeListFooter();
		}

	}

	protected void listRefreshing(boolean bool) {
		if (SKYBuilder != null) {
			SKYBuilder.listRefreshing(bool);
		}
	}

	protected void listLoadMoreOpen() {
		if (SKYBuilder != null) {
			SKYBuilder.loadMoreOpen();
		}
	}

	protected SKYListAdapter adapter() {
		return SKYBuilder == null ? null : SKYBuilder.getAdapter();
	}

	protected ListView listView() {
		return SKYBuilder == null ? null : SKYBuilder.getListView();
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
}