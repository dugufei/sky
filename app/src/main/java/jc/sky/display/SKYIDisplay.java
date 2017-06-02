package jc.sky.display;

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

import org.jetbrains.annotations.NotNull;

import jc.sky.core.Impl;

/**
 * @author sky
 * @version 版本
 */
@Impl(SKYDisplay.class)
public interface SKYIDisplay {

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
	void intentFromFragment(@NotNull Class clazz, @NotNull Fragment fragment, int requestCode);

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
	void intentFromFragment(@NotNull Intent intent, @NotNull Fragment fragment, int requestCode);

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
	void popBackStack(@NotNull Class clazz);

	/**
	 * @param clazzName
	 *            参数
	 */
	void popBackStack(@NotNull String clazzName);

	/**
	 * 
	 */
	void popBackStackAll();

	/**
	 * @param fragment
	 *            参数
	 */
	void commitAdd(@NotNull Fragment fragment);

	/**
	 * @param layoutId
	 *            参数
	 * @param fragment
	 *            参数
	 */
	void commitAdd(@IdRes int layoutId, @NotNull Fragment fragment);

	/**
	 * @param fragment
	 *            参数
	 */
	void commitReplace(@NotNull Fragment fragment);

	/**
	 * @param srcFragment
	 *            参数
	 * @param layoutId
	 *            参数
	 * @param fragment
	 *            参数
	 */
	void commitChildReplace(@NotNull Fragment srcFragment, @IdRes int layoutId, @NotNull Fragment fragment);

	/**
	 * @param layoutId
	 *            参数
	 * @param fragment
	 *            参数
	 */
	void commitReplace(@IdRes int layoutId, @NotNull Fragment fragment);

	/**
	 * @param fragment
	 *            参数
	 */
	void commitBackStack(@NotNull Fragment fragment);

	/**
	 * @param srcFragment
	 *            参数
	 * @param fragment
	 *            参数
	 */
	void commitHideAndBackStack(@NotNull Fragment srcFragment, @NotNull Fragment fragment);

	/**
	 * @param srcFragment
	 *            参数
	 * @param fragment
	 *            参数
	 */
	void commitDetachAndBackStack(@NotNull Fragment srcFragment, @NotNull Fragment fragment);

	/**
	 * @param layoutId
	 *            参数
	 * @param fragment
	 *            参数
	 */
	void commitBackStack(@IdRes int layoutId, @NotNull Fragment fragment);

	/**
	 * @param layoutId
	 *            参数
	 * @param fragment
	 *            参数
	 * @param animation
	 *            参数
	 */
	void commitBackStack(@IdRes int layoutId, @NotNull Fragment fragment, int animation);

	/**
	 * 跳转intent
	 * 
	 * @param clazz
	 *            参数
	 **/

	void intent(@NotNull Class clazz);

	/**
	 * @param clazzName
	 *            参数
	 */
	void intent(@NotNull String clazzName);

	/**
	 * @param clazz
	 *            参数
	 */
	void intentNotAnimation(@NotNull Class clazz);

	/**
	 * @param clazz
	 *            参数
	 * @param bundle
	 *            参数
	 */
	void intent(@NotNull Class clazz, Bundle bundle);

	/**
	 * @param clazz
	 *            参数
	 * @param bundle
	 *            参数
	 */
	void intentNotAnimation(@NotNull Class clazz, @NotNull Bundle bundle);

	/**
	 * @param intent
	 *            参数
	 */
	void intent(@NotNull Intent intent);

	/**
	 * @param intent
	 *            参数
	 * @param options
	 *            参数
	 */
	void intent(@NotNull Intent intent, @NotNull Bundle options);

	/**
	 * @param clazz
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentForResult(@NotNull Class clazz, int requestCode);

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
	void intentForResultFromFragment(@NotNull Class clazz, Bundle bundle, int requestCode, @NotNull Fragment fragment);

	/**
	 * @param clazz
	 *            参数
	 * @param bundle
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentForResult(@NotNull Class clazz, @NotNull Bundle bundle, int requestCode);

	/**
	 * @param intent
	 *            参数
	 * @param requestCod
	 *            参数
	 */
	void intentForResult(@NotNull Intent intent, int requestCod);

	/**
	 * @param intent
	 *            参数
	 * @param options
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentForResult(@NotNull Intent intent, @NotNull Bundle options, int requestCode);

	/**
	 * @param clazz
	 *            参数
	 * @param view
	 *            参数
	 * @param bundle
	 *            参数
	 */
	void intentAnimation(@NotNull Class clazz, @NotNull View view, Bundle bundle);

	/**
	 * @param clazz
	 *            参数
	 * @param in
	 *            参数
	 * @param out
	 *            参数
	 */
	void intentAnimation(@NotNull Class clazz, @AnimRes int in, @AnimRes int out);

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
	void intentAnimation(@NotNull Class clazz, @AnimRes int in, @AnimRes int out, @NonNull Bundle bundle);

	/**
	 * @param clazz
	 *            参数
	 * @param view
	 *            参数
	 * @param requestCode
	 *            参数
	 */
	void intentForResultAnimation(@NotNull Class clazz, @NotNull View view, int requestCode);

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
	void intentForResultAnimation(@NotNull Class clazz, @NotNull View view, @NotNull Bundle bundle, int requestCode);

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
	void intentForResultAnimation(@NotNull Class clazz, @AnimRes int in, @AnimRes int out, int requestCode);

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
	void intentForResultAnimation(@NotNull Class clazz, @AnimRes int in, @AnimRes int out, @NonNull Bundle bundle, int requestCode);

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
	void intentCustomAnimation(@NotNull Class clazz, @AnimRes int in, @AnimRes int out);

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
	void intentScaleUpAnimation(@NotNull Class clazz, @NotNull View view, int startX, int startY, int startWidth, int startHeight);

	/**
	 * 动画
	 * 
	 * @param clazz
	 *            参数
	 * @param skyDisplayModel
	 *            参数
	 */
	void intentSceneTransitionAnimation(@NotNull Class clazz, SKYDisplayModel... skyDisplayModel);

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
	void intentSceneTransitionAnimation(@NotNull Class clazz, View first, String second);

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
	void intentClipRevealAnimation(@NotNull Class clazz, @NotNull View view, int startX, int startY, int width, int height);

	/**
	 * 动画
	 * 
	 * @param clazz
	 *            参数
	 * @param thumbnail
	 *            参数
	 * @param startX
	 *            参数
	 * @param startY
	 *            参数
	 */
	void intentThumbnailScaleUpAnimation(@NotNull Class clazz,@NotNull View view, @NotNull Bitmap thumbnail, int startX, int startY);

}