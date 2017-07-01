package jc.sky.common.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import jc.sky.SKYHelper;
import jc.sky.modules.log.L;

/**
 * @author sky
 * @version 版本
 */
public final class SKYAppUtil {


	/**
	 * 获取泛型类型
	 * 
	 * @param clazz
	 *            参数
	 * @param index
	 *            参数
	 * @return 返回值
	 */
	public static Class getClassGenricType(final Class clazz, final int index) {
		Type type = clazz.getGenericSuperclass();

		if (!(type instanceof ParameterizedType)) {
			return null;
		}
		// 强制类型转换
		ParameterizedType pType = (ParameterizedType) type;

		Type[] tArgs = pType.getActualTypeArguments();

		if (tArgs.length < 1) {
			return null;
		}

		return (Class) tArgs[index];
	}

	/**
	 * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. 1.因为获取泛型类型-所以增加逻辑判定
	 * 
	 * @param clazz
	 *            参数
	 * @param index
	 *            参数
	 * @return 返回值
	 */
	public static Class<Object> getSuperClassGenricType(final Class clazz, final int index) {

		// 返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
		Type[] genType = clazz.getGenericInterfaces();
		Type[] params = null;
		Type baseType = clazz.getGenericSuperclass();
		// 父类
		if (baseType != null && (baseType instanceof ParameterizedType)) {
			params = ((ParameterizedType) baseType).getActualTypeArguments();
			if (index >= params.length || index < 0) {
				return Object.class;
			}
			if (!(params[index] instanceof Class)) {
				return Object.class;
			}

			return (Class<Object>) params[index];
		}
		// 接口
		if (genType == null || genType.length < 1) {
			Type testType = clazz.getGenericSuperclass();
			if (!(testType instanceof ParameterizedType)) {
				return Object.class;
			}
			// 返回表示此类型实际类型参数的 Type 对象的数组。
			params = ((ParameterizedType) testType).getActualTypeArguments();
		} else {
			if (!(genType[index] instanceof ParameterizedType)) {
				return Object.class;
			}
			// 返回表示此类型实际类型参数的 Type 对象的数组。
			params = ((ParameterizedType) genType[index]).getActualTypeArguments();
		}

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}

		return (Class<Object>) params[index];
	}


	/**
	 * 获取手机屏幕宽高
	 * 
	 * @param activity
	 *            参数
	 * @return 显示器信息实体类
	 */
	public static final DisplayMetrics getWindowsSize(FragmentActivity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	/**
	 * 获取手机宽高
	 * 
	 * @return 返回值
	 */
	public static final DisplayMetrics getWindowsSize() {
		DisplayMetrics dm = SKYHelper.getInstance().getResources().getDisplayMetrics();
		return dm;
	}

	/**
	 * 需要权限 设置手机飞行模式
	 * 
	 * @param context
	 *            参数
	 * @param enabling
	 *            true:设置为飞行模式 false:取消飞行模式
	 */
	public static void setAirplaneModeOn(Context context, boolean enabling) {
		Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, enabling ? 1 : 0);
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", enabling);
		context.sendBroadcast(intent);
	}

	/**
	 * 检查Sim卡
	 * 
	 * @param context
	 *            参数
	 * @return true 无卡 false 有卡
	 */
	public static boolean isSimMode(Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		int state = mTelephonyManager.getSimState();
		switch (state) {
			case TelephonyManager.SIM_STATE_UNKNOWN:
			case TelephonyManager.SIM_STATE_ABSENT:
				return true;
		}
		return false;
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param context
	 *            参数
	 * @return 返回值
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}


	/**
	 * 判断是否运行
	 * 
	 * @param context
	 *            参数
	 * @param packageName
	 *            参数
	 * @return 返回值
	 */
	public static boolean isAppAlive(Context context, String packageName) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
		for (int i = 0; i < processInfos.size(); i++) {
			if (processInfos.get(i).processName.equals(packageName)) {
				L.i("SKYNotificationLaunch", String.format("the %s is running, isAppAlive return true", packageName));
				return true;
			}
		}
		L.i("SKYNotificationLaunch", String.format("the %s is not running, isAppAlive return false", packageName));
		return false;
	}

	/**
	 * 后台唤醒到前台
	 * 
	 * @param context
	 *            参数
	 * @param clazz
	 *            参数
	 * @param bundle
	 *            参数
	 */
	public static void awaken(Context context, Class clazz, Bundle bundle) {
		Intent mainIntent = new Intent(context, clazz);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		mainIntent.setAction(Intent.ACTION_MAIN);
		mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		if (bundle != null) {
			mainIntent.putExtras(bundle);
		}
		context.startActivity(mainIntent);
	}

	/**
	 * 转换
	 * 
	 * @param dp
	 *            dp
	 * @param context
	 *            上下文
	 * @return 结果
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return px;
	}

	/**
	 * 转换
	 * 
	 * @param px
	 *            px
	 * @param context
	 *            上下文
	 * @return 结果
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return dp;
	}
}
