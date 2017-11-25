package com.example.sky.helper;

import sky.core.SKYModulesManage;

/**
 * @author sky
 * @version 1.0 on 2017-11-26 上午1:22
 * @see SampleManage
 */
public class SampleManage extends SKYModulesManage {

	private API api;

	public API api() {
		if (api == null) {
			synchronized (API.class) {
				if (api == null) {
					api = new API();
				}
			}
		}
		return api;
	}
}
