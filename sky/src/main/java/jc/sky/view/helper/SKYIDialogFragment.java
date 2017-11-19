package jc.sky.view.helper;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * @author sky
 * @version 版本
 */
public interface SKYIDialogFragment {

	/**
	 * 显示碎片
	 *
	 * @param fragmentManager
	 *            参数
	 * @return 返回值
	 */
	DialogFragment show(FragmentManager fragmentManager);

	DialogFragment show(FragmentManager fragmentManager, int mRequestCode);

	DialogFragment show(FragmentManager fragmentManager, Fragment mTargetFragment);

	DialogFragment show(FragmentManager fragmentManager, Fragment mTargetFragment, int mRequestCode);

	DialogFragment show(FragmentManager fragmentManager, Activity activity);

	DialogFragment show(FragmentManager fragmentManager, Activity activity, int mRequestCode);

	/**
	 * 显示碎片-不保存activity状态
	 *
	 * @param fragmentManager
	 *            参数
	 * @return 返回值
	 */
	DialogFragment showAllowingStateLoss(FragmentManager fragmentManager);

	DialogFragment showAllowingStateLoss(FragmentManager fragmentManager, int mRequestCode);

	DialogFragment showAllowingStateLoss(FragmentManager fragmentManager, Fragment mTargetFragment);

	DialogFragment showAllowingStateLoss(FragmentManager fragmentManager, Fragment mTargetFragment, int mRequestCode);

	DialogFragment showAllowingStateLoss(FragmentManager fragmentManager, Activity activity);

	DialogFragment showAllowingStateLoss(FragmentManager fragmentManager, Activity activity, int mRequestCode);
}