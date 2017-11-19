package com.example.sky;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.sky.view.SKYBuilder;
import jc.sky.view.SKYDialogFragment;
import jc.sky.view.helper.IDialogListener;

/**
 * @author sky
 * @date Created on 2017-11-19 下午10:43
 * @version 1.0
 * @Description TipDialogFragment - 描述
 */
public class TipDialogFragment extends SKYDialogFragment<TipBiz> {

	public static final TipDialogFragment getInstance() {
		TipDialogFragment tipdialogfragment = new TipDialogFragment();
		Bundle bundle = new Bundle();
		tipdialogfragment.setArguments(bundle);
		return tipdialogfragment;
	}

	@Override protected SKYBuilder build(SKYBuilder initialSKYBuilder) {
		initialSKYBuilder.layoutId(R.layout.dialogfragment_tip);
		return initialSKYBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {

	}

	@Override protected int getSKYStyle() {
		return 0;
	}

	@OnClick({ R.id.button4, R.id.button5 }) public void onViewClicked(View view) {
		ITipDialog tipDialog;
		switch (view.getId()) {
			case R.id.button4:// 开始
				tipDialog = listenersActivity(ITipDialog.class);
				if (tipDialog != null) {
					tipDialog.ok();
				}
				dismissAllowingStateLoss();
				break;
			case R.id.button5: // 结束
				tipDialog = listenersActivity(ITipDialog.class);
				if (tipDialog != null) {
					tipDialog.onCancelled(requestCode());
				}
				dismissAllowingStateLoss();
				break;
		}
	}

	public interface ITipDialog extends IDialogListener {

		void ok();
	}
}