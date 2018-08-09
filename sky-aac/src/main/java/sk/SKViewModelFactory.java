package sk;

import android.arch.lifecycle.SKViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import sk.proxy.SKProxy;

import static sk.utils.SKUtils.validateServiceInterface;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午9:16
 * @see SKViewModelFactory
 */
public class SKViewModelFactory extends SKViewModelProvider.NewInstanceFactory {

	@NonNull @Override public <T extends SKViewModel> T create(@NonNull Class<T> modelClass, @NonNull final Class<? extends SKBiz> biz, Bundle bundle) {
		T viewModel;
		try {
			validateServiceInterface(biz);
			/** 加载类 **/
			Constructor c = biz.getDeclaredConstructor();
			c.setAccessible(true);
			/** 创建类 **/
			final Object impl = c.newInstance();
			/** 创建代理类 **/
//			SKProxy proxy = SKHelper.bizStore().createProxy(biz, impl);
			SKProxy proxy = new SKProxy();
			proxy.impl = impl;
			proxy.bizClass = biz;
			/** 创建 ViewModel **/
			viewModel = modelClass.getConstructor(SKProxy.class).newInstance(proxy);
			/** 注入 **/
			SKInputs.inputNotThrow(impl);
			/** 初始化值 **/
			((SKBiz) proxy.impl).initBiz(bundle);
		} catch (InstantiationException e) {
			throw new RuntimeException("Cannot create an instance of " + modelClass, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Cannot create an instance of " + modelClass, e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("No Such method of " + modelClass, e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("No new Instance of " + modelClass, e);
		}

		return viewModel;
	}

}
