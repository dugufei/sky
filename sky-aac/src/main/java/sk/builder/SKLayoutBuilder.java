package sk.builder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import sk.SKActivity;
import sk.SKHelper;
import sk.utils.SKAnimations;
import sk.utils.SKPreconditions;

import static android.view.Window.ID_ANDROID_CONTENT;

/**
 * @author sky
 * @version 1.0 on 2018-07-12 下午10:59
 * @see SKLayoutBuilder
 */
public class SKLayoutBuilder {

	static final int	STATE_CONTENT	= 1;

	static final int	STATE_LOADING	= 2;

	static final int	STATE_EMPTY		= 3;

	static final int	STATE_ERROR		= 4;

	public ViewGroup	contentRoot;

	int					showState;

	public boolean		fitsSystem		= true;

	public int			layoutId;

	public int			layoutLoadingId;

	public int			layoutEmptyId;

	public int			layoutErrorId;

	public int			layoutStateId;

	public int			layoutBackground;

	public View			layoutContent;

	public ViewGroup	layoutStateContent;

	public ViewStub		vsLoading;

	public SKViewStub	vsViewLoading;

	public ViewStub		vsEmpty;

	public SKViewStub	vsViewEmpty;

	public ViewStub		vsError;

	public SKViewStub	vsViewError;

	public View			layoutLoading;

	public View			layoutEmpty;

	public View			layoutError;

	public void createLayout(SKActivity skActivity, Context context, LayoutInflater mInflater) {
		contentRoot = skActivity.findViewById(ID_ANDROID_CONTENT);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

		// 内容布局
		if (layoutId != 0) {
			layoutContent = mInflater.inflate(layoutId, null, false);
			if (layoutStateId != 0) {
				layoutStateContent = layoutContent.findViewById(layoutStateId);
			}
			layoutContent.setFitsSystemWindows(fitsSystem);
			contentRoot.addView(layoutContent, layoutParams);
			showState = STATE_CONTENT;
		}

		// 进度布局
		layoutLoadingId = layoutLoadingId > 0 ? layoutLoadingId : SKHelper.commonView().layoutLoading();
		if (layoutLoadingId > 0) {
			vsLoading = new ViewStub(context);
			vsLoading.setLayoutResource(layoutLoadingId);

			if (layoutStateContent != null) {
				layoutStateContent.addView(vsLoading, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			} else {
				vsLoading.setFitsSystemWindows(fitsSystem);
				contentRoot.addView(vsLoading, layoutParams);
			}
		}

		// 空布局
		layoutEmptyId = layoutEmptyId > 0 ? layoutEmptyId : SKHelper.commonView().layoutEmpty();
		if (layoutEmptyId > 0) {
			vsEmpty = new ViewStub(context);
			vsEmpty.setLayoutResource(layoutEmptyId);

			if (layoutStateContent != null) {
				layoutStateContent.addView(vsEmpty, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			} else {
				vsEmpty.setFitsSystemWindows(fitsSystem);
				contentRoot.addView(vsEmpty, layoutParams);
			}
		}
		// 错误布局
		layoutErrorId = layoutErrorId > 0 ? layoutErrorId : SKHelper.commonView().layoutError();
		if (layoutErrorId > 0) {
			vsError = new ViewStub(context);
			vsError.setLayoutResource(layoutErrorId);

			if (layoutStateContent != null) {
				layoutStateContent.addView(vsError, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			} else {
				vsError.setFitsSystemWindows(fitsSystem);
				contentRoot.addView(vsError, layoutParams);
			}
		}

		/** background color **/
		if (layoutBackground != 0) {
			contentRoot.setBackgroundResource(layoutBackground);
		}
	}

	public void detachLayout() {
		contentRoot = null;
		layoutStateContent = null;
		layoutContent = null;
		layoutError = null;
		layoutEmpty = null;
		vsLoading = null;
		layoutLoading = null;
	}

	public void layoutContent() {
		if (showState == STATE_CONTENT) {
			return;
		}
		SKAnimations.changeShowAnimation(contentRoot.getContext(), getSelectView(showState), false);

		SKAnimations.changeShowAnimation(contentRoot.getContext(), layoutContent, true);

		showState = STATE_CONTENT;
	}

	public void layoutLoading() {
		if (showState == STATE_LOADING) {
			return;
		}

		if (layoutLoading == null && vsLoading != null) {
			layoutLoading = vsLoading.inflate();
			if(vsViewLoading != null){
				vsViewLoading.init(layoutLoading);
			}
			SKPreconditions.checkNotNull(layoutLoading, "无法根据布局文件ID,获取layoutLoading");
		}

		if (layoutLoading == null) {
			return;
		}

		SKAnimations.changeShowAnimation(contentRoot.getContext(), getSelectView(showState), false);

		SKAnimations.changeShowAnimation(contentRoot.getContext(), layoutLoading, true);
		showState = STATE_LOADING;
	}

	public void layoutEmpty() {
		if (showState == STATE_EMPTY) {
			return;
		}

		if (layoutEmpty == null && vsEmpty != null) {
			layoutEmpty = vsEmpty.inflate();
			if(vsViewEmpty != null){
				vsViewEmpty.init(layoutEmpty);
			}
			SKPreconditions.checkNotNull(layoutEmpty, "无法根据布局文件ID,获取layoutLoading");
		}

		if (layoutEmpty == null) {
			return;
		}

		SKAnimations.changeShowAnimation(contentRoot.getContext(), getSelectView(showState), false);

		SKAnimations.changeShowAnimation(contentRoot.getContext(), layoutEmpty, true);
		showState = STATE_EMPTY;
	}

	public void layoutError() {
		if (showState == STATE_ERROR) {
			return;
		}

		if (layoutError == null && vsError != null) {
			layoutError = vsError.inflate();
			if(vsViewError != null){
				vsViewError.init(layoutError);
			}
			SKPreconditions.checkNotNull(layoutError, "无法根据布局文件ID,获取layoutLoading");
		}

		if (layoutError == null) {
			return;
		}

		SKAnimations.changeShowAnimation(contentRoot.getContext(), getSelectView(showState), false);

		SKAnimations.changeShowAnimation(contentRoot.getContext(), layoutError, true);

		showState = STATE_ERROR;
	}

	private View getSelectView(int state) {
		View view = null;
		switch (state) {
			case STATE_CONTENT:
				view = layoutContent;
				break;
			case STATE_LOADING:
				view = layoutLoading;
				break;
			case STATE_EMPTY:
				view = layoutEmpty;
				break;
			case STATE_ERROR:
				view = layoutError;
				break;
		}

		return view;
	}
}
