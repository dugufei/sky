package com.example.sky.helper;

import com.example.sky.helper.modules.API;

import jc.sky.modules.SKYExtraModulesManage;

/**
 * @author sky
 * @version 1.0 on 2017-11-26 上午1:22
 * @see SampleManage
 */
public class SampleManage extends SKYExtraModulesManage {

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
