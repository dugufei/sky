package com.example.sky.helper;

import sky.core.SKYHelper;

/**
 * @author sky
 * @version 1.0 on 2017-11-26 上午1:24
 * @see SampleHelper
 */
public class SampleHelper extends SKYHelper {

	public static API api() {
		SampleManage sampleManage = getManage();
		return sampleManage.api;
	}
}
