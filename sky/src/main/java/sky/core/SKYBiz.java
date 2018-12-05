package sky.core;

import android.os.Bundle;

import java.util.Vector;

import retrofit2.Call;
import sky.core.exception.SKYArgumentException;
import sky.core.exception.SKYHttpException;
import sky.core.exception.SKYNotUIPointerException;
import sky.core.exception.SKYNullPointerException;
import sky.core.interfaces.SKYIView;

/**
 * @author sky
 * @version 版本
 */
public abstract class SKYBiz<U> implements SKYIIntercept {

	private U							u;

	private Class						ui;

	private static SKYStructureModel	SKYStructureModel;

	private Vector<Call>				callVector;

	protected <H> H http(Class<H> hClass) {
		return SKYHelper.http(hClass);
	}

	protected static <D extends SKYIDisplay> D display(Class<D> eClass) {
		return SKYHelper.display(eClass);
	}

	protected static <B extends SKYBiz> B biz(Class<B> service) {
		if (SKYStructureModel != null && SKYStructureModel.isSupterClass(service)) {
			if (SKYStructureModel.getSKYProxy() == null || SKYStructureModel.getSKYProxy().proxy == null) {
				return SKYHelper.structureHelper().createNullService(service);
			}
			return (B) SKYStructureModel.getSKYProxy().proxy;
		} else if (SKYStructureModel != null && service.equals(SKYStructureModel.getService())) {
			if (SKYStructureModel.getSKYProxy() == null || SKYStructureModel.getSKYProxy().proxy == null) {
				return SKYHelper.structureHelper().createNullService(service);
			}
			return (B) SKYStructureModel.getSKYProxy().proxy;
		} else {
			return SKYHelper.biz(service);
		}
	}

	/**
	 * 初始化
	 *
	 * @param bundle
	 *            初始化
	 */
	protected void initBiz(Bundle bundle) {

	}

	/**
	 * View层 回调引用
	 *
	 * @return 返回值
	 */
	protected U ui() {
		if (u == null) {
			if(SKYHelper.isMainLooperThread()){
				throw new SKYArgumentException("is not main looper error");
			}
			Class ui = SKYUtils.getClassGenricType(this.getClass(), 0);
			return (U) SKYHelper.structureHelper().createNullService(ui);
		}
		return u;
	}

	/**
	 * View层 回调
	 *
	 * @param clazz
	 *            参数
	 * @param <V>
	 *            参数
	 * @return 返回值
	 */
	protected <V> V ui(Class<V> clazz) {
		if (clazz.equals(ui)) {
			return (V) ui();
		} else {
			return SKYHelper.ui(clazz);
		}
	}

	/**
	 * @param call
	 *            参数
	 * @param <D>
	 *            参数
	 * @return 返回值
	 */
	protected <D> D httpBody(Call<D> call) {
		callVector.add(call);
		return SKYHelper.httpBody(call);
	}

	/**
	 * View层 是否存在
	 *
	 * @return 返回值
	 */
	public boolean isUI() {
		return u != null;
	}

	/**
	 * @param SKYStructureModel
	 *            参数
	 */
	void initUI(SKYStructureModel SKYStructureModel) {
		this.SKYStructureModel = SKYStructureModel;
		ui = SKYUtils.getClassGenricType(this.getClass(), 0);
		if (ui == null) {
			throw new SKYNullPointerException("view或者biz 没有指定泛型");
		}

		if (!ui.isInterface()) {
			u = (U) SKYHelper.structureHelper().createMainLooperNotIntf(ui, this.SKYStructureModel.getView());
		} else {
			u = (U) SKYHelper.structureHelper().createMainLooper(ui, this.SKYStructureModel.getView());
		}
		callVector = new Vector<>();
	}

	void initBundle() {
		initBiz(this.SKYStructureModel.getBundle());
	}

	protected void detach() {
		u = null;
		ui = null;
		SKYStructureModel = null;
		httpCancel();
	}

	/**
	 * 获取数据
	 *
	 * @return 数据
	 */
	protected Bundle bundle() {
		return this.SKYStructureModel.getBundle();
	}

	/**
	 * 网络取消
	 */
	protected void httpCancel() {
		int count = callVector.size();
		if (count < 1) {
			return;
		}
		for (int i = 0; i < count; i++) {
			Call call = callVector.get(i);
			SKYHelper.httpCancel(call);
		}
		callVector.removeAllElements();
	}

	/**
	 * 进度条
	 */
	public void loading() {
		if (ui == null) {
			return;
		}
		((SKYIView) ui()).loading();
	}

	/**
	 * 关闭进度条
	 */
	public void closeLoading() {
		if (ui == null) {
			return;
		}
		((SKYIView) ui()).closeLoading();
	}

	@Override public boolean interceptHttpError(int interceptor, SKYHttpException sKYHttpException) {
		return false;
	}

	@Override public boolean interceptUIError(int interceptor, SKYNotUIPointerException sKYNotUIPointerException) {
		return false;
	}

	@Override public boolean interceptBizError(int interceptor, Throwable sKYBizException) {
		return false;
	}
}