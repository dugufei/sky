package jc.sky.core;

import android.os.Bundle;

import java.util.Vector;

import jc.sky.SKYHelper;
import jc.sky.common.utils.SKYAppUtil;
import jc.sky.core.exception.SKYHttpException;
import jc.sky.core.exception.SKYNotUIPointerException;
import jc.sky.display.SKYIDisplay;
import jc.sky.modules.structure.SKYStructureModel;
import jc.sky.view.SKYActivity;
import retrofit2.Call;

/**
 * @author sky
 * @version 版本
 */
public abstract class SKYBiz<U> implements SKYIBiz, SKYIIntercept, SKYIView {

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
	 * 初始化
	 * 
	 * @param bundle
	 *            初始化
	 */
	protected void initBiz(Bundle bundle) {

	}

	@Override public <T> void notifyRecyclerAdatper(final T t) {
		final SKYIView skyiView = (SKYIView) this.SKYStructureModel.getView();
		if (skyiView == null) {
			return;
		}
		// 如果是主线程 - 直接执行
		if (!SKYHelper.isMainLooperThread()) { // 主线程
			skyiView.notifyReyclerAdapter(t);
			return;
		}
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				if (skyiView == null) {
					return;
				}
				skyiView.notifyReyclerAdapter(t);
			}
		});

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
		initBiz(this.SKYStructureModel.getBundle());
	}

	@Override public void detach() {
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

	@Override public void showEmpty() {
		final SKYIView skyiView = (SKYIView) this.SKYStructureModel.getView();
		if (skyiView == null) {
			return;
		}
		// 如果是主线程 - 直接执行
		if (!SKYHelper.isMainLooperThread()) { // 主线程
			skyiView.showEmpty();
			return;
		}
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				if (skyiView == null) {
					return;
				}
				skyiView.showEmpty();
			}
		});
	}

	@Override public void showContent() {
		final SKYIView skyiView = (SKYIView) this.SKYStructureModel.getView();
		if (skyiView == null) {
			return;
		}
		// 如果是主线程 - 直接执行
		if (!SKYHelper.isMainLooperThread()) { // 主线程
			skyiView.showContent();
			return;
		}
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				if (skyiView == null) {
					return;
				}
				skyiView.showContent();
			}
		});
	}

	@Override public void showHttpError() {
		final SKYIView skyiView = (SKYIView) this.SKYStructureModel.getView();
		if (skyiView == null) {
			return;
		}
		// 如果是主线程 - 直接执行
		if (!SKYHelper.isMainLooperThread()) { // 主线程
			skyiView.showHttpError();
			return;
		}
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				if (skyiView == null) {
					return;
				}
				skyiView.showHttpError();
			}
		});
	}

	@Override public void showLoading() {
		final SKYIView skyiView = (SKYIView) this.SKYStructureModel.getView();
		if (skyiView == null) {
			return;
		}
		// 如果是主线程 - 直接执行
		if (!SKYHelper.isMainLooperThread()) { // 主线程
			skyiView.showLoading();
			return;
		}
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				if (skyiView == null) {
					return;
				}
				skyiView.showLoading();
			}
		});
	}

	@Override public void showBizError() {
		final SKYIView skyiView = (SKYIView) this.SKYStructureModel.getView();
		if (skyiView == null) {
			return;
		}
		// 如果是主线程 - 直接执行
		if (!SKYHelper.isMainLooperThread()) { // 主线程
			skyiView.showBizError();
			return;
		}
		SKYHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				if (skyiView == null) {
					return;
				}
				skyiView.showBizError();
			}
		});
	}

	@Override public boolean interceptBizError(Throwable throwable) {
		return false;
	}

	@Override public boolean interceptHttpError(SKYHttpException sKYHttpException) {
		return false;
	}

	@Override public boolean interceptUIError(SKYNotUIPointerException sKYNotUIPointerException) {
		return false;
	}
}
