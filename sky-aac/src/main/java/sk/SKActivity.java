package sk;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import sk.livedata.SKObserver;
import sky.SKInput;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 上午11:26
 * @see SKActivity
 */
public abstract class SKActivity<B extends SKBiz> extends AppCompatActivity {

	private SKActivityBuilder	skBuilder;

	B							biz;

	@SKInput SKViewModelFactory	skViewModelFactory;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 初始化编辑器 **/
		skBuilder = new SKActivityBuilder(this, getLifecycle(), savedInstanceState);
	}

	/**
	 * 定制
	 *
	 * @param skBuilder
	 *            参数
	 * @return 返回值
	 **/
	protected abstract SKActivityBuilder build(SKActivityBuilder skBuilder);

	/**
	 * 初始化数据
	 *
	 * @param savedInstanceState
	 *            数据
	 */
	protected abstract void initData(Bundle savedInstanceState);

	@Override protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		skBuilder.onPostCreate(savedInstanceState);
	}

	@Override protected void onPostResume() {
		super.onPostResume();
		skBuilder.onPostResume();
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		skBuilder.onActivityResult(requestCode, resultCode, data);
	}

	@Override protected void onRestart() {
		super.onRestart();
		skBuilder.onRestart();
	}

	@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		skBuilder.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	/**
	 * 返回按钮
	 *
	 * @param keyCode
	 * @param event
	 * @return
	 */
	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return onKeyBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 进度条
	 */
	public void loading() {
		SKHelper.interceptor().activityInterceptor().onShowLoading(this);
	}

	/**
	 * 关闭进度条
	 */
	public void closeLoading() {
		SKHelper.interceptor().activityInterceptor().onCloseLoading(this);
	}

	/**
	 * 延迟显示键盘
	 *
	 * @param et
	 */
	protected void showSoftInputDelay(final EditText et) {
		et.postDelayed(new Runnable() {

			@Override public void run() {
				if (et == null) return;
				et.requestFocus();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(et, InputMethodManager.RESULT_UNCHANGED_SHOWN);
			}
		}, 300);
	}

	/**
	 * 返回按钮
	 * 
	 * @return
	 */
	public boolean onKeyBack() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			finishAfterTransition();
		} else {
			finish();
		}
		return true;
	}

	public B biz() {
		return biz;
	}

	public <PB extends SKBiz> PB biz(Class<PB> bizClass) {
		return SKHelper.bizStore().biz(bizClass);
	}

	/**
	 * 设置标记
	 */
	public void setLanding() {
		SKHelper.screen().setAsLanding(this);
	}

	protected final RecyclerView.LayoutManager layoutManager() {
		return skBuilder.skRecyclerViewBuilder == null ? null : skBuilder.skRecyclerViewBuilder.layoutManager;
	}

	protected final <R extends RecyclerView> R recyclerView() {
		return skBuilder.skRecyclerViewBuilder == null ? null : (R) skBuilder.skRecyclerViewBuilder.recyclerView;
	}

	protected <T extends SKViewModel> T find(Class<T> modelClazz) {
		return SKHelper.find(modelClazz);
	}

	public void showContent() {
		SKHelper.interceptor().layoutInterceptor().showContent(this);
		skBuilder.skLayoutBuilder.layoutContent();
	}

	public void showLoading() {
		SKHelper.interceptor().layoutInterceptor().showLoading(this);
		skBuilder.skLayoutBuilder.layoutLoading();
	}

	public void showError() {
		SKHelper.interceptor().layoutInterceptor().showError(this);
		skBuilder.skLayoutBuilder.layoutError();
	}

	public void showEmpty() {
		SKHelper.interceptor().layoutInterceptor().showEmpty(this);
		skBuilder.skLayoutBuilder.layoutEmpty();
	}

	public abstract class SKViewObserver<T> implements SKObserver<T> {

		@Override public void onAction(int state, Object... args) {
			if (args != null) {
				if (args.length > 0 && args[0] instanceof Boolean) {
					if (((Boolean) args[0])) {
						closeLoading();
					}
				}
			}
			switch (state) {
				case 1000:
					showContent();
					break;
				case 2000:
					showEmpty();
					break;
				case 3000:
					showLoading();
					break;
				case 4000:
					showError();
					break;
				case 5000:
					loading();
					break;
				case 6000:
					closeLoading();
					break;
			}
		}
	}
}
