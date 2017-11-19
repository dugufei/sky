package jc.sky.display;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import jc.sky.R;
import jc.sky.core.SKYHelper;
import jc.sky.common.SKYCheckUtils;

/**
 * @author sky
 * @version 版本
 */
public class SKYDisplay implements SKYIDisplay {

	@Override public Context context() {
		return SKYHelper.screenHelper().getCurrentActivity();
	}

	@Override public <T extends FragmentActivity> T activity() {
		T SKYActivity = SKYHelper.screenHelper().getCurrentIsRunningActivity();
		if (SKYActivity != null) {
			return SKYActivity;
		} else {
			return SKYHelper.screenHelper().getCurrentActivity();
		}
	}

	@Override public void intentFromFragment(Class clazz, Fragment fragment, int requestCode) {
		Intent intent = new Intent();
		if (activity() == null) {
			return;
		}
		intent.setClass(activity(), clazz);
		intentFromFragment(intent, fragment, requestCode);
	}

	@Override public void intentFromFragment(Intent intent, Fragment fragment, int requestCode) {
		if (activity() == null) {
			return;
		}
		activity().startActivityFromFragment(fragment, intent, requestCode);
	}

	/** 跳转fragment **/
	@Override public void commitAdd(Fragment fragment) {
		commitAdd(R.id.sky_home, fragment);
	}

	@Override public void commitAdd(int layoutId, Fragment fragment) {
		SKYCheckUtils.checkArgument(layoutId > 0, "布局ID 不能为空~");
		SKYCheckUtils.checkNotNull(fragment, "fragment不能为空~");
		if (activity() == null) {
			return;
		}
		activity().getSupportFragmentManager().beginTransaction().add(layoutId, fragment, fragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commitAllowingStateLoss();
	}

	@Override public void commitReplace(Fragment fragment) {
		commitReplace(R.id.sky_home, fragment);
	}

	@Override public void commitChildReplace(Fragment srcFragment, int layoutId, Fragment fragment) {
		SKYCheckUtils.checkArgument(layoutId > 0, "提交布局ID 不能为空~");
		SKYCheckUtils.checkNotNull(fragment, "fragment不能为空~");
		if (activity() == null) {
			return;
		}
		srcFragment.getChildFragmentManager().beginTransaction().replace(layoutId, fragment, fragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commitAllowingStateLoss();
	}

	@Override public void commitReplace(int layoutId, Fragment fragment) {
		SKYCheckUtils.checkArgument(layoutId > 0, "提交布局ID 不能为空~");
		SKYCheckUtils.checkNotNull(fragment, "fragment不能为空~");
		if (activity() == null) {
			return;
		}
		activity().getSupportFragmentManager().beginTransaction().replace(layoutId, fragment, fragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commitAllowingStateLoss();
	}

	@Override public void commitBackStack(Fragment fragment) {
		commitBackStack(R.id.sky_home, fragment);
	}

	@Override public void commitHideAndBackStack(Fragment srcFragment, Fragment fragment) {
		SKYCheckUtils.checkNotNull(fragment, "fragment不能为空~");
		if (activity() == null) {
			return;
		}
		FragmentTransaction transaction = activity().getSupportFragmentManager().beginTransaction();
		transaction.hide(srcFragment);
		transaction.add(R.id.sky_home, fragment, fragment.getClass().getName()).addToBackStack(fragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commitAllowingStateLoss();
	}

	@Override public void commitDetachAndBackStack(Fragment srcFragment, Fragment fragment) {
		SKYCheckUtils.checkNotNull(fragment, "fragment不能为空~");
		if (activity() == null) {
			return;
		}
		FragmentTransaction transaction = activity().getSupportFragmentManager().beginTransaction();
		transaction.detach(srcFragment);
		transaction.add(R.id.sky_home, fragment, fragment.getClass().getName()).addToBackStack(fragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.commitAllowingStateLoss();
	}

	@Override public void commitBackStack(int layoutId, Fragment fragment) {
		SKYCheckUtils.checkArgument(layoutId > 0, "提交布局ID 不能为空~");
		SKYCheckUtils.checkNotNull(fragment, "fragment不能为空~");
		if (activity() == null) {
			return;
		}
		activity().getSupportFragmentManager().beginTransaction().add(layoutId, fragment, fragment.getClass().getName()).addToBackStack(fragment.getClass().getName())
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commitAllowingStateLoss();

	}

	@Override public void commitBackStack(int layoutId, Fragment fragment, int animation) {
		SKYCheckUtils.checkArgument(layoutId > 0, "提交布局ID 不能为空~");
		SKYCheckUtils.checkArgument(animation > 0, "动画 不能为空~");
		SKYCheckUtils.checkNotNull(fragment, "fragment不能为空~");
		if (activity() == null) {
			return;
		}
		activity().getSupportFragmentManager().beginTransaction().add(layoutId, fragment, fragment.getClass().getName()).addToBackStack(fragment.getClass().getName())
				.setTransition(animation != 0 ? animation : FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commitAllowingStateLoss();
	}

	/** 跳转intent **/
	@Override public void intent(Class clazz) {
		intent(clazz, null);
	}

	@Override public void intent(String clazzName) {
		if (activity() == null || clazzName == null) {
			return;
		}
		Intent intent = new Intent();
		intent.setClassName(SKYHelper.getInstance(), clazzName);
		activity().startActivity(intent);
	}

	@Override public void intentNotAnimation(Class clazz) {
		intentNotAnimation(clazz, null);
	}

	@Override public void intent(Class clazz, Bundle bundle) {
		if (activity() == null) {
			return;
		}
		Intent intent = new Intent();
		intent.setClass(activity(), clazz);
		intent(intent, bundle);
	}

	@Override public void intentNotAnimation(Class clazz, Bundle bundle) {
		if (activity() == null) {
			return;
		}
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		intent.setClass(activity(), clazz);
		intent(intent, bundle);
	}

	@Override public void intent(Intent intent) {
		intent(intent, null);
	}

	@Override public void intent(Intent intent, Bundle options) {
		intentForResult(intent, options, -1);
	}

	@Override public void intentForResult(Class clazz, int requestCode) {
		intentForResult(clazz, null, requestCode);
	}

	@Override public void intentForResultFromFragment(Class clazz, Bundle bundle, int requestCode, Fragment fragment) {
		Intent intent = new Intent();
		intent.setClass(activity(), clazz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		if (activity() == null) {
			return;
		}
		activity().startActivityFromFragment(fragment, intent, requestCode);
	}

	@Override public void intentForResult(Class clazz, Bundle bundle, int requestCode) {
		if (activity() == null) {
			return;
		}
		Intent intent = new Intent();
		intent.setClass(activity(), clazz);
		intentForResult(intent, bundle, requestCode);
	}

	@Override public void intentForResult(Intent intent, int requestCod) {
		intentForResult(intent, null, requestCod);
	}

	/** 根据某个View 位置 启动跳转动画 **/

	@Override public void intentAnimation(Class clazz, View view, Bundle bundle) {
		if (activity() == null) {
			return;
		}
		Intent intent = new Intent();
		intent.setClass(activity(), clazz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		ActivityCompat.startActivity(activity(), intent, ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle());
	}

	@Override public void intentAnimation(Class clazz, int in, int out) {
		intent(clazz);
		if (activity() == null) {
			return;
		}
		activity().overridePendingTransition(in, out);
	}

	@Override public void intentAnimation(Class clazz, int in, int out, Bundle bundle) {
		intent(clazz, bundle);
		if (activity() == null) {
			return;
		}
		activity().overridePendingTransition(in, out);
	}

	@Override public void intentForResultAnimation(Class clazz, View view, int requestCode) {
		intentForResultAnimation(clazz, view, null, requestCode);
	}

	@Override public void intentForResultAnimation(Class clazz, View view, Bundle bundle, int requestCode) {
		if (activity() == null) {
			return;
		}
		Intent intent = new Intent();
		intent.setClass(activity(), clazz);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		ActivityCompat.startActivityForResult(activity(), intent, requestCode, ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight()).toBundle());

	}

	@Override public void intentForResultAnimation(Class clazz, int in, int out, int requestCode) {
		intentForResultAnimation(clazz, in, out, null, requestCode);
	}

	@Override public void intentForResultAnimation(Class clazz, int in, int out, Bundle bundle, int requestCode) {
		intentForResult(clazz, bundle, requestCode);
		if (activity() == null) {
			return;
		}
		activity().overridePendingTransition(in, out);
	}

	@Override public void intentCustomAnimation(@NotNull Class clazz, @AnimRes int in, @AnimRes int out) {
		if (activity() == null) {
			return;
		}
		ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity(), in, out);
		ActivityCompat.startActivity(activity(), new Intent(activity(), clazz), compat.toBundle());

	}

	@Override public void intentCustomAnimation(@NotNull Class clazz, @AnimRes int in, @AnimRes int out, @NotNull Bundle options) {
		if (activity() == null) {
			return;
		}
		ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(activity(), in, out);

		Intent intent = new Intent(activity(), clazz);
		if (options != null) {
			intent.putExtras(options);
		}
		ActivityCompat.startActivity(activity(), intent, compat.toBundle());
	}

	@Override public void intentScaleUpAnimation(@NotNull Class clazz, @NotNull View view, int startX, int startY, int startWidth, int startHeight) {
		if (activity() == null) {
			return;
		}

		ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(view, startX, startY, startWidth, startHeight);
		ActivityCompat.startActivity(activity(), new Intent(activity(), clazz), compat.toBundle());

	}

	@Override public void intentScaleUpAnimation(@NotNull Class clazz, @NotNull View view, int startX, int startY, int startWidth, int startHeight, @NotNull Bundle options) {
		if (activity() == null) {
			return;
		}

		ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(view, startX, startY, startWidth, startHeight);

		Intent intent = new Intent(activity(), clazz);
		if (options != null) {
			intent.putExtras(options);
		}
		ActivityCompat.startActivity(activity(), intent, compat.toBundle());

	}

	@Override public void intentSceneTransitionAnimation(@NotNull Class clazz, SKYDisplayModel... skyDisplayModel) {
		if (activity() == null || skyDisplayModel == null) {
			return;
		}
		Pair<View, String>[] pairs = new Pair[skyDisplayModel.length];
		for (int i = 0; i < skyDisplayModel.length; i++) {
			pairs[i] = new Pair<>(skyDisplayModel[i].first, skyDisplayModel[i].second);
		}

		ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity(), pairs);
		ActivityCompat.startActivity(activity(), new Intent(activity(), clazz), compat.toBundle());

	}

	@Override public void intentSceneTransitionAnimation(@NotNull Class clazz, @NotNull Bundle options, SKYDisplayModel... skyDisplayModel) {
		if (activity() == null || skyDisplayModel == null) {
			return;
		}
		Pair<View, String>[] pairs = new Pair[skyDisplayModel.length];
		for (int i = 0; i < skyDisplayModel.length; i++) {
			pairs[i] = new Pair<>(skyDisplayModel[i].first, skyDisplayModel[i].second);
		}

		ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity(), pairs);
		Intent intent = new Intent(activity(), clazz);
		if (options != null) {
			intent.putExtras(options);
		}
		ActivityCompat.startActivity(activity(), intent, compat.toBundle());
	}

	@Override public void intentSceneTransitionAnimation(@NotNull Class clazz, View first, String second) {
		if (activity() == null) {
			return;
		}
		ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity(), first, second);
		ActivityCompat.startActivity(activity(), new Intent(activity(), clazz), compat.toBundle());

	}

	@Override public void intentSceneTransitionAnimation(@NotNull Class clazz, View first, String second, @NotNull Bundle options) {
		if (activity() == null) {
			return;
		}
		ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity(), first, second);
		Intent intent = new Intent(activity(), clazz);
		if (options != null) {
			intent.putExtras(options);
		}
		ActivityCompat.startActivity(activity(), intent, compat.toBundle());
	}

	@Override public void intentClipRevealAnimation(@NotNull Class clazz, @NotNull View view, int startX, int startY, int width, int height) {
		if (activity() == null) {
			return;
		}
	}

	@Override public void intentClipRevealAnimation(@NotNull Class clazz, @NotNull View view, int startX, int startY, int width, int height, @NotNull Bundle options) {

	}

	@Override public void intentThumbnailScaleUpAnimation(@NotNull Class clazz, @NotNull View view, @NotNull Bitmap thumbnail, int startX, int startY) {
		if (activity() == null) {
			return;
		}

		ActivityOptionsCompat compat = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(view, thumbnail, startX, startY);
		ActivityCompat.startActivity(activity(), new Intent(activity(), clazz), compat.toBundle());
	}

	@Override public void intentThumbnailScaleUpAnimation(@NotNull Class clazz, @NotNull View view, @NotNull Bitmap thumbnail, int startX, int startY, @NotNull Bundle options) {
		if (activity() == null) {
			return;
		}

		ActivityOptionsCompat compat = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(view, thumbnail, startX, startY);
		Intent intent = new Intent(activity(), clazz);
		if (options != null) {
			intent.putExtras(options);
		}
		ActivityCompat.startActivity(activity(), intent, compat.toBundle());
	}

	@Override @TargetApi(Build.VERSION_CODES.JELLY_BEAN) public void intentForResult(Intent intent, Bundle options, int requestCode) {
		SKYCheckUtils.checkNotNull(intent, "intent不能为空～");
		if (options != null) {
			intent.putExtras(options);
		}
		if (activity() == null) {
			return;
		}
		activity().startActivityForResult(intent, requestCode);
	}

	@Override public void onKeyHome() {
		if (activity() == null) {
			return;
		}
		activity().moveTaskToBack(true);
	}

	@Override public void popBackStack() {
		if (activity() == null) {
			return;
		}
		activity().getSupportFragmentManager().popBackStackImmediate();
	}

	@Override public void popBackStack(Class clazz) {
		if (activity() == null) {
			return;
		}
		activity().getSupportFragmentManager().popBackStackImmediate(clazz.getName(), 0);
	}

	@Override public void popBackStack(String clazzName) {
		if (activity() == null) {
			return;
		}
		activity().getSupportFragmentManager().popBackStackImmediate(clazzName, 0);
	}

	@Override public void popBackStackAll() {
		if (activity() == null) {
			return;
		}
		activity().getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

}