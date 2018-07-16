package sk;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import sky.SKInput;

import static sk.SKViewState.EMPTY;

/**
 * @author sky
 * @version 1.0 on 2018-04-27 上午11:26
 * @see SKActivity
 */
public abstract class SKActivity<M extends SKViewModel> extends AppCompatActivity {

	private boolean							isFinish;

	private SKBuilder						skBuilder;

	protected M								model;

	@SKInput SKViewModelFactory	skViewModelFactory;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SKInputs.input(this);
		/** 初始化堆栈 **/
		SKHelper.screen().onCreate(this);
		/** 活动拦截器 **/
		SKHelper.interceptor().activityInterceptor().onCreate(this, getIntent().getExtras(), savedInstanceState);
		/** 初始化编辑器 **/
		skBuilder = new SKBuilder(this, getLifecycle());
		/** 初始化view model **/
		initViewModel();
	}

	private void initViewModel() {
		Class clazz = SKCoreUtils.getClassGenricType(this.getClass(), 0);
		model = (M) ViewModelProviders.of(this, skViewModelFactory).get(clazz);
	}

	/**
	 * 定制
	 *
	 * @param skBuilder
	 *            参数
	 * @return 返回值
	 **/
	protected abstract SKBuilder build(SKBuilder skBuilder);

	/**
	 * 初始化数据
	 *
	 * @param savedInstanceState
	 *            数据
	 */
	protected abstract void initData(Bundle savedInstanceState);

	@Override protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		SKHelper.interceptor().activityInterceptor().onPostCreate(this, savedInstanceState);
	}

	@Override protected void onPostResume() {
		super.onPostResume();
		SKHelper.interceptor().activityInterceptor().onPostResume(this);
	}

	@Override protected void onStart() {
		super.onStart();
		SKHelper.interceptor().activityInterceptor().onStart(this);
	}

	@Override protected void onResume() {
		super.onResume();
		SKHelper.screen().onResume(this);
		SKHelper.interceptor().activityInterceptor().onResume(this);
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SKHelper.screen().onActivityResult(this);
		SKHelper.interceptor().activityInterceptor().onActivityResult(requestCode, resultCode, data);
	}

	@Override protected void onPause() {
		super.onPause();
		SKHelper.screen().onPause(this);
		SKHelper.interceptor().activityInterceptor().onPause(this);

		if (isFinishing()) {
			isFinish = true;
			SKHelper.screen().onDestroy(this);
			SKHelper.interceptor().activityInterceptor().onDestroy(this);
			/** 关闭键盘 **/
			hideSoftInput();
		}
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		if (!isFinish) {
			SKHelper.screen().onDestroy(this);
			SKHelper.interceptor().activityInterceptor().onDestroy(this);
			/** 关闭键盘 **/
			hideSoftInput();
		}
	}

	@Override protected void onRestart() {
		super.onRestart();
		SKHelper.interceptor().activityInterceptor().onRestart(this);
	}

	@Override protected void onStop() {
		super.onStop();
		SKHelper.interceptor().activityInterceptor().onStop(this);
	}

	/**
	 * 权限
	 * 
	 * @param requestCode
	 *            参数
	 * @param permissions
	 *            参数
	 * @param grantResults
	 *            参数
	 */
	@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		SKHelper.interceptor().activityInterceptor().onRequestPermissionsResult(requestCode, permissions, grantResults);
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
	 * 隐藏键盘
	 */
	protected void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getWindow().getDecorView().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

	/**
	 * 设置标记
	 */
	public void setLanding() {
		SKHelper.screen().setAsLanding(this);
	}

	/**
	 * @return 返回值
	 */
	protected final RecyclerView.LayoutManager layoutManager() {
		return skBuilder.skRecyclerViewBuilder == null ? null : skBuilder.skRecyclerViewBuilder.layoutManager;
	}

	/**
	 * @return 返回值
	 */
	protected final <R extends RecyclerView> R recyclerView() {
		return skBuilder.skRecyclerViewBuilder == null ? null : (R) skBuilder.skRecyclerViewBuilder.recyclerView;
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

}
