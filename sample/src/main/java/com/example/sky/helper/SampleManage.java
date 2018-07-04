package com.example.sky.helper;

import com.example.sky.helper.modules.API;

import javax.inject.Inject;

import dagger.Lazy;
import sky.core.SKYModulesManage;

/**
 * @author sky
 * @version 1.0 on 2017-11-26 上午1:22
 * @see SampleManage
 */
public class SampleManage extends SKYModulesManage {

	@Inject public Lazy<API> api;

	// public API api() {
	// if (api == null) {
	// synchronized (API.class) {
	// if (api == null) {
	// api = new API();
	// }
	// }
	// }
	// return api;
	// }

}
