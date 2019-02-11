package sk.screen;

import android.os.Bundle;

/**
 * @author sky
 * @version 版本
 */
public class SKActivityTransporter {

	private Class<?>	toClazz;

	private Bundle		bundle;

	public SKActivityTransporter(Class<?> toClazz) {
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
