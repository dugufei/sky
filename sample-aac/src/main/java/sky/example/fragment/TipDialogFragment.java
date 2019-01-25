package sky.example.fragment;

import android.os.Bundle;

import sk.SKDialogFragment;
import sk.SKDialogFragmentBuilder;
import sky.example.R;

/**
 * @author sky
 * @version 1.0 on 2019-01-24 10:53 PM
 * @see TipDialogFragment
 */
public class TipDialogFragment extends SKDialogFragment<TipBiz> {

	public static final TipDialogFragment getInstance() {
		TipDialogFragment helloFragment = new TipDialogFragment();
		return helloFragment;
	}

	@Override protected int style() {
		return android.R.style.Animation_Dialog;
	}

	@Override protected SKDialogFragmentBuilder build(SKDialogFragmentBuilder skBuilder) {
		skBuilder.layoutId(R.layout.dialogfragment_tip);
		skBuilder.layoutWrapHeight();
		skBuilder.isCancel(true);
		return skBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {

	}
}
