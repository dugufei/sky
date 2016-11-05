package jc.sky.modules.screen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author sky
 * @version 版本
 */
public class SKYScreenManager {

	private final int							HONEYCOMB	= 11;

	/**
	 * 该数组保存活动及其运行状态
	 */
	private final ArrayList<SKYScreenHolder>	activities;

	/**
	 * 1.参考在完成当前的一个活动之后再移动另一个活动 不要返回到以前的
	 * 2.用法:在活动一开始setnextstep活动B调用方法和B而不是调用完成的方法，叫前进的方法
	 */
	private SKYActivityTransporter				nextStep;

	public SKYScreenManager() {
		activities = new ArrayList<>();
	}

	/**
	 * 检查当前应用程序是否正在运行或不运行。如果应用程序 在背景-按下按钮或某种方式中断-然后它 假定应用程序不运行
	 * 
	 * @return 返回值
	 */
	public boolean isApplicationRunning() {
		synchronized (activities) {
			for (int i = 0; i < activities.size(); i++)
				if (activities.get(i).isRunning()) return true;
		}
		return false;
	}

	/**
	 * 返回当前活动活动
	 * 
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	public <T extends FragmentActivity> T getCurrentIsRunningActivity() {
		if (activities.size() > 0) {
			synchronized (activities) {
				for (int i = 0; i < activities.size(); i++) {
					if (activities.get(i).isRunning()) {
						return (T) activities.get(i).getActivity();
					}
				}
			}
		}
		return null;
	}

	public <T extends FragmentActivity> T getCurrentActivity() {
		if (activities.size() > 0) {
			return (T) activities.get(activities.size() - 1).getActivity();
		}
		return null;
	}

	/**
	 * 保持参考下一个活动，这是必要的，以目前 活动，而不是整理前一个
	 * 
	 * @param transporter
	 *            参数
	 */
	public void setNextStep(SKYActivityTransporter transporter) {
		nextStep = transporter;
	}

	/**
	 * 从当前活动中开始预定义的临时活动的预定义活动， 如果需要的话，必须完成
	 * 
	 * @param finishThis
	 *            参数
	 */
	public void moveForward(boolean finishThis) {
		if (nextStep != null) {
			Activity current = getCurrentActivity();
			if (current != null) {
				Intent intent = new Intent(current, nextStep.toClazz());
				if (nextStep.getExtras() != null) {
					for (int i = 0; i < nextStep.getExtras().size(); i++) {
						SKYActivityExtra extra = nextStep.getExtras().get(i);
						intent.putExtra(extra.getKey(), extra.getValue());
					}
				}
				getCurrentActivity().startActivity(intent);
				if (finishThis) current.finish();
			}
		}
	}

	/**
	 * 开始
	 * 
	 * @param activity
	 *            参数
	 */
	public void onCreate(FragmentActivity activity) {
		onCreate(activity, false);
	}

	/**
	 * 附加阵列，具有实例的活动，并指定了 活动是登陆一个或不。#这种方法仅仅是保持跟踪 对给定的活动没有影响
	 * 
	 * @param activity
	 *            参数
	 * @param asLanding
	 *            参数
	 */
	public void onCreate(FragmentActivity activity, boolean asLanding) {
		synchronized (activities) {
			activities.add(new SKYScreenHolder(activity, asLanding));
		}
	}

	/**
	 * 开始一系列新的活动
	 * 
	 * @param array
	 *            参数
	 */
	@SuppressLint("NewApi") public void startWithNewArray(Intent[] array) {
		if (Build.VERSION.SDK_INT < HONEYCOMB) {
			Log.e("TheActivityManager", "This method is not supported before Honeycomb (Api Level 11)");
			return;
		}

		toLanding();
		getCurrentActivity().startActivities(array);

		SKYScreenHolder landing = getLanding();
		landing.getActivity().finish();
		synchronized (activities) {
			activities.remove(landing);
		}
	}

	/**
	 * 返回登陆活动的实例
	 * 
	 * @return 返回值
	 */
	public SKYScreenHolder getLanding() {
		synchronized (activities) {
			for (int i = 0; i < activities.size(); i++) {
				if (activities.get(i).isLanding()) return activities.get(i);
			}
		}
		return null;
	}

	/**
	 * 改变活动的登陆属性
	 * 
	 * @param activity
	 *            参数
	 */
	public void setAsLanding(FragmentActivity activity) {
		synchronized (activities) {
			// Clear previous landing, because having more than one landing is
			// not supported
			for (int i = 0; i < activities.size(); i++) {
				if (activities.get(i).isLanding()) {
					activities.get(i).setLanding(false);
					break;
				}
			}

			// Set currently given as landing
			for (int i = 0; i < activities.size(); i++) {
				if (activities.get(i).getActivity().equals(activity)) {
					activities.get(i).setLanding(true);
					break;
				}
			}
		}
	}

	/**
	 * 属性
	 * 
	 * @param className class名称
	 */
	public void setAsLanding(String className) {
		synchronized (activities) {
			for (int i = 0; i < activities.size(); i++) {
				if (activities.get(i).isLanding()) {
					activities.get(i).setLanding(false);
					break;
				}
			}

			// Set currently given as landing
			for (int i = 0; i < activities.size(); i++) {
				if (activities.get(i).getActivity().getClass().getName().equals(className)) {
					activities.get(i).setLanding(true);
					break;
				}
			}
		}
	}

	/**
	 * 确定已经有一个活动
	 * 
	 * @return 返回值
	 */
	public boolean hasLanding() {
		synchronized (activities) {
			for (int i = 0; i < activities.size(); i++) {
				if (activities.get(i).isLanding()) return true;
			}
		}
		return false;
	}

	/**
	 * 改变给定活动的状态，因为它不再运行了。#这 方法只是保持跟踪对给定的活动没有影响
	 * 
	 * @param activity
	 *            参数
	 */
	public void onPause(Activity activity) {
		synchronized (activities) {
			for (int i = activities.size() - 1; i >= 0; i--) {
				if (activities.get(i).getActivity().equals(activity)) {
					activities.get(i).pause();
					break;
				}
			}
		}
	}

	public void onResume(Activity activity) {
		synchronized (activities) {
			for (int i = activities.size() - 1; i >= 0; i--) {
				if (activities.get(i).getActivity().equals(activity)) {
					activities.get(i).resume();
					break;
				}
			}
		}
	}

	public void onActivityResult(Activity activity) {
		synchronized (activities) {
			for (int i = activities.size() - 1; i >= 0; i--) {
				if (activities.get(i).getActivity().equals(activity)) {
					activities.get(i).result();
					break;
				}
			}
		}
	}

	public void onDestroy(Activity activity) {
		synchronized (activities) {
			for (int i = activities.size() - 1; i >= 0; i--) {
				if (activities.get(i).getActivity().equals(activity)) {
					activities.remove(i).removed();
					break;
				}
			}
		}
	}

	/**
	 * 如果给定类的任何实例存在，则结束并将其从 数组。
	 * 
	 * @param clazz
	 *            参数
	 */
	public void finishInstance(Class<?> clazz) {
		synchronized (activities) {
			for (int i = activities.size() - 1; i >= 0; i--) {
				if (clazz.isInstance(activities.get(i).getActivity())) {
					activities.get(i).finish();
					activities.remove(i);
				}
			}
		}
	}

	/**
	 * 出栈不相等的activity
	 *
	 * @param clazz
	 *            参数
	 */
	public void finishNotInstance(Class<?> clazz) {
		synchronized (activities) {
			for (Iterator<SKYScreenHolder> iter = activities.iterator(); iter.hasNext();) {
				SKYScreenHolder item = iter.next();
				if (!clazz.isInstance(item.getActivity())) {
					item.finish();
					iter.remove();
				}
			}
		}
	}

	/**
	 * 结束所有
	 */
	public void finishAll() {
		synchronized (activities) {
			for (int i = activities.size() - 1; i >= 0; i--) {
				activities.get(i).finish();
				activities.remove(i);
			}
		}
	}

	/**
	 * 完成所有的活动，除了着陆
	 */
	public void toLanding() {
		synchronized (activities) {
			for (int i = activities.size() - 1; i >= 0; i--) {
				if (activities.get(i).isLanding()) {
					return;
				} else {
					activities.get(i).finish();
					activities.remove(i);
				}
			}
		}
	}

	/**
	 * 完成所有的活动，直到给定类的实例
	 * 
	 * @param clazz
	 *            参数
	 */
	public void toInstanceOf(Class<?> clazz) {
		synchronized (activities) {
			for (int i = activities.size() - 1; i >= 0; i--) {
				if (clazz.isInstance(activities.get(i).getActivity())) {
					return;
				} else {
					activities.get(i).finish();
					activities.remove(i);
				}
			}
		}
	}

	public <A> A getActivityOf(Class<?> clazz) {
		synchronized (activities) {
			for (int i = activities.size() - 1; i >= 0; i--) {
				if (clazz.isInstance(activities.get(i).getActivity())) {
					return (A) activities.get(i).getActivity();
				}
			}
		}
		return null;
	}
}
