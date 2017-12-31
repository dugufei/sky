package sky.core.modules.error;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import sky.core.L;
import sky.core.SKYHelper;


/**
 * @author sky
 * @version 1.0 on 2016-12-18 下午7:28
 * @see SKYHandler 系统错误处理
 */
public class SKYHandler implements Thread.UncaughtExceptionHandler {

	protected SKYExceptionData mExceptionData;

	@Override public void uncaughtException(Thread thread, Throwable throwable) {


		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		Throwable cause = throwable.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();

		String exceptionType = throwable.getClass().getName();
		String exceptionMsg = writer.toString();
		if (SKYHelper.isLogOpen()) {
			L.i("SKYHandler"+exceptionMsg);
		}
		String throwClassName;
		String throwMethodName;
		int throwLineNumber;

		if (throwable.getStackTrace().length > 0) {
			StackTraceElement trace = throwable.getStackTrace()[0];
			throwClassName = trace.getClassName();
			throwMethodName = trace.getMethodName();
			throwLineNumber = trace.getLineNumber();
		} else {
			throwClassName = "unknown";
			throwMethodName = "unknown";
			throwLineNumber = 0;
		}

		mExceptionData = SKYExceptionData.newInstance().type(exceptionType).msg(exceptionMsg).className(throwClassName).methodName(throwMethodName).lineNumber(throwLineNumber);
	}

	protected void startSkyActivity(Class clazz) {
		Intent intent = new Intent();

		Activity activity = SKYHelper.screenHelper().getCurrentActivity();
		if (activity == null) {
			L.i("当前activity 为空");
			return;
		}
		Bundle bundle = null;
		if (activity.getIntent() != null && activity.getIntent().getExtras() != null) {
			bundle = activity.getIntent().getExtras();
		}

		intent.setClass(SKYHelper.getInstance().getApplicationContext(), clazz);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		if (bundle != null) {
			intent.putExtra(SKYExceptionData.RECOVERY_INTENT, bundle);
		}

		if (mExceptionData != null) {
			intent.putExtra(SKYExceptionData.EXCEPTION_DATA, mExceptionData);
		}

		intent.putParcelableArrayListExtra(SKYExceptionData.RECOVERY_INTENTS,SKYHelper.screenHelper().getIntents());

		activity.startActivity(intent);
		killProcess();
	}

	private void killProcess() {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
	}
}
