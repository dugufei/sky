package jc.sky.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import jc.sky.R;
import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.common.utils.SKYKeyboardUtils;
import jc.sky.modules.log.L;
import jc.sky.view.adapter.SKYAdapterItem;
import jc.sky.view.adapter.SKYListAdapter;
import jc.sky.view.adapter.SKYListViewMultiLayout;
import jc.sky.view.adapter.recycleview.SKYRVAdapter;
import jc.sky.view.adapter.recycleview.stickyheader.SKYStickyHeaders;
import jc.sky.view.adapter.recycleview.stickyheader.StickyRecyclerHeadersDecoration;
import jc.sky.view.adapter.recycleview.stickyheader.StickyRecyclerHeadersTouchListener;
import jc.sky.view.common.SKYFooterListener;
import jc.sky.view.common.SKYRefreshListener;

/**
 * @author sky
 * @version 版本
 */
public class SKYBuilder implements AbsListView.OnScrollListener {

	/**
	 * UI
	 **/
	private SKYView			skyView;

	/**
	 * 布局加载器
	 **/
	private LayoutInflater	mInflater;

	/**
	 * 构造器
	 *
	 * @param skyActivity
	 *            参数
	 * @param inflater
	 *            参数
	 */
	public SKYBuilder(@NonNull SKYActivity skyActivity, @NonNull LayoutInflater inflater) {
		skyView = new SKYView();
		skyView.initUI(skyActivity);
		this.mInflater = inflater;
	}

	/**
	 * 构造器
	 *
	 * @param SKYFragment
	 *            参数
	 * @param inflater
	 *            参数
	 */
	public SKYBuilder(@NonNull SKYFragment SKYFragment, @NonNull LayoutInflater inflater) {
		skyView = new SKYView();
		skyView.initUI(SKYFragment);
		this.mInflater = inflater;
	}

	/**
	 * 构造器
	 *
	 * @param SKYDialogFragment
	 *            参数
	 * @param inflater
	 *            参数
	 */
	public SKYBuilder(@NonNull SKYDialogFragment SKYDialogFragment, @NonNull LayoutInflater inflater) {
		skyView = new SKYView();
		skyView.initUI(SKYDialogFragment);
		this.mInflater = inflater;
	}

	@Nullable public SKYView getSKYView() {
		return skyView;
	}

	/**
	 * 布局ID
	 */
	private int			layoutId;

	private FrameLayout	contentRoot;

	private int			contentRootColor;

	int getLayoutId() {
		return layoutId;
	}

	public void layoutId(@LayoutRes int layoutId) {
		this.layoutId = layoutId;
	}

	public void layoutColor(@ColorRes int color) {
		this.contentRootColor = color;
	}

	/**
	 * 显示状态切换
	 */

	private int			layoutLoadingId;

	private int			layoutEmptyId;

	private int			layoutBizErrorId;

	private int			layoutHttpErrorId;

	private View		layoutContent;

	private ViewStub	vsLoading;

	private View		layoutLoading;

	private View		layoutEmpty;

	private View		layoutBizError;

	private View		layoutHttpError;

	// 设置
	public void layoutLoadingId(@LayoutRes int layoutLoadingId) {
		this.layoutLoadingId = layoutLoadingId;
	}

	public void layoutEmptyId(@LayoutRes int layoutEmptyId) {
		this.layoutEmptyId = layoutEmptyId;
	}

	public void layoutBizErrorId(@LayoutRes int layoutBizErrorId) {
		this.layoutBizErrorId = layoutBizErrorId;
	}

	public void layoutHttpErrorId(@LayoutRes int layoutHttpErrorId) {
		this.layoutHttpErrorId = layoutHttpErrorId;
	}

	// 功能
	void layoutContent() {
		if (layoutContent == null) {
			return;
		}
		changeShowAnimation(layoutLoading, false);
		changeShowAnimation(layoutEmpty, false);
		changeShowAnimation(layoutBizError, false);
		changeShowAnimation(layoutHttpError, false);
		changeShowAnimation(layoutContent, true);
	}

	void layoutLoading() {
		if (layoutLoadingId < 1) {
			return;
		}
		changeShowAnimation(layoutEmpty, false);
		changeShowAnimation(layoutBizError, false);
		changeShowAnimation(layoutHttpError, false);
		changeShowAnimation(layoutContent, false);
		if (layoutLoading == null && vsLoading != null) {
			layoutLoading = vsLoading.inflate();
			SKYCheckUtils.checkNotNull(layoutLoading, "无法根据布局文件ID,获取layoutLoading");
		}
		changeShowAnimation(layoutLoading, true);
	}

	void layoutEmpty() {
		if (layoutEmpty == null) {
			return;
		}
		changeShowAnimation(layoutBizError, false);
		changeShowAnimation(layoutHttpError, false);
		changeShowAnimation(layoutContent, false);
		changeShowAnimation(layoutLoading, false);
		changeShowAnimation(layoutEmpty, true);
	}

	void layoutBizError() {
		if (layoutBizError == null) {
			return;
		}
		changeShowAnimation(layoutEmpty, false);
		changeShowAnimation(layoutHttpError, false);
		changeShowAnimation(layoutContent, false);
		changeShowAnimation(layoutLoading, false);
		changeShowAnimation(layoutBizError, true);
	}

	void layoutHttpError() {
		if (layoutHttpError == null) {
			return;
		}
		changeShowAnimation(layoutEmpty, false);
		changeShowAnimation(layoutBizError, false);
		changeShowAnimation(layoutContent, false);
		changeShowAnimation(layoutLoading, false);
		changeShowAnimation(layoutHttpError, true);
	}

	void changeShowAnimation(@NonNull View view, boolean visible) {
		if (view == null) {
			return;
		}
		Animation anim;
		if (visible) {
			if (view.getVisibility() == View.VISIBLE) {
				return;
			}
			view.setVisibility(View.VISIBLE);
			anim = AnimationUtils.loadAnimation(skyView.activity(), android.R.anim.fade_in);
		} else {
			if (view.getVisibility() == View.GONE) {
				return;
			}
			view.setVisibility(View.GONE);
			anim = AnimationUtils.loadAnimation(skyView.activity(), android.R.anim.fade_out);
		}

		anim.setDuration(skyView.activity().getResources().getInteger(android.R.integer.config_shortAnimTime));
		view.startAnimation(anim);
	}

	/**
	 * 键盘
	 */
	private boolean autoShouldHideInput = true;

	public void autoKeyBoard(boolean auto) {
		this.autoShouldHideInput = auto;
	}

	boolean isAutoKeyBoard() {
		return this.autoShouldHideInput;
	}

	/**
	 * swipback
	 */

	private boolean		isOpenSwipBackLayout;

	private ImageView	ivShadow;

	public void swipBackIsOpen(boolean isOpenSwipBackLayout) {
		this.isOpenSwipBackLayout = isOpenSwipBackLayout;
	}

	// 获取
	boolean isOpenSwipBackLayout() {
		return isOpenSwipBackLayout;
	}

	/**
	 * TintManger
	 */
	private int		tintColor;

	private boolean	statusBarEnabled			= true;

	private boolean	navigationBarTintEnabled	= true;

	int getTintColor() {
		return tintColor;
	}

	boolean isTintColor() {
		return tintColor > 0;
	}

	public boolean getStatusBarTintEnabled() {
		return statusBarEnabled;
	}

	public boolean getNavigationBarTintEnabled() {
		return navigationBarTintEnabled;
	}

	public void tintColor(@ColorRes int tintColor) {
		this.tintColor = tintColor;
	}

	public void tintStatusBarEnabled(boolean isStatusBar) {
		this.statusBarEnabled = isStatusBar;
	}

	public void tintNavigationBarEnabled(boolean isNavigationBar) {
		this.navigationBarTintEnabled = isNavigationBar;
	}

	/**
	 * actionbar
	 */

	private Toolbar							toolbar;

	private Toolbar.OnMenuItemClickListener	menuListener;

	private int								toolbarLayoutId	= R.layout.sky_include_toolbar;

	private int								toolbarId		= R.id.toolbar;

	private int								toolbarMenuId;

	private int								toolbarDrawerId;

	private boolean							isOpenToolbar;

	private boolean							isOpenCustomToolbar;

	private boolean							isOpenToolbarBack;

	// 获取
	int getToolbarLayoutId() {
		return toolbarLayoutId;
	}

	public boolean isOpenCustomToolbar() {
		return isOpenCustomToolbar;
	}

	boolean isOpenToolbar() {
		return isOpenToolbar;
	}

	boolean isOpenToolbarBack() {
		return isOpenToolbarBack;
	}

	int getToolbarId() {
		return toolbarId;
	}

	int getToolbarMenuId() {
		return toolbarMenuId;
	}

	int getToolbarDrawerId() {
		return toolbarDrawerId;
	}

	@Nullable Toolbar getToolbar() {
		return toolbar;
	}

	@Nullable Toolbar.OnMenuItemClickListener getMenuListener() {
		return menuListener;
	}

	// 设置

	public void toolbarId(@IdRes int toolbarId) {
		this.toolbarId = toolbarId;
		this.isOpenCustomToolbar = true;
	}

	public void toolbarLayoutId(@LayoutRes int toolbarLayoutId) {
		this.toolbarLayoutId = toolbarLayoutId;
	}

	public void toolbarDrawerId(@DrawableRes int toolbarDrawerId) {
		this.toolbarDrawerId = toolbarDrawerId;
	}

	public void toolbarMenuListener(@NonNull Toolbar.OnMenuItemClickListener menuListener) {
		this.menuListener = menuListener;
	}

	public void toolbarIsBack(@NonNull boolean isOpenToolbarBack) {
		this.isOpenToolbarBack = isOpenToolbarBack;
	}

	public void toolbarIsOpen(@NonNull boolean isOpenToolbar) {
		this.isOpenToolbar = isOpenToolbar;
	}

	public void toolbarMenuId(@MenuRes int toolbarMenuId) {
		this.toolbarMenuId = toolbarMenuId;
	}

	/**
	 * ListView
	 */
	private SKYListAdapter				SKYListAdapter;

	private ListView					listView;

	private View						header;

	private View						footer;

	private SwipeRefreshLayout			swipe_container;

	private SKYRefreshListener			SKYRefreshListener;

	private boolean						mLoadMoreIsAtBottom;			// 加载更多

	// 开关

	private int							mLoadMoreRequestedItemCount;	// 加载更多

	// 数量

	private int							colorResIds[];

	private int							swipRefreshId;

	private int							listId;

	private int							listHeaderLayoutId;

	private int							listFooterLayoutId;

	SKYAdapterItem						SKYAdapterItem;

	SKYListViewMultiLayout				SKYListViewMultiLayout;

	AdapterView.OnItemClickListener		itemListener;

	AdapterView.OnItemLongClickListener	itemLongListener;

	// 获取
	int getListId() {
		return listId;
	}

	@Nullable SKYAdapterItem getSKYAdapterItem() {
		return SKYAdapterItem;
	}

	@Nullable SKYListViewMultiLayout getSKYListViewMultiLayout() {
		return SKYListViewMultiLayout;
	}

	@Nullable AdapterView.OnItemClickListener getItemListener() {
		return itemListener;
	}

	@Nullable AdapterView.OnItemLongClickListener getItemLongListener() {
		return itemLongListener;
	}

	int getListHeaderLayoutId() {
		return listHeaderLayoutId;
	}

	int getListFooterLayoutId() {
		return listFooterLayoutId;
	}

	@Nullable SKYListAdapter getAdapter() {
		SKYCheckUtils.checkNotNull(SKYListAdapter, "适配器没有初始化");
		return SKYListAdapter;
	}

	@Nullable ListView getListView() {
		SKYCheckUtils.checkNotNull(listView, "没有设置布局文件ID,无法获取ListView");
		return listView;
	}

	int getSwipRefreshId() {
		return swipRefreshId;
	}

	int[] getSwipeColorResIds() {
		return colorResIds;
	}

	// 设置
	public void listHeaderLayoutId(@LayoutRes int listHeaderLayoutId) {
		this.listHeaderLayoutId = listHeaderLayoutId;
	}

	public void listFooterLayoutId(@LayoutRes int listFooterLayoutId) {
		this.listFooterLayoutId = listFooterLayoutId;
	}

	public void listViewOnItemClick(@NonNull AdapterView.OnItemClickListener itemListener) {
		this.itemListener = itemListener;
	}

	public void listViewOnItemLongClick(@NonNull AdapterView.OnItemLongClickListener itemLongListener) {
		this.itemLongListener = itemLongListener;
	}

	public void listViewId(@IdRes int listId, @NonNull SKYAdapterItem SKYAdapterItem) {
		this.listId = listId;
		this.SKYAdapterItem = SKYAdapterItem;
	}

	public void listViewId(@IdRes int listId, @NonNull SKYListViewMultiLayout SKYListViewMultiLayout) {
		this.listId = listId;
		this.SKYListViewMultiLayout = SKYListViewMultiLayout;
	}

	public void listSwipRefreshId(@IdRes int swipRefreshId, @NonNull SKYRefreshListener SKYRefreshListener) {
		this.swipRefreshId = swipRefreshId;
		this.SKYRefreshListener = SKYRefreshListener;
	}

	public void listSwipeColorResIds(int... colorResIds) {
		this.colorResIds = colorResIds;
	}

	// 功能
	void addListHeader() {
		if (listView != null && header != null) {
			listView.addHeaderView(header);
		}
	}

	void addListFooter() {
		if (listView != null && footer != null) {
			listView.addFooterView(footer);
		}
	}

	void removeListHeader() {
		if (listView != null && header != null) {
			listView.removeHeaderView(header);
		}
	}

	void removeListFooter() {
		if (listView != null && footer != null) {
			listView.removeFooterView(footer);
		}
	}

	void listRefreshing(boolean bool) {
		if (swipe_container != null) {
			swipe_container.setRefreshing(bool);
		}
		if (recyclerviewSwipeContainer != null) {
			recyclerviewSwipeContainer.setRefreshing(bool);
		}
	}

	void loadMoreOpen() {
		mLoadMoreIsAtBottom = true;
		mLoadMoreRequestedItemCount = 0;
	}

	/**
	 * RecyclerView 替代ListView GradView 可以实现瀑布流
	 */

	private int															recyclerviewId;

	private int															recyclerviewColorResIds[];

	private int															recyclerviewSwipRefreshId;

	private SKYFooterListener											SKYFooterListener;

	private RecyclerView												recyclerView;

	private SKYRVAdapter												SKYRVAdapter;

	private RecyclerView.LayoutManager									layoutManager;					// 布局管理器

	private RecyclerView.ItemAnimator									itemAnimator;					// 动画

	private RecyclerView.ItemDecoration									itemDecoration;					// 分割线

	private SwipeRefreshLayout											recyclerviewSwipeContainer;

	private SKYRefreshListener											recyclerviewSKYRefreshListener;

	private StickyRecyclerHeadersTouchListener.OnHeaderClickListener	onHeaderClickListener;

	private boolean														isHeaderFooter;

	// 获取
	int getRecyclerviewId() {
		return recyclerviewId;
	}

	RecyclerView getRecyclerView() {
		SKYCheckUtils.checkNotNull(recyclerView, "RecyclerView没有找到，查看布局里是否存在~");
		return recyclerView;
	}

	@Nullable SKYRVAdapter getSKYRVAdapterItem2() {
		return SKYRVAdapter;
	}

	@Nullable public RecyclerView.LayoutManager getLayoutManager() {
		return layoutManager;
	}

	@Nullable RecyclerView.ItemAnimator getItemAnimator() {
		return itemAnimator;
	}

	@Nullable RecyclerView.ItemDecoration getItemDecoration() {
		return itemDecoration;
	}

	int[] getRecyclerviewColorResIds() {
		return recyclerviewColorResIds;
	}

	int getRecyclerviewSwipRefreshId() {
		return recyclerviewSwipRefreshId;
	}

	public void recyclerviewGridOpenHeaderFooter(boolean bool) {
		this.isHeaderFooter = bool;
	}

	// 设置
	public void recyclerviewId(@IdRes int recyclerviewId) {
		this.recyclerviewId = recyclerviewId;
	}

	public void recyclerviewLoadingMore(@NonNull SKYFooterListener SKYFooterListener) {
		this.SKYFooterListener = SKYFooterListener;
	}

	public void recyclerviewStickyHeaderClick(@NonNull StickyRecyclerHeadersTouchListener.OnHeaderClickListener onHeaderClickListener) {
		this.onHeaderClickListener = onHeaderClickListener;
	}

	public void recyclerviewAdapter(@NonNull SKYRVAdapter SKYRVAdapter) {
		this.SKYRVAdapter = SKYRVAdapter;
	}

	public void recyclerviewGridManager(@NonNull GridLayoutManager gridLayoutManager) {
		this.layoutManager = gridLayoutManager;
	}

	public void recyclerviewLinearManager(@NonNull LinearLayoutManager linearLayoutManager) {
		this.layoutManager = linearLayoutManager;
	}

	public void recyclerviewAnimator(@NonNull RecyclerView.ItemAnimator itemAnimator) {
		this.itemAnimator = itemAnimator;
	}

	public void recyclerviewLinearLayoutManager(int direction, RecyclerView.ItemDecoration itemDecoration, RecyclerView.ItemAnimator itemAnimator, boolean... reverseLayout) {
		boolean reverse = false;
		if (reverseLayout != null && reverseLayout.length > 0) {
			reverse = reverseLayout[0];
		}
		this.layoutManager = new LinearLayoutManager(skyView.activity(), direction, reverse);
		this.itemDecoration = itemDecoration;
		this.itemAnimator = itemAnimator;
	}

	public void recyclerviewGridLayoutManager(int direction, int spanCount, RecyclerView.ItemDecoration itemDecoration, RecyclerView.ItemAnimator itemAnimator, boolean... reverseLayout) {
		boolean reverse = false;
		if (reverseLayout != null && reverseLayout.length > 0) {
			reverse = reverseLayout[0];
		}
		this.layoutManager = new GridLayoutManager(skyView.activity(), spanCount, direction, reverse);
		this.itemDecoration = itemDecoration;
		this.itemAnimator = itemAnimator == null ? new DefaultItemAnimator() : itemAnimator;
	}

	public void recyclerviewStaggeredGridyoutManager(int direction, int spanCount, RecyclerView.ItemDecoration itemDecoration, RecyclerView.ItemAnimator itemAnimator, boolean... reverseLayout) {
		this.layoutManager = new StaggeredGridLayoutManager(spanCount, direction);
		this.itemDecoration = itemDecoration;
		this.itemAnimator = itemAnimator == null ? new DefaultItemAnimator() : itemAnimator;
	}

	public void recyclerviewColorResIds(int... recyclerviewColorResIds) {
		this.recyclerviewColorResIds = recyclerviewColorResIds;
	}

	public void recyclerviewSwipRefreshId(@IdRes int recyclerviewSwipRefreshId, @NonNull SKYRefreshListener recyclerviewSKYRefreshListener) {
		this.recyclerviewSwipRefreshId = recyclerviewSwipRefreshId;
		this.recyclerviewSKYRefreshListener = recyclerviewSKYRefreshListener;
	}

	/**
	 * 创建
	 *
	 * @return
	 */
	View create() {
		L.i("SKYBuilder.create()");
		/** layout **/
		createLayout();
		/** listview **/
		createListView(contentRoot);
		/** recyclerview **/
		createRecyclerView(contentRoot);
		/** actoinbar **/
		View view = createActionbar(contentRoot);
		return view;
	}

	/**
	 * 清空所有
	 */
	void detach() {
		// 清楚
		if (skyView != null) {
			skyView.detach();
			skyView = null;
		}
		// 基础清除
		detachLayout();
		// actionbar清除
		detachActionbar();
		// listview清除
		detachListView();
		// recyclerview清楚
		detachRecyclerView();
	}

	/**
	 * 布局
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) private void createLayout() {
		contentRoot = new FrameLayout(skyView.context());
		if (contentRootColor > 0) {
			contentRoot.setBackgroundColor(contentRootColor);
		}
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

		// 内容
		if (getLayoutId() > 0) {
			layoutContent = mInflater.inflate(getLayoutId(), null, false);
			SKYCheckUtils.checkNotNull(layoutContent, "无法根据布局文件ID,获取layoutContent");
			contentRoot.addView(layoutContent, layoutParams);
		}

		// 进度条
		layoutLoadingId = layoutLoadingId > 0 ? layoutLoadingId : SKYHelper.getComnonView() == null ? 0 : SKYHelper.getComnonView().layoutLoading();
		if (layoutLoadingId > 0) {
			vsLoading = new ViewStub(skyView.activity());
			vsLoading.setLayoutResource(layoutLoadingId);
			contentRoot.addView(vsLoading, layoutParams);
		}

		// 空布局
		layoutEmptyId = layoutEmptyId > 0 ? layoutEmptyId : SKYHelper.getComnonView() == null ? 0 : SKYHelper.getComnonView().layoutEmpty();
		if (layoutEmptyId > 0) {
			layoutEmpty = mInflater.inflate(layoutEmptyId, null, false);
			SKYCheckUtils.checkNotNull(layoutEmpty, "无法根据布局文件ID,获取layoutEmpty");
			contentRoot.addView(layoutEmpty, layoutParams);
			layoutEmpty.setVisibility(View.GONE);
		}

		// 业务错误布局
		layoutBizErrorId = layoutBizErrorId > 0 ? layoutBizErrorId : SKYHelper.getComnonView() == null ? 0 : SKYHelper.getComnonView().layoutBizError();
		if (layoutBizErrorId > 0) {
			layoutBizError = mInflater.inflate(layoutBizErrorId, null, false);
			SKYCheckUtils.checkNotNull(layoutBizError, "无法根据布局文件ID,获取layoutBizError");
			contentRoot.addView(layoutBizError, layoutParams);
			layoutBizError.setVisibility(View.GONE);
		}

		// 网络错误布局
		layoutHttpErrorId = layoutHttpErrorId > 0 ? layoutHttpErrorId : SKYHelper.getComnonView() == null ? 0 : SKYHelper.getComnonView().layoutHttpError();
		if (layoutHttpErrorId > 0) {
			SKYCheckUtils.checkArgument(layoutHttpErrorId > 0, "网络错误布局Id不能为空,重写公共布局Application.layoutBizError 或者 在Buider.layout里设置");
			layoutHttpError = mInflater.inflate(layoutHttpErrorId, null, false);
			SKYCheckUtils.checkNotNull(layoutHttpError, "无法根据布局文件ID,获取layoutHttpError");
			contentRoot.addView(layoutHttpError, layoutParams);
			layoutHttpError.setVisibility(View.GONE);
		}
	}

	private void detachLayout() {
		contentRoot = null;
		mInflater = null;
		layoutContent = null;
		layoutBizError = null;
		layoutHttpError = null;
		layoutEmpty = null;
		vsLoading = null;
		layoutLoading = null;
		ivShadow = null;
	}

	/**
	 * 标题栏
	 *
	 * @param view
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) private View createActionbar(View view) {
		if (isOpenToolbar()) {
			final RelativeLayout toolbarRoot = new RelativeLayout(skyView.context());
			toolbarRoot.setId(R.id.sky_home);
			toolbarRoot.setFitsSystemWindows(true);
			// 添加toolbar布局
			mInflater.inflate(getToolbarLayoutId(), toolbarRoot, true);
			// 添加内容布局
			RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			contentLayoutParams.addRule(RelativeLayout.BELOW, R.id.toolbar);
			toolbarRoot.addView(view, contentLayoutParams);
			toolbar = ButterKnife.findById(toolbarRoot, getToolbarId());

			SKYCheckUtils.checkNotNull(toolbar, "无法根据布局文件ID,获取Toolbar");

			if (getToolbarDrawerId() > 0) {
				DrawerLayout drawerLayout = ButterKnife.findById(view, getToolbarDrawerId());
				SKYCheckUtils.checkNotNull(drawerLayout, "无法根据布局文件ID,获取DrawerLayout");
				ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(skyView.activity(), drawerLayout, toolbar, R.string.app_name, R.string.app_name);
				mDrawerToggle.syncState();
				drawerLayout.setDrawerListener(mDrawerToggle);
			}
			// 添加点击事件
			if (getMenuListener() != null) {
				toolbar.setOnMenuItemClickListener(getMenuListener());
			}
			if (getToolbarMenuId() > 0) {
				toolbar.inflateMenu(getToolbarMenuId());
			}
			if (isOpenToolbarBack()) {
				toolbar.setNavigationOnClickListener(new View.OnClickListener() {

					@Override public void onClick(View v) {
						SKYKeyboardUtils.hideSoftInput(skyView.activity());
						switch (skyView.getState()) {
							case SKYView.STATE_ACTIVITY:
								skyView.activity().onKeyBack();
								break;
							case SKYView.STATE_FRAGMENT:
								skyView.fragment().onKeyBack();
								break;
							case SKYView.STATE_DIALOGFRAGMENT:
								skyView.dialogFragment().onKeyBack();
								break;
						}
					}
				});
			} else {
			}

			toolbarRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

			return toolbarRoot;
		} else if (isOpenCustomToolbar()) {
			view.setId(R.id.sky_home);
			view.setFitsSystemWindows(true);
			toolbar = ButterKnife.findById(view, getToolbarId());

			SKYCheckUtils.checkNotNull(toolbar, "无法根据布局文件ID,获取Toolbar");
			if (getToolbarDrawerId() > 0) {
				DrawerLayout drawerLayout = ButterKnife.findById(view, getToolbarDrawerId());
				SKYCheckUtils.checkNotNull(drawerLayout, "无法根据布局文件ID,获取DrawerLayout");
				ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(skyView.activity(), drawerLayout, toolbar, R.string.app_name, R.string.app_name);
				mDrawerToggle.syncState();
				drawerLayout.setDrawerListener(mDrawerToggle);
			}
			if (isOpenToolbarBack()) {
				toolbar.setNavigationOnClickListener(new View.OnClickListener() {

					@Override public void onClick(View v) {
						SKYKeyboardUtils.hideSoftInput(skyView.activity());
						switch (skyView.getState()) {
							case SKYView.STATE_ACTIVITY:
								skyView.activity().onKeyBack();
								break;
							case SKYView.STATE_FRAGMENT:
								skyView.fragment().onKeyBack();
								break;
							case SKYView.STATE_DIALOGFRAGMENT:
								skyView.dialogFragment().onKeyBack();
								break;
						}
					}
				});
			}
			// 添加点击事件
			if (getMenuListener() != null) {
				toolbar.setOnMenuItemClickListener(getMenuListener());
			}
			if (getToolbarMenuId() > 0) {
				toolbar.inflateMenu(getToolbarMenuId());
			}

			return view;
		} else {
			view.setId(R.id.sky_home);
			view.setFitsSystemWindows(true);
			return view;
		}
	}

	private void detachActionbar() {
		menuListener = null;
		toolbar = null;
		menuListener = null;
	}

	/**
	 * 列表
	 *
	 * @param view
	 */
	private void createListView(View view) {
		if (getListId() > 0) {
			listView = ButterKnife.findById(view, getListId());
			SKYCheckUtils.checkNotNull(listView, "无法根据布局文件ID,获取ListView");
			// 添加头布局
			if (getListHeaderLayoutId() != 0) {
				header = mInflater.inflate(getListHeaderLayoutId(), null, false);
				SKYCheckUtils.checkNotNull(header, "无法根据布局文件ID,获取ListView 头布局");
				addListHeader();
			}
			// 添加尾布局
			if (getListFooterLayoutId() != 0) {
				footer = mInflater.inflate(getListFooterLayoutId(), null, false);
				SKYCheckUtils.checkNotNull(footer, "无法根据布局文件ID,获取ListView 尾布局");
				addListFooter();
			}
			// 设置上拉和下拉事件
			if (getSwipRefreshId() != 0) {
				swipe_container = ButterKnife.findById(view, getSwipRefreshId());
				SKYCheckUtils.checkNotNull(swipe_container, "无法根据布局文件ID,获取ListView的SwipRefresh下载刷新布局");
				SKYCheckUtils.checkNotNull(SKYRefreshListener, " ListView的SwipRefresh 下拉刷新和上拉加载事件没有设置");
				swipe_container.setOnRefreshListener(SKYRefreshListener);// 下载刷新
				listView.setOnScrollListener(this);// 加载更多
			}
			// 设置进度颜色
			if (getSwipeColorResIds() != null) {
				SKYCheckUtils.checkNotNull(swipe_container, "无法根据布局文件ID,获取ListView的SwipRefresh下载刷新布局");
				swipe_container.setColorSchemeResources(getSwipeColorResIds());
			}
			// 添加点击事件
			if (getItemListener() != null) {
				listView.setOnItemClickListener(getItemListener());
			}
			if (getItemLongListener() != null) {
				listView.setOnItemLongClickListener(getItemLongListener());
			}
			// 创建适配器
			SKYListAdapter = SKYListViewMultiLayout == null ? new SKYListAdapter(skyView, getSKYAdapterItem()) : new SKYListAdapter(skyView, SKYListViewMultiLayout);
			SKYCheckUtils.checkNotNull(SKYListAdapter, "适配器创建失败");
			// 设置适配器
			listView.setAdapter(SKYListAdapter);
		}
	}

	private void detachListView() {
		if (SKYListAdapter != null) {
			SKYListAdapter.detach();
			SKYListAdapter = null;
		}
		listView = null;
		header = null;
		footer = null;
		SKYAdapterItem = null;
		SKYListViewMultiLayout = null;
		itemListener = null;
		itemLongListener = null;
		swipe_container = null;
		colorResIds = null;
		SKYRefreshListener = null;
	}

	/**
	 * 列表
	 *
	 * @param view
	 */
	private void createRecyclerView(View view) {
		if (getRecyclerviewId() > 0) {
			recyclerView = ButterKnife.findById(view, getRecyclerviewId());
			SKYCheckUtils.checkNotNull(recyclerView, "无法根据布局文件ID,获取recyclerView");
			SKYCheckUtils.checkNotNull(layoutManager, "LayoutManger不能为空");
			recyclerView.setLayoutManager(layoutManager);
			if (SKYRVAdapter != null) {
				// 扩展适配器
				if (SKYRVAdapter instanceof SKYStickyHeaders) {
					SKYStickyHeaders SKYStickyHeaders = (SKYStickyHeaders) SKYRVAdapter;
					final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(SKYStickyHeaders);
					recyclerView.addItemDecoration(headersDecor);

					if (onHeaderClickListener != null) {
						StickyRecyclerHeadersTouchListener touchListener = new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
						touchListener.setOnHeaderClickListener(onHeaderClickListener);
						recyclerView.addOnItemTouchListener(touchListener);

					}
					SKYRVAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

						@Override public void onChanged() {
							headersDecor.invalidateHeaders();
						}
					});
				}
				recyclerView.setAdapter(SKYRVAdapter);
				if (isHeaderFooter) {
					final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
					SKYCheckUtils.checkNotNull(gridLayoutManager, "LayoutManger，不是GridLayoutManager");
					gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

						@Override public int getSpanSize(int position) {
							return SKYRVAdapter.isHeaderAndFooter(position) ? gridLayoutManager.getSpanCount() : 1;
						}
					});
				}
				// 设置Item增加、移除动画
				recyclerView.setItemAnimator(getItemAnimator());
				// 添加分割线
				if (getItemDecoration() != null) {
					recyclerView.addItemDecoration(getItemDecoration());
				}
				// 优化
				recyclerView.setHasFixedSize(true);
				// 设置上拉和下拉事件
				if (getRecyclerviewSwipRefreshId() != 0) {
					recyclerviewSwipeContainer = ButterKnife.findById(view, getRecyclerviewSwipRefreshId());
					SKYCheckUtils.checkNotNull(recyclerviewSwipeContainer, "无法根据布局文件ID,获取recyclerview的SwipRefresh下载刷新布局");
					SKYCheckUtils.checkNotNull(recyclerviewSKYRefreshListener, " recyclerview的SwipRefresh 下拉刷新和上拉加载事件没有设置");
					recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

						@Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
							super.onScrollStateChanged(recyclerView, newState);
							if (newState == RecyclerView.SCROLL_STATE_IDLE && mLoadMoreIsAtBottom) {
								if (recyclerviewSKYRefreshListener.onScrolledToBottom()) {
									mLoadMoreRequestedItemCount = SKYRVAdapter.getItemCount();
									mLoadMoreIsAtBottom = false;
								}
							}
						}

						@Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
							super.onScrolled(recyclerView, dx, dy);
							if (layoutManager instanceof LinearLayoutManager) {
								int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
								mLoadMoreIsAtBottom = SKYRVAdapter.getItemCount() > mLoadMoreRequestedItemCount && lastVisibleItem + 1 == SKYRVAdapter.getItemCount();
							}
						}
					});// 加载更多
					recyclerviewSwipeContainer.setOnRefreshListener(recyclerviewSKYRefreshListener);// 下载刷新
				} else {
					if (SKYFooterListener != null) {
						recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

							@Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
								super.onScrollStateChanged(recyclerView, newState);
								if (newState == RecyclerView.SCROLL_STATE_IDLE && mLoadMoreIsAtBottom) {
									if (SKYFooterListener.onScrolledToBottom()) {
										mLoadMoreRequestedItemCount = SKYRVAdapter.getItemCount();
										mLoadMoreIsAtBottom = false;
									}
								}
							}

							@Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
								super.onScrolled(recyclerView, dx, dy);
								if (layoutManager instanceof LinearLayoutManager) {
									int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
									mLoadMoreIsAtBottom = SKYRVAdapter.getItemCount() > mLoadMoreRequestedItemCount && lastVisibleItem + 1 == SKYRVAdapter.getItemCount();
								}
							}
						});
					}
				}
			} else {
				SKYCheckUtils.checkNotNull(null, "SKYRVAdapter适配器不能为空");
			}

			// 设置进度颜色
			if (getRecyclerviewColorResIds() != null) {
				SKYCheckUtils.checkNotNull(recyclerviewSwipeContainer, "无法根据布局文件ID,获取recyclerview的SwipRefresh下载刷新布局");
				recyclerviewSwipeContainer.setColorSchemeResources(getRecyclerviewColorResIds());
			}
		}
	}

	private void detachRecyclerView() {
		recyclerView = null;
		if (SKYRVAdapter != null) {
			SKYRVAdapter.clearCache();
			SKYRVAdapter = null;
		}
		onHeaderClickListener = null;
		layoutManager = null;
		itemAnimator = null;
		itemDecoration = null;
		recyclerviewSwipeContainer = null;
		recyclerviewSKYRefreshListener = null;
	}

	/**
	 * 自动加载更多
	 **/
	@Override public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mLoadMoreIsAtBottom) {
			if (SKYRefreshListener.onScrolledToBottom()) {
				mLoadMoreRequestedItemCount = view.getCount();
				mLoadMoreIsAtBottom = false;
			}
		}
	}

	@Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		mLoadMoreIsAtBottom = totalItemCount > mLoadMoreRequestedItemCount && firstVisibleItem + visibleItemCount == totalItemCount;
	}

}
