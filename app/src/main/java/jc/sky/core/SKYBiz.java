package jc.sky.core;

import java.util.Vector;

import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYAppUtil;
import jc.sky.display.SKYIDisplay;
import jc.sky.modules.structure.SKYStructureModel;
import jc.sky.view.SKYActivity;
import retrofit2.Call;

/**
 * @author sky
 * @version 版本
 */
public abstract class SKYBiz<U> implements SKYIBiz {

	private U					u;

	private Class				ui;

	private SKYStructureModel	SKYStructureModel;

	private Vector<Call>		callVector;

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
		if (SKYStructureModel != null && SKYStructureModel.isSupterClass(service)) {
			if (SKYStructureModel.getSKYProxy() == null || SKYStructureModel.getSKYProxy().proxy == null) {
				return SKYHelper.structureHelper().createNullService(service);
			}
			return (C) SKYStructureModel.getSKYProxy().proxy;
		} else if (SKYStructureModel != null && service.equals(SKYStructureModel.getService())) {
			if (SKYStructureModel.getSKYProxy() == null || SKYStructureModel.getSKYProxy().proxy == null) {
				return SKYHelper.structureHelper().createNullService(service);
			}
			return (C) SKYStructureModel.getSKYProxy().proxy;
		} else {
			return SKYHelper.biz(service);
		}
	}

	/**
	 * View层 回调引用
	 *
	 * @return 返回值
	 */
	protected U ui() {
		if (u == null) {
			Class ui = SKYAppUtil.getSuperClassGenricType(this.getClass(), 0);
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
			return SKYHelper.structureHelper().createNullService(clazz);
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
	@Override public void initUI(SKYStructureModel SKYStructureModel) {
		this.SKYStructureModel = SKYStructureModel;
		ui = SKYAppUtil.getSuperClassGenricType(this.getClass(), 0);
		u = (U) SKYHelper.structureHelper().createMainLooper(ui, SKYStructureModel.getView());
		callVector = new Vector<>();
	}

	@Override public void detach() {
		u = null;
		ui = null;
		SKYStructureModel = null;
		httpCancel();
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

	protected void showEmpty() {
		final SKYActivity skyActivity = (SKYActivity) this.SKYStructureModel.getView();

		// 如果是主线程 - 直接执行
		if (!SKYHelper.isMainLooperThread()) { // 主线程
			skyActivity.showEmpty();
			return;
		}
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				skyActivity.showEmpty();
			}
		});
	}

	protected void showContent() {
		final SKYActivity skyActivity = (SKYActivity) this.SKYStructureModel.getView();

		// 如果是主线程 - 直接执行
		if (!SKYHelper.isMainLooperThread()) { // 主线程
			skyActivity.showContent();
			return;
		}
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				skyActivity.showContent();
			}
		});
	}

	protected void showHttpError() {
		final SKYActivity skyActivity = (SKYActivity) this.SKYStructureModel.getView();

		// 如果是主线程 - 直接执行
		if (!SKYHelper.isMainLooperThread()) { // 主线程
			skyActivity.showHttpError();
			return;
		}
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				skyActivity.showHttpError();
			}
		});
	}

	protected void showLoading() {
		final SKYActivity skyActivity = (SKYActivity) this.SKYStructureModel.getView();

		// 如果是主线程 - 直接执行
		if (!SKYHelper.isMainLooperThread()) { // 主线程
			skyActivity.showLoading();
			return;
		}
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				skyActivity.showLoading();
			}
		});
	}

	protected void showBizError() {
		final SKYActivity skyActivity = (SKYActivity) this.SKYStructureModel.getView();

		// 如果是主线程 - 直接执行
		if (!SKYHelper.isMainLooperThread()) { // 主线程
			skyActivity.showBizError();
			return;
		}
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				skyActivity.showBizError();
			}
		});
	}
}
