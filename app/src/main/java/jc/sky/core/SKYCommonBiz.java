package jc.sky.core;

import jc.sky.SKYHelper;
import jc.sky.display.SKYIDisplay;

/**
 * @创建人 sky
 * @创建时间 16/4/13 下午6:02
 * @类描述 公共接口
 */
public class SKYCommonBiz implements SKYICommonBiz {

	protected <H> H http(Class<H> hClass) {
		return SKYHelper.http(hClass);
	}

	protected <I> I impl(Class<I> inter) {
		return SKYHelper.impl(inter);
	}

	protected <D extends SKYIDisplay> D display(Class<D> eClass) {
		return SKYHelper.display(eClass);
	}

	public <C extends SKYIBiz> C biz(Class<C> service) {
		return SKYHelper.biz(service);
	}
}