package jc.sky.view.embed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.common.utils.SKYKeyboardUtils;
import jc.sky.core.SKYIBiz;
import jc.sky.display.SKYIDisplay;
import jc.sky.modules.structure.SKYStructureModel;

/**
 * @author sky
 * @version 版本
 */
public class SKYEmbedActivity<T extends SKYIBiz> extends AppCompatActivity {

	SKYStructureModel SKYStructureModel;

	/**
	 * 初始化
	 *
	 * @param savedInstanceState
	 *            参数
	 */
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 初始化结构 **/
		SKYStructureModel = new SKYStructureModel(this,getIntent() == null ? null : getIntent().getExtras());
		SKYHelper.structureHelper().attach(SKYStructureModel);
		/** 初始化堆栈 **/
		SKYHelper.screenHelper().onCreate(this);
	}

	@Override protected void onResume() {
		super.onResume();
		SKYHelper.screenHelper().onResume(this);
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		SKYHelper.screenHelper().onActivityResult(this);
	}

	/**
	 * 设置输入法
	 *
	 * @param mode
	 *            参数
	 */
	public void setSoftInputMode(int mode) {
		getWindow().setSoftInputMode(mode);
	}

	@Override protected void onPause() {
		super.onPause();
		SKYHelper.screenHelper().onPause(this);
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		detach();
		/** 移除builder **/
		SKYHelper.structureHelper().detach(SKYStructureModel);
		SKYHelper.screenHelper().onDestroy(this);
		/** 关闭键盘 **/
		SKYKeyboardUtils.hideSoftInput(this);
	}

	/**
	 * 清空
	 */
	protected void detach() {}

	public void setLanding() {
		SKYHelper.screenHelper().setAsLanding(this);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public <D extends SKYIDisplay> D display(Class<D> eClass) {
		return SKYHelper.display(eClass);
	}

	public <C extends SKYIBiz> C biz(Class<C> service) {
		return SKYHelper.biz(service);
	}

	/**********************
	 * View业务代码
	 ********************
	 * @param <T>
	 *            参数
	 * @param clazz
	 *            参数
	 * @return 返回值
	 */

	public <T> T findFragment(Class<T> clazz) {
		SKYCheckUtils.checkNotNull(clazz, "class不能为空");
		return (T) getSupportFragmentManager().findFragmentByTag(clazz.getName());
	}

	@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		SKYHelper.methodsProxy().activityInterceptor().onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
}