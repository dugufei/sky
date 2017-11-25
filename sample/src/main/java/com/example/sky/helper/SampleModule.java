package com.example.sky.helper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sky.core.SKYModule;

/**
 * @author sky
 * @version 1.0 on 2017-11-26 上午1:21
 * @see SampleModule
 */
@Module(includes = SKYModule.class)
public class SampleModule {

	@Provides @Singleton public API provideAPI() {
		return new API();
	}

}
