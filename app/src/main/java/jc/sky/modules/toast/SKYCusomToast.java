package jc.sky.modules.toast;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import jc.sky.SKYHelper;

/**
 * @author sky
 * @version 版本
 */
public abstract class SKYCusomToast extends SKYToast{

	private View	v;

	/**
	 * 布局ID
	 * 
	 * @return 返回值
	 */
	public abstract int layoutId();

	/**
	 * 
	 * @param view
	 *            参数
	 * @param msg
	 *            参数
	 */
	public abstract void init(View view, String msg);

	/**
	 * 位置
	 * 
	 * @return 返回值 默认 居中
	 */
	public int getGravity() {
		return Gravity.CENTER;
	}

	/**
	 * 设置显示时间
	 * 
	 * @return 返回值
	 */
	public int getDuration() {
		return Toast.LENGTH_SHORT;
	}

	/**
	 * 显示
	 * 
	 * @param msg
	 *            参数
	 */
	public void show(final String msg) {
		// 判断是否在主线程
		boolean isMainLooper = Looper.getMainLooper().getThread() != Thread.currentThread();

		if (isMainLooper) {
			SKYHelper.mainLooper().execute(new Runnable() {

				@Override public void run() {
					cusomShow(msg, Toast.LENGTH_SHORT);
				}
			});
		} else {
			cusomShow(msg, Toast.LENGTH_SHORT);
		}
	}

	/**
	 * 显示
	 * 
	 * @param msg
	 *            参数
	 * @param duration
	 *            参数
	 */
	public void show(final String msg, final int duration) {
		// 判断是否在主线程
		boolean isMainLooper = Looper.getMainLooper().getThread() != Thread.currentThread();

		if (isMainLooper) {
			SKYHelper.mainLooper().execute(new Runnable() {

				@Override public void run() {
					cusomShow(msg, duration);
				}
			});
		} else {
			cusomShow(msg, duration);
		}
	}

	/**
	 * 显示
	 *
	 * @param msg
	 *            参数
	 * @param duration
	 *            参数
	 */
	protected void cusomShow(String msg, int duration) {
		if (mToast == null) {
			mToast = new Toast(SKYHelper.getInstance());
			LayoutInflater inflate = (LayoutInflater) SKYHelper.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflate.inflate(layoutId(), null);
			mToast.setView(v);
			init(v, msg);
			mToast.setDuration(duration);
			mToast.setGravity(getGravity(), 0, 0);
		} else {
			init(v, msg);
			mToast.setDuration(duration);
			mToast.setGravity(getGravity(), 0, 0);
		}
		mToast.show();

	}
}
