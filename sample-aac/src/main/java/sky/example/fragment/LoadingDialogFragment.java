package sky.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import sky.example.R;

public class LoadingDialogFragment extends DialogFragment {

	public static final LoadingDialogFragment getInstance() {
		LoadingDialogFragment loadingdialogfragment = new LoadingDialogFragment();
		Bundle bundle = new Bundle();
		loadingdialogfragment.setArguments(bundle);
		return loadingdialogfragment;
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialogfragment_loading, null);
		getDialog().setCanceledOnTouchOutside(false);
		return view;
	}


}