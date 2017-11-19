package jc.sky.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import jc.sky.R;
import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.common.utils.SKYKeyboardUtils;
import jc.sky.core.SKYIView;
import jc.sky.view.adapter.recycleview.SKYFooterOnScrollListener;
import jc.sky.view.adapter.recycleview.SKYOnScrollListener;
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
public final class SKYBuilder {

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

	private int			layoutStateId;

	private FrameLayout	contentRoot;

	private View		contentRootView;

	int getLayoutId() {
		return layoutId;
	}

	public void layoutId(@LayoutRes int layoutId) {
		this.layoutId = layoutId;
	}

	public void layoutStateId(@IdRes int layoutId) {
		this.layoutStateId = layoutId;
	}

	/**
	 * 显示状态切换
	 */

	int					showState;

	private int			layoutLoadingId;

	private int			layoutEmptyId;

	private int			layoutBizErrorId;

	private int			layoutHttpErrorId;

	private int			layoutBackground;

	private View		layoutContent;

	private View		layoutStateContent;

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

	public void layoutBackground(@ColorRes int color) {
		this.layoutBackground = color;
	}

	// 功能

	View getContentRootView() {
		return contentRootView;
	}

	void layoutContent() {
		if (layoutContent == null) {
			return;
		}
		changeShowAnimation(layoutLoading, false);
		changeShowAnimation(layoutEmpty, false);
		changeShowAnimation(layoutBizError, false);
		changeShowAnimation(layoutHttpError, false);

		if (layoutStateContent != null) {
			changeShowAnimation(layoutStateContent, true);
		} else {
			changeShowAnimation(layoutContent, true);
		}
		showState = SKYIView.STATE_CONTENT;
	}

	void layoutLoading() {
		if (layoutLoadingId < 1) {
			return;
		}
		changeShowAnimation(layoutEmpty, false);
		changeShowAnimation(layoutBizError, false);
		changeShowAnimation(layoutHttpError, false);
		if (layoutStateContent != null) {
			changeShowAnimation(layoutStateContent, false);
		} else {
			changeShowAnimation(layoutContent, false);
		}
		if (layoutLoading == null && vsLoading != null) {
			layoutLoading = vsLoading.inflate();
			SKYCheckUtils.checkNotNull(layoutLoading, "无法根据布局文件ID,获取layoutLoading");
		}
		changeShowAnimation(layoutLoading, true);
		showState = SKYIView.STATE_LOADING;
	}

	void layoutEmpty() {
		if (layoutEmpty == null) {
			return;
		}
		changeShowAnimation(layoutBizError, false);
		changeShowAnimation(layoutHttpError, false);
		if (layoutStateContent != null) {
			changeShowAnimation(layoutStateContent, false);
		} else {
			changeShowAnimation(layoutContent, false);
		}
		changeShowAnimation(layoutLoading, false);
		changeShowAnimation(layoutEmpty, true);
		showState = SKYIView.STATE_EMPTY;
	}

	void layoutBizError() {
		if (layoutBizError == null) {
			return;
		}
		changeShowAnimation(layoutEmpty, false);
		changeShowAnimation(layoutHttpError, false);
		if (layoutStateContent != null) {
			changeShowAnimation(layoutStateContent, false);
		} else {
			changeShowAnimation(layoutContent, false);
		}
		changeShowAnimation(layoutLoading, false);
		changeShowAnimation(layoutBizError, true);
		showState = SKYIView.STATE_BIZ_ERROR;
	}

	void layoutHttpError() {
		if (layoutHttpError == null) {
			return;
		}
		changeShowAnimation(layoutEmpty, false);
		changeShowAnimation(layoutBizError, false);
		if (layoutStateContent != null) {
			changeShowAnimation(layoutStateContent, false);
		} else {
			changeShowAnimation(layoutContent, false);
		}
		changeShowAnimation(layoutLoading, false);
		changeShowAnimation(layoutHttpError, true);
		showState = SKYIView.STATE_HTTP_ERROR;
	}

	int getLayoutState() {
		return showState;
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

	private boolean isOpenSwipBackLayout;

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

	private boolean	fitsSystem					= false;

	private boolean	tint;

	int getTintColor() {
		return tintColor;
	}

	boolean isTintColor() {
		return tintColor > 0;
	}

	public boolean isFitsSystem() {
		return fitsSystem;
	}

	public boolean getStatusBarTintEnabled() {
		return statusBarEnabled;
	}

	public boolean getNavigationBarTintEnabled() {
		return navigationBarTintEnabled;
	}

	public boolean isTint() {
		return tint;
	}

	public void tintIs(boolean isTint) {
		this.tint = isTint;
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

	public void tintFitsSystem(boolean isFitsSystem) {
		this.fitsSystem = isFitsSystem;
	}

	/**
	 * actionbar
	 */

	private Toolbar							toolbar;

	private Toolbar.OnMenuItemClickListener	menuListener;

	private int								toolbarLayoutId	= R.layout.sky_include_toolbar;

	private int								toolbarId		= R.id.toolbar;

	private int								toolbarMenuId;

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
		this.isOpenToolbar = true;
		this.toolbarLayoutId = toolbarLayoutId;
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

	private SwipeRefreshLayout.OnRefreshListener						onRefreshListener;

	private StickyRecyclerHeadersTouchListener.OnHeaderClickListener	onHeaderClickListener;

	private boolean														isHeaderFooter;

	int getRecyclerviewId() {
		return recyclerviewId;
	}

	RecyclerView getRecyclerView() {
		return recyclerView;
	}

	@Nullable SKYRVAdapter getSKYRVAdapterItem() {
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

	@Nullable SwipeRefreshLayout getSwipeContainer() {
		return recyclerviewSwipeContainer;
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

	public void recyclerviewIdLinear(@IdRes int recyclerviewId, @NonNull SKYRVAdapter SKYRVAdapter) {
		this.recyclerviewId = recyclerviewId;
		this.SKYRVAdapter = SKYRVAdapter;
		this.layoutManager = new LinearLayoutManager(skyView.activity(), LinearLayoutManager.VERTICAL, false);
		this.itemDecoration = null;
		this.itemAnimator = new DefaultItemAnimator();
	}

	public void recyclerviewIdGrid(@IdRes int recyclerviewId, int spanCount, @NonNull SKYRVAdapter SKYRVAdapter) {
		this.recyclerviewId = recyclerviewId;
		this.SKYRVAdapter = SKYRVAdapter;
		this.layoutManager = new GridLayoutManager(skyView.activity(), spanCount, GridLayoutManager.VERTICAL, false);
		this.itemDecoration = null;
		this.itemAnimator = new DefaultItemAnimator();
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

	public void recyclerviewSwipRefreshId(@IdRes int recyclerviewSwipRefreshId, @NonNull SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
		this.recyclerviewSwipRefreshId = recyclerviewSwipRefreshId;
		this.onRefreshListener = onRefreshListener;
	}

	public void recyclerRefreshing(boolean bool) {
		if (recyclerviewSwipeContainer != null) {
			recyclerviewSwipeContainer.setRefreshing(bool);
		}
	}

	/**
	 * 创建
	 *
	 * @return
	 */
	View create() {
		/** layout **/
		createLayout();
		/** recyclerview **/
		createRecyclerView(contentRoot);
		/** actoinbar **/
		contentRootView = createActionbar(contentRoot);
		/** background color **/
		if (layoutBackground != 0) {
			contentRootView.setBackgroundResource(layoutBackground);
		}
		return contentRootView;
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
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

		// 内容
		if (getLayoutId() > 0) {
			layoutContent = mInflater.inflate(getLayoutId(), null, false);
			if (layoutStateId > 0) {
				ViewGroup view = layoutContent.findViewById(layoutStateId);
				layoutStateContent = view.getChildAt(0);
				if (layoutStateContent == null) {
					SKYCheckUtils.checkNotNull(layoutContent, "指定切换状态布局后,内容不能为空");
				}
			}
			SKYCheckUtils.checkNotNull(layoutContent, "无法根据布局文件ID,获取layoutContent");
			layoutContent.setFitsSystemWindows(isFitsSystem());
			contentRoot.addView(layoutContent, layoutParams);
		}

		// 进度条
		layoutLoadingId = layoutLoadingId > 0 ? layoutLoadingId : SKYHelper.getComnonView() == null ? 0 : SKYHelper.getComnonView().layoutLoading();
		if (layoutLoadingId > 0) {
			vsLoading = new ViewStub(skyView.activity());
			vsLoading.setLayoutResource(layoutLoadingId);

			if (layoutStateId > 0) {
				ViewGroup view =  layoutContent.findViewById(layoutStateId);
				view.addView(vsLoading, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			} else {
				vsLoading.setFitsSystemWindows(isFitsSystem());
				contentRoot.addView(vsLoading, layoutParams);
			}
		}

		// 空布局
		layoutEmptyId = layoutEmptyId > 0 ? layoutEmptyId : SKYHelper.getComnonView() == null ? 0 : SKYHelper.getComnonView().layoutEmpty();
		if (layoutEmptyId > 0) {
			layoutEmpty = mInflater.inflate(layoutEmptyId, null, false);
			SKYCheckUtils.checkNotNull(layoutEmpty, "无法根据布局文件ID,获取layoutEmpty");

			if (layoutStateId > 0) {
				ViewGroup view = (ViewGroup) layoutContent.findViewById(layoutStateId);
				view.addView(layoutEmpty, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			} else {
				layoutEmpty.setFitsSystemWindows(isFitsSystem());
				contentRoot.addView(layoutEmpty, layoutParams);
			}
			layoutEmpty.setVisibility(View.GONE);
		}

		// 业务错误布局
		layoutBizErrorId = layoutBizErrorId > 0 ? layoutBizErrorId : SKYHelper.getComnonView() == null ? 0 : SKYHelper.getComnonView().layoutBizError();
		if (layoutBizErrorId > 0) {
			layoutBizError = mInflater.inflate(layoutBizErrorId, null, false);
			SKYCheckUtils.checkNotNull(layoutBizError, "无法根据布局文件ID,获取layoutBizError");
			if (layoutStateId > 0) {
				ViewGroup view = (ViewGroup) layoutContent.findViewById(layoutStateId);
				view.addView(layoutBizError, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			} else {
				layoutBizError.setFitsSystemWindows(isFitsSystem());
				contentRoot.addView(layoutBizError, layoutParams);
			}
			layoutBizError.setVisibility(View.GONE);
		}

		// 网络错误布局
		layoutHttpErrorId = layoutHttpErrorId > 0 ? layoutHttpErrorId : SKYHelper.getComnonView() == null ? 0 : SKYHelper.getComnonView().layoutHttpError();
		if (layoutHttpErrorId > 0) {
			SKYCheckUtils.checkArgument(layoutHttpErrorId > 0, "网络错误布局Id不能为空,重写公共布局Application.layoutBizError 或者 在Buider.layout里设置");
			layoutHttpError = mInflater.inflate(layoutHttpErrorId, null, false);
			SKYCheckUtils.checkNotNull(layoutHttpError, "无法根据布局文件ID,获取layoutHttpError");
			if (layoutStateId > 0) {
				ViewGroup view = (ViewGroup) layoutContent.findViewById(layoutStateId);
				view.addView(layoutHttpError, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			} else {
				layoutHttpError.setFitsSystemWindows(isFitsSystem());
				contentRoot.addView(layoutHttpError, layoutParams);
			}
			layoutHttpError.setVisibility(View.GONE);
		}
	}

	private void detachLayout() {
		contentRootView = null;
		contentRoot = null;
		mInflater = null;
		layoutContent = null;
		layoutBizError = null;
		layoutHttpError = null;
		layoutEmpty = null;
		vsLoading = null;
		layoutLoading = null;
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
			// 添加toolbar布局
			mInflater.inflate(getToolbarLayoutId(), toolbarRoot, true);
			// 添加内容布局
			RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			contentLayoutParams.addRule(RelativeLayout.BELOW, R.id.toolbar);
			toolbarRoot.addView(view, contentLayoutParams);
			toolbar = ButterKnife.findById(toolbarRoot, getToolbarId());

			SKYCheckUtils.checkNotNull(toolbar, "无法根据布局文件ID,获取Toolbar");

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
			toolbar = ButterKnife.findById(view, getToolbarId());

			SKYCheckUtils.checkNotNull(toolbar, "无法根据布局文件ID,获取Toolbar");

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

					if (onRefreshListener != null) {
						recyclerviewSwipeContainer.setOnRefreshListener(onRefreshListener);
					} else if (recyclerviewSKYRefreshListener != null) {
						recyclerView.addOnScrollListener(new SKYOnScrollListener(recyclerviewSKYRefreshListener));// 加载更多
						recyclerviewSwipeContainer.setOnRefreshListener(recyclerviewSKYRefreshListener);// 下载刷新
					}
				} else {
					if (SKYFooterListener != null) {
						recyclerView.addOnScrollListener(new SKYFooterOnScrollListener(SKYFooterListener));// 加载更多

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

}
