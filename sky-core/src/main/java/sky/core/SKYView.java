package sky.core;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import static sky.core.SKYUtils.checkNotNull;


/**
 * @author sky
 * @version 版本
 */
class SKYView {

	/**
	 * 常量
	 */
	 static final int		STATE_ACTIVITY			= 99999;

	 static final int		STATE_FRAGMENT			= 88888;

	 static final int		STATE_DIALOGFRAGMENT	= 77777;

	 static final int		STATE_NOTVIEW			= 66666;

	/** 类型 **/
	private int					state;

	private SKYActivity mSKYActivity;

	private Context				context;

	private SKYFragment mSKYFragment;

	private SKYDialogFragment mSKYDialogFragment;

	private FragmentManager		fragmentManager;

	/**
	 * 初始化
	 * 
	 * @param mSKYActivity
	 *            参数
	 **/
	 void initUI(SKYActivity mSKYActivity) {
		this.state = STATE_ACTIVITY;
		this.mSKYActivity = mSKYActivity;
		this.context = mSKYActivity;
	}

	 void initUI(SKYFragment mSKYFragment) {
		initUI((SKYActivity) mSKYFragment.getActivity());
		this.state = STATE_FRAGMENT;
		this.mSKYFragment = mSKYFragment;
	}

	 void initUI(SKYDialogFragment mSKYDialogFragment) {
		initUI((SKYActivity) mSKYDialogFragment.getActivity());
		this.state = STATE_DIALOGFRAGMENT;
		this.mSKYDialogFragment = mSKYDialogFragment;
	}

	 void initUI(Context context) {
		this.context = context;
		this.state = STATE_NOTVIEW;
	}

	 Context context() {
		return context;
	}

	 <A extends SKYActivity> A activity() {
		return (A) mSKYActivity;
	}

	 FragmentManager manager() {
		return SKYHelper.screenHelper().getCurrentActivity().getSupportFragmentManager();
	}

	 Object getView() {
		Object obj = null;
		switch (state) {
			case STATE_ACTIVITY:
				obj = mSKYActivity;
				break;
			case STATE_FRAGMENT:
				obj = mSKYFragment;
				break;
			case STATE_DIALOGFRAGMENT:
				obj = mSKYDialogFragment;
				break;
		}
		return obj;
	}

	 int getState() {
		return state;
	}

	 <F extends SKYFragment> F fragment() {
		return (F) mSKYFragment;
	}

	 <D extends SKYDialogFragment> D dialogFragment() {
		return (D) mSKYDialogFragment;
	}

	 <B extends SKYBiz> B biz() {
		B b = null;
		switch (state) {
			case STATE_ACTIVITY:
				b = (B) mSKYActivity.biz();
				break;
			case STATE_FRAGMENT:
				b = (B) mSKYFragment.biz();
				break;
			case STATE_DIALOGFRAGMENT:
				b = (B) mSKYDialogFragment.biz();
				break;
		}
		return b;
	}

	 <B extends SKYBiz> B biz(Class<B> service) {
		B b = null;
		switch (state) {
			case STATE_ACTIVITY:
				b = (B) mSKYActivity.biz(service);
				break;
			case STATE_FRAGMENT:
				b = (B) mSKYFragment.biz(service);
				break;
			case STATE_DIALOGFRAGMENT:
				b = (B) mSKYDialogFragment.biz(service);
				break;
		}
		return b;
	}

	 <E extends SKYIDisplay> E display(Class<E> display) {
		E e = null;
		switch (state) {
			case STATE_ACTIVITY:
				e = (E) mSKYActivity.display(display);
				break;
			case STATE_FRAGMENT:
				e = (E) mSKYFragment.display(display);
				break;
			case STATE_DIALOGFRAGMENT:
				e = (E) mSKYDialogFragment.display(display);
				break;
		}
		return e;
	}

	 Toolbar toolbar(int... types) {
		int type = state;
		if (types.length > 0) {
			type = types[0];
		}
		Toolbar toolbar = null;
		switch (type) {
			case STATE_ACTIVITY:
				toolbar = mSKYActivity.toolbar();
				break;
			case STATE_FRAGMENT:
				toolbar = mSKYFragment.toolbar();
				toolbar = toolbar == null ? mSKYActivity.toolbar() : toolbar;
				break;
			case STATE_DIALOGFRAGMENT:
				toolbar = mSKYDialogFragment.toolbar();
				toolbar = toolbar == null ? mSKYActivity.toolbar() : toolbar;
				break;
		}

		checkNotNull(toolbar, "标题栏没有打开，无法调用");
		return toolbar;
	}

	/**
	 * 消除引用
	 */
	 void detach() {
		this.state = 0;
		this.mSKYActivity = null;
		this.mSKYFragment = null;
		this.mSKYDialogFragment = null;
		this.context = null;
		this.fragmentManager = null;
	}
}