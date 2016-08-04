package jc.sky.core.plugin;

import android.os.Bundle;
import android.support.annotation.NonNull;

import jc.sky.view.SKYActivity;
import jc.sky.view.SKYBuilder;

/**
 * @创建人 sky
 * @创建时间 16/1/6
 * @类描述 activity拦截器
 */
public interface SKYActivityInterceptor {

	void build(SKYBuilder initialJ2WBuilder);

	void onCreate(SKYActivity j2WIView, Bundle bundle, Bundle savedInstanceState);

	void onStart(SKYActivity j2WIView);

	void onResume(SKYActivity j2WIView);

	void onPause(SKYActivity j2WIView);

	void onStop(SKYActivity j2WIView);

	void onDestroy(SKYActivity j2WIView);

	void onRestart(SKYActivity j2WIView);

	void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

	SKYActivityInterceptor NONE	= new SKYActivityInterceptor() {

										@Override public void build(SKYBuilder initialJ2WBuilder) {

										}

										@Override public void onCreate(SKYActivity j2WIView, Bundle bundle, Bundle savedInstanceState) {

										}

										@Override public void onStart(SKYActivity j2WIView) {

										}

										@Override public void onResume(SKYActivity j2WIView) {

										}

										@Override public void onPause(SKYActivity j2WIView) {

										}

										@Override public void onStop(SKYActivity j2WIView) {

										}

										@Override public void onDestroy(SKYActivity j2WIView) {

										}

										@Override public void onRestart(SKYActivity j2WIView) {

										}

										@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

										}
									};

}
