package jc.sky.core;

import jc.sky.SKYHelper;
import jc.sky.display.J2WIDisplay;

/**
 * @创建人 sky
 * @创建时间 16/4/13 下午6:02
 * @类描述 公共接口
 */
public class J2WCommonBiz implements J2WICommonBiz {

	protected <H> H http(Class<H> hClass) {
		return SKYHelper.http(hClass);
	}

	protected <I> I impl(Class<I> inter) {
		return SKYHelper.impl(inter);
	}

	protected <D extends J2WIDisplay> D display(Class<D> eClass) {
		return SKYHelper.display(eClass);
	}

	public <C extends J2WIBiz> C biz(Class<C> service) {
		return SKYHelper.biz(service);
	}
}