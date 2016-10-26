package jc.sky.modules.threadpool;

import android.os.Process;
import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * @author sky
 * @version 版本
 */
class SKYThread extends Thread {

	public SKYThread(Runnable runnable, String name) {
		super(runnable, name);
	}

	@Override public void run() {
		// 设置线程优先级-标准后台线程
		Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
		super.run();
	}
}
