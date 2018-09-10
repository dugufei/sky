package sky.example;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import sk.SKApp;
import sk.SKCommonView;
import sk.SKDI;
import sk.SKDefaultLibrary;
import sk.SKHolder;
import sk.livedata.SKLoadMoreHolder;
import sk.livedata.SKNetworkState;
import sky.SKDIApp;
import sky.example.helper.TextBind;

import static sk.livedata.SKNetworkState.FAILED;
import static sk.livedata.SKNetworkState.RUNNING;


/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:07
 * @see MyApp
 */

@SKDIApp(SKDefaultLibrary.class)
public class MyApp extends SKApp {

	@Override public void onCreate() {
		super.onCreate();
		SKDI.builder().setApplication(this).setISK(new TextBind()).setSKCommonView(new SKCommonView() {

			@Override public int layoutLoading() {
				return R.layout.layout_loading;
			}

			@Override public int layoutEmpty() {
				return R.layout.layout_empty;
			}

			@Override public int layoutError() {
				return R.layout.layout_error;
			}

			@Override public SKLoadMoreHolder adapterLoadMore(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType) {
				View view = layoutInflater.inflate(R.layout.layout_loadmore, viewGroup, false);
				return new LoadMore(view);
			}

			@Override public SKHolder adapterUnknownType(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType) {
				return null;
			}

		}).build();
	}

	public class LoadMore extends SKLoadMoreHolder {

		@BindView(R.id.error_msg) TextView			errorMsg;

		@BindView(R.id.progress_bar) ProgressBar	progressBar;

		@BindView(R.id.retry_button) Button			retry;

		public LoadMore(View itemView) {
			super(itemView);
		}

		@Override public void bindData(SKNetworkState state, int position) {
			progressBar.setVisibility(toVisbility(state == RUNNING));
			retry.setVisibility(toVisbility(state == FAILED));
			errorMsg.setVisibility(toVisbility(state.Message != null));
			errorMsg.setText(state.Message);
		}

		private int toVisbility(boolean is) {
			return is ? View.VISIBLE : View.GONE;
		}

		@OnClick(R.id.retry_button) public void onReload() {
			callBack.onActoin(0);
		}

	}
}
