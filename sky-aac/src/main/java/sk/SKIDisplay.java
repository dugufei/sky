package sk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import sky.Impl;

/**
 * @author sky
 * @version 版本
 */
public interface SKIDisplay {

	/**
	 * 获取上下文
	 *
	 * @return 返回值
	 */
	Context context();

	/**
	 * @param <T>
	 *            参数
	 * @return 返回值
	 */
	<T extends FragmentActivity> T activity();

	void start(@NonNull Class clazz);

	/**
	 * 跳转
	 * 
	 * @param clazz
	 *            参数
	 * @param fragment
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentFromFragment(@NonNull Class clazz, @NonNull Fragment fragment, int requestCode);

	/**
	 * 跳转
	 * 
	 * @param intent
	 *            参数
	 * @param fragment
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentFromFragment(@NonNull Intent intent, @NonNull Fragment fragment, int requestCode);

	/**
	 * home键
	 */
	void onKeyHome();

	/**
	 * 
	 */
	void popBackStack();

	/**
	 * @param clazz
	 *            参数
	 */
	void popBackStack(@NonNull Class clazz);

	/**
	 * @param clazzName
	 *            参数
	 */
	void popBackStack(@NonNull String clazzName);

	/**
	 * 
	 */
	void popBackStackAll();

	/**
	 * @param layoutId
	 *            参数
	 * @param fragment
	 *            参数
	 */
	void commitAdd(@IdRes int layoutId, @NonNull Fragment fragment);

	/**
	 * @param srcFragment
	 *            参数
	 * @param layoutId
	 *            参数
	 * @param fragment
	 *            参数
	 */
	void commitChildReplace(@NonNull Fragment srcFragment, @IdRes int layoutId, @NonNull Fragment fragment);

	/**
	 * @param layoutId
	 *            参数
	 * @param fragment
	 *            参数
	 */
	void commitReplace(@IdRes int layoutId, @NonNull Fragment fragment);

	/**
	 * @param layoutId
	 *            参数
	 * @param fragment
	 *            参数
	 */
	void commitBackStack(@IdRes int layoutId, @NonNull Fragment fragment);

	/**
	 * @param layoutId
	 *            参数
	 * @param fragment
	 *            参数
	 * @param animation
	 *            参数
	 */
	void commitBackStack(@IdRes int layoutId, @NonNull Fragment fragment, int animation);

	/**
	 * 跳转intent
	 * 
	 * @param clazz
	 *            参数
	 **/

	void intent(@NonNull Class clazz);

	/**
	 * @param clazzName
	 *            参数
	 */
	void intent(@NonNull String clazzName);

	/**
	 * @param clazz
	 *            参数
	 */
	void intentNotAnimation(@NonNull Class clazz);

	/**
	 * @param clazz
	 *            参数
	 * @param bundle
	 *            参数
	 */
	void intent(@NonNull Class clazz, Bundle bundle);

	/**
	 * @param clazz
	 *            参数
	 * @param bundle
	 *            参数
	 */
	void intentNotAnimation(@NonNull Class clazz, @NonNull Bundle bundle);

	/**
	 * @param intent
	 *            参数
	 */
	void intent(@NonNull Intent intent);

	/**
	 * @param intent
	 *            参数
	 * @param options
	 *            参数
	 */
	void intent(@NonNull Intent intent, @NonNull Bundle options);

	/**
	 * @param clazz
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentForResult(@NonNull Class clazz, int requestCode);

	/**
	 * @param clazz
	 *            参数
	 * @param bundle
	 *            参数
	 * @param requestCode
	 *            参数
	 * @param fragment
	 *            参数
	 */
	void intentForResultFromFragment(@NonNull Class clazz, Bundle bundle, int requestCode, @NonNull Fragment fragment);

	/**
	 * @param clazz
	 *            参数
	 * @param bundle
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentForResult(@NonNull Class clazz, @NonNull Bundle bundle, int requestCode);

	/**
	 * @param intent
	 *            参数
	 * @param requestCod
	 *            参数
	 */
	void intentForResult(@NonNull Intent intent, int requestCod);

	/**
	 * @param intent
	 *            参数
	 * @param options
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentForResult(@NonNull Intent intent, @NonNull Bundle options, int requestCode);

	/**
	 * @param clazz
	 *            参数
	 * @param view
	 *            参数
	 * @param bundle
	 *            参数
	 */
	void intentAnimation(@NonNull Class clazz, @NonNull View view, Bundle bundle);

	/**
	 * @param clazz
	 *            参数
	 * @param in
	 *            参数
	 * @param out
	 *            参数
	 */
	void intentAnimation(@NonNull Class clazz, @AnimRes int in, @AnimRes int out);

	/**
	 * @param clazz
	 *            参数
	 * @param in
	 *            参数
	 * @param out
	 *            参数
	 * @param bundle
	 *            参数
	 */
	void intentAnimation(@NonNull Class clazz, @AnimRes int in, @AnimRes int out, @NonNull Bundle bundle);

	/**
	 * @param clazz
	 *            参数
	 * @param view
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentForResultAnimation(@NonNull Class clazz, @NonNull View view, int requestCode);

	/**
	 * @param clazz
	 *            参数
	 * @param view
	 *            参数
	 * @param bundle
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentForResultAnimation(@NonNull Class clazz, @NonNull View view, @NonNull Bundle bundle, int requestCode);

	/**
	 * @param clazz
	 *            参数
	 * @param in
	 *            参数
	 * @param out
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentForResultAnimation(@NonNull Class clazz, @AnimRes int in, @AnimRes int out, int requestCode);

	/**
	 * @param clazz
	 *            参数
	 * @param in
	 *            参数
	 * @param out
	 *            参数
	 * @param bundle
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentForResultAnimation(@NonNull Class clazz, @AnimRes int in, @AnimRes int out, @NonNull Bundle bundle, int requestCode);

	/**
	 * 自定义动画
	 * 
	 * @param clazz
	 *            参数
	 * @param in
	 *            参数
	 * @param out
	 *            参数
	 */
	void intentCustomAnimation(@NonNull Class clazz, @AnimRes int in, @AnimRes int out);

	void intentCustomAnimation(@NonNull Class clazz, @AnimRes int in, @AnimRes int out, @NonNull Bundle options);

	/**
	 * 动画
	 * 
	 * @param clazz
	 *            参数
	 * @param view
	 *            参数
	 * @param startX
	 *            参数
	 * @param startY
	 *            参数
	 * @param startWidth
	 *            参数
	 * @param startHeight
	 *            参数
	 */
	void intentScaleUpAnimation(@NonNull Class clazz, @NonNull View view, int startX, int startY, int startWidth, int startHeight);

	void intentScaleUpAnimation(@NonNull Class clazz, @NonNull View view, int startX, int startY, int startWidth, int startHeight, @NonNull Bundle options);

	/**
	 * 动画
	 * 
	 * @param clazz
	 *            参数
	 * @param skyDisplayModel
	 *            参数
	 */
	void intentSceneTransitionAnimation(@NonNull Class clazz, SKDisplayModel... skyDisplayModel);

	void intentSceneTransitionAnimation(@NonNull Class clazz, @NonNull Bundle options, SKDisplayModel... skyDisplayModel);

	/**
	 * 动画
	 * 
	 * @param clazz
	 *            参数
	 * @param first
	 *            参数
	 * @param second
	 *            参数
	 */
	void intentSceneTransitionAnimation(@NonNull Class clazz, View first, String second);

	void intentSceneTransitionAnimation(@NonNull Class clazz, View first, String second, @NonNull Bundle options);

	/**
	 * 动画
	 * 
	 * @param clazz
	 *            参数
	 * @param view
	 *            参数
	 * @param startX
	 *            参数
	 * @param startY
	 *            参数
	 * @param width
	 *            参数
	 * @param height
	 *            参数
	 */
	void intentClipRevealAnimation(@NonNull Class clazz, @NonNull View view, int startX, int startY, int width, int height);

	void intentClipRevealAnimation(@NonNull Class clazz, @NonNull View view, int startX, int startY, int width, int height, @NonNull Bundle options);

	/**
	 * 动画
	 * 
	 * @param clazz
	 *            参数
	 * @param view
	 *            参数
	 * @param thumbnail
	 *            参数
	 * @param startX
	 *            参数
	 * @param startY
	 *            参数
	 */
	void intentThumbnailScaleUpAnimation(@NonNull Class clazz, @NonNull View view, @NonNull Bitmap thumbnail, int startX, int startY);

	void intentThumbnailScaleUpAnimation(@NonNull Class clazz, @NonNull View view, @NonNull Bitmap thumbnail, int startX, int startY, @NonNull Bundle options);

}