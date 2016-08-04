package jc.sky.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import jc.sky.SKYHelper;
import jc.sky.core.SKYIBiz;
import jc.sky.display.SKYIDisplay;
import jc.sky.common.utils.SKYCheckUtils;

/**
 * @创建人 sky
 * @创建时间 15/8/18 下午11:43
 * @类描述 UI层引用
 */
public class SKYView {

	/**
	 * 常量
	 */
	public static final int		STATE_ACTIVITY			= 99999;

	public static final int		STATE_FRAGMENT			= 88888;

	public static final int		STATE_DIALOGFRAGMENT	= 77777;

	public static final int		STATE_NOTVIEW			= 66666;

	/** 类型 **/
	private int					state;

	private SKYActivity mJ2WActivity;

	private Context				context;

	private SKYFragment mJ2WFragment;

	private SKYDialogFragment mJ2WDialogFragment;

	private FragmentManager		fragmentManager;

	/** 初始化 **/
	public void initUI(SKYActivity mJ2WActivity) {
		this.state = STATE_ACTIVITY;
		this.mJ2WActivity = mJ2WActivity;
		this.context = mJ2WActivity;
	}

	public void initUI(SKYFragment mJ2WFragment) {
		initUI((SKYActivity) mJ2WFragment.getActivity());
		this.state = STATE_FRAGMENT;
		this.mJ2WFragment = mJ2WFragment;
	}

	public void initUI(SKYDialogFragment mJ2WDialogFragment) {
		initUI((SKYActivity) mJ2WDialogFragment.getActivity());
		this.state = STATE_DIALOGFRAGMENT;
		this.mJ2WDialogFragment = mJ2WDialogFragment;
	}

	public void initUI(Context context) {
		this.context = context;
		this.state = STATE_NOTVIEW;
	}

	public Context context() {
		return context;
	}

	public <A extends SKYActivity> A activity() {
		return (A) mJ2WActivity;
	}

	public FragmentManager manager() {
		return SKYHelper.screenHelper().getCurrentActivity().getSupportFragmentManager();
	}

	public Object getView() {
		Object obj = null;
		switch (state) {
			case STATE_ACTIVITY:
				obj = mJ2WActivity;
				break;
			case STATE_FRAGMENT:
				obj = mJ2WFragment;
				break;
			case STATE_DIALOGFRAGMENT:
				obj = mJ2WDialogFragment;
				break;
		}
		return obj;
	}

	public int getState() {
		return state;
	}

	public <F extends SKYFragment> F fragment() {
		return (F) mJ2WFragment;
	}

	public <D extends SKYDialogFragment> D dialogFragment() {
		return (D) mJ2WDialogFragment;
	}

	public <B extends SKYIBiz> B biz() {
		B b = null;
		switch (state) {
			case STATE_ACTIVITY:
				b = (B) mJ2WActivity.biz();
				break;
			case STATE_FRAGMENT:
				b = (B) mJ2WFragment.biz();
				break;
			case STATE_DIALOGFRAGMENT:
				b = (B) mJ2WDialogFragment.biz();
				break;
		}
		return b;
	}

	public <B extends SKYIBiz> B biz(Class<B> service) {
		B b = null;
		switch (state) {
			case STATE_ACTIVITY:
				b = (B) mJ2WActivity.biz(service);
				break;
			case STATE_FRAGMENT:
				b = (B) mJ2WFragment.biz(service);
				break;
			case STATE_DIALOGFRAGMENT:
				b = (B) mJ2WDialogFragment.biz(service);
				break;
		}
		return b;
	}

	public <E extends SKYIDisplay> E display(Class<E> display) {
		E e = null;
		switch (state) {
			case STATE_ACTIVITY:
				e = (E) mJ2WActivity.display(display);
				break;
			case STATE_FRAGMENT:
				e = (E) mJ2WFragment.display(display);
				break;
			case STATE_DIALOGFRAGMENT:
				e = (E) mJ2WDialogFragment.display(display);
				break;
		}
		return e;
	}

	public Toolbar toolbar(int... types) {
		int type = state;
		if (types.length > 0) {
			type = types[0];
		}
		Toolbar toolbar = null;
		switch (type) {
			case STATE_ACTIVITY:
				toolbar = mJ2WActivity.toolbar();
				break;
			case STATE_FRAGMENT:
				toolbar = mJ2WFragment.toolbar();
				toolbar = toolbar == null ? mJ2WActivity.toolbar() : toolbar;
				break;
			case STATE_DIALOGFRAGMENT:
				toolbar = mJ2WDialogFragment.toolbar();
				toolbar = toolbar == null ? mJ2WActivity.toolbar() : toolbar;
				break;
		}

		SKYCheckUtils.checkNotNull(toolbar, "标题栏没有打开，无法调用");
		return toolbar;
	}

	/**
	 * 消除引用
	 */
	public void detach() {
		this.state = 0;
		this.mJ2WActivity = null;
		this.mJ2WFragment = null;
		this.mJ2WDialogFragment = null;
		this.context = null;
		this.fragmentManager = null;
	}
}