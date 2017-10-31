package jc.sky.core;

import jc.sky.SKYHelper;
import jc.sky.core.exception.SKYHttpException;
import jc.sky.core.exception.SKYNotUIPointerException;
import jc.sky.display.SKYIDisplay;
import retrofit2.Call;

/**
 * @author sky
 * @version 版本
 */
public class SKYCommonBiz implements SKYICommonBiz {

	protected <H> H http(Class<H> hClass) {
		return SKYHelper.http(hClass);
	}

	protected <D> D httpBody(Call<D> call) {
		return SKYHelper.httpBody(call);
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

	@Override public boolean interceptHttpError(String method, SKYHttpException sKYHttpException) {
		return false;
	}

	@Override public boolean interceptUIError(String method, SKYNotUIPointerException sKYNotUIPointerException) {
		return false;
	}

	@Override public boolean interceptBizError(String method, Throwable throwable) {
		return false;
	}
}