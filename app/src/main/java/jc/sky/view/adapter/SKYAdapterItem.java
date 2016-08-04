package jc.sky.view.adapter;

import android.view.View;

import jc.sky.display.J2WIDisplay;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.view.SKYActivity;
import jc.sky.view.SKYDialogFragment;
import jc.sky.view.SKYFragment;
import jc.sky.view.SKYView;

/**
 * Created by sky on 15/2/6. 适配器
 */
public abstract class SKYAdapterItem<T> implements Cloneable {

	private SKYView j2WView;

	void setJ2WView(SKYView j2WView) {
		this.j2WView = j2WView;
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
		return j2WView.fragment();
	}

	public <A extends SKYActivity> A activity() {
		return j2WView.activity();
	}

	public <D extends SKYDialogFragment> D dialogFragment() {
		return j2WView.dialogFragment();
	}

	public SKYView getUI() {
		return j2WView;
	}

	/**
	 * 获取调度
	 *
	 * @param e
	 * @param <E>
	 * @return
	 */
	protected <E extends J2WIDisplay> E display(Class<E> e) {
		return j2WView.display(e);
	}

	/**
	 * 获取fragment
	 *
	 * @param clazz
	 * @return
	 */
	public <T> T findFragment(Class<T> clazz) {
		SKYCheckUtils.checkNotNull(clazz, "class不能为空");
		return (T) j2WView.manager().findFragmentByTag(clazz.getSimpleName());
	}

	/**
	 * 克隆
	 * 
	 * @return
	 * @throws CloneNotSupportedException
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
