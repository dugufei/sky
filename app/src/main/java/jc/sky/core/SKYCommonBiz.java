package jc.sky.core;

import jc.sky.SKYHelper;
import jc.sky.display.SKYIDisplay;

/**
 * @author sky
 * @version 版本
 */
public class SKYCommonBiz implements SKYICommonBiz {

	protected <H> H http(Class<H> hClass) {
		return SKYHelper.http(hClass);
	}

	protected <I> I interfaces(Class<I> inter) {
		return SKYHelper.interfaces(inter);
	}

	protected <D extends SKYIDisplay> D display(Class<D> eClass) {
		return SKYHelper.display(eClass);
	}

	public <C extends SKYIBiz> C biz(Class<C> service) {
		return SKYHelper.biz(service);
	}
}