package jc.sky.view.adapter;

import android.view.View;

import jc.sky.display.SKYIDisplay;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.view.SKYActivity;
import jc.sky.view.SKYDialogFragment;
import jc.sky.view.SKYFragment;
import jc.sky.view.SKYView;

/**
 * @author sky
 * @version 版本
 */
public abstract class SKYAdapterItem<T> implements Cloneable {

	private SKYView SKYView;

	void setSKYView(SKYView SKYView) {
		this.SKYView = SKYView;
	}

	/**
	 * 设置布局
	 * 
	 * @return 布局ID
	 */
	public abstract int getItemLayout();

	/**
	 * 初始化控件
	 * 
	 * @param contentView
	 *            ItemView
	 */
	public abstract void init(View contentView);

	/**
	 * 绑定数据
	 * 
	 * @param t
	 *            数据类型泛型
	 * @param position
	 *            下标
	 * @param count
	 *            数量
	 */
	public abstract void bindData(T t, int position, int count);

	public <V extends SKYFragment> V fragment() {
		return SKYView.fragment();
	}

	public <A extends SKYActivity> A activity() {
		return SKYView.activity();
	}

	public <D extends SKYDialogFragment> D dialogFragment() {
		return SKYView.dialogFragment();
	}

	public SKYView getUI() {
		return SKYView;
	}

	/**
	 * 获取调度
	 *
	 * @param e
	 *            参数
	 * @param <E>
	 *            参数
	 * @return 返回值
	 */
	protected <E extends SKYIDisplay> E display(Class<E> e) {
		return SKYView.display(e);
	}

	/**
	 * 获取fragment
	 *
	 * @param <T> 参数
	 * @param clazz
	 *            参数
	 * @return 返回值
	 */
	public <T> T findFragment(Class<T> clazz) {
		SKYCheckUtils.checkNotNull(clazz, "class不能为空");
		return (T) SKYView.manager().findFragmentByTag(clazz.getSimpleName());
	}

	/**
	 * 克隆
	 * 
	 * @return 返回值
	 */
	@Override protected final Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
