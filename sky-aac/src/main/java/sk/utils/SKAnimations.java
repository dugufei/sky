package sk.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

/**
 * @author sky
 * @version 1.0 on 2018-07-12 下午10:57
 * @see SKAnimations 动画
 */
public class SKAnimations {

	public static void changeShowAnimation(@NonNull Context context, @NonNull View view, boolean visible) {
		if (view == null) {
			return;
		}
		Animation anim;
		if (visible) {
			if (view.getVisibility() == View.VISIBLE) {
				return;
			}
			view.setVisibility(View.VISIBLE);
			anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
		} else {
			if (view.getVisibility() == View.GONE) {
				return;
			}
			view.setVisibility(View.GONE);
			anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
		}
		anim.setInterpolator(new LinearInterpolator());
		anim.setDuration(200);
		view.startAnimation(anim);
	}
}
