package jc.sky.modules.screen;

import android.os.Bundle;

/**
 * @author sky
 * @version 版本
 */
public class SKYActivityTransporter {

	private Class<?>	toClazz;

	private Bundle		bundle;

	public SKYActivityTransporter(Class<?> toClazz) {
		this.toClazz = toClazz;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	public Class<?> toClazz() {
		return toClazz;
	}

	public Bundle getBundle() {
		return this.bundle;
	}
}
