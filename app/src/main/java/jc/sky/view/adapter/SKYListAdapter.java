package jc.sky.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import butterknife.ButterKnife;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.display.J2WIDisplay;
import jc.sky.view.SKYActivity;
import jc.sky.view.SKYDialogFragment;
import jc.sky.view.SKYFragment;
import jc.sky.view.SKYView;

/**
 * @创建人 sky
 * @创建时间 15/7/13 上午10:26
 * @类描述 列表适配器
 */
public class SKYListAdapter extends BaseAdapter {

	private SKYListAdapter() {}

	/**
	 * 数据
	 */
	private List					mItems;

	/**
	 * View
	 */

	SKYView j2WView;

	/**
	 * 适配器Item
	 */
	private SKYAdapterItem j2WAdapterItem;

	/**
	 * 多布局接口
	 */
	private SKYListViewMultiLayout j2WListViewMultiLayout;

	public SKYListAdapter(SKYView j2WView, SKYAdapterItem j2WAdapterItem) {
		SKYCheckUtils.checkNotNull(j2WView, "View层不存在");
		SKYCheckUtils.checkNotNull(j2WAdapterItem, "ListView Item类不存在");
		this.j2WView = j2WView;
		this.j2WAdapterItem = j2WAdapterItem;
	}

	public SKYListAdapter(SKYView j2WView, SKYListViewMultiLayout j2WListViewMultiLayout) {
		SKYCheckUtils.checkNotNull(j2WView, "View层不存在");
		SKYCheckUtils.checkNotNull(j2WListViewMultiLayout, "ListView 多布局接口不存在");
		this.j2WView = j2WView;
		this.j2WListViewMultiLayout = j2WListViewMultiLayout;
	}

	public void setItems(List items) {
		mItems = items;
		notifyDataSetChanged();
	}

	public void add(int position, Object object) {
		if (object == null || mItems == null || position < 0 || position > mItems.size()) {
			return;
		}
		mItems.add(position, object);
		notifyDataSetChanged();
	}

	public void add(Object object) {
		if (object == null || mItems == null) {
			return;
		}
		mItems.add(object);
		notifyDataSetChanged();
	}

	public void addList(int position, List list) {
		if (list == null || list.size() < 1 || mItems == null || position < 0 || position > mItems.size()) {
			return;
		}
		mItems.addAll(position, list);
		notifyDataSetChanged();
	}

	public void addList(List list) {
		if (list == null || list.size() < 1 || mItems == null) {
			return;
		}
		mItems.addAll(list);
		notifyDataSetChanged();
	}

	public void delete(int position) {
		if (mItems == null || position < 0 || mItems.size() < position) {
			return;
		}
		mItems.remove(position);
		notifyDataSetChanged();
	}

	public void delete(Object object) {
		if (mItems == null || mItems.size() < 1) {
			return;
		}
		mItems.remove(object);
		notifyDataSetChanged();
	}

	public void clear() {
		if (mItems == null) {
			return;
		}
		mItems.clear();
		notifyDataSetChanged();
	}

	public List getItems() {
		return mItems;
	}

	@Override public int getCount() {
		return mItems == null ? 0 : mItems.size();
	}

	@Override public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override public long getItemId(int position) {
		return position;
	}

	@Override public int getViewTypeCount() {
		return j2WListViewMultiLayout == null ? 1 : j2WListViewMultiLayout.getJ2WViewTypeCount();
	}

	@Override public int getItemViewType(int position) {
		return j2WListViewMultiLayout == null ? 0 : j2WListViewMultiLayout.getJ2WViewType(position);
	}

	@Override public View getView(int position, View convertView, ViewGroup parent) {
		SKYAdapterItem item = null;
		if (convertView == null) {
			if (j2WListViewMultiLayout == null) {
				item = createItem(); // 单类型
			} else {
				item = createMultiItem(position);// 多类型
			}
			convertView = LayoutInflater.from(parent.getContext()).inflate(item.getItemLayout(), null, false);
			// 初始化
			ButterKnife.bind(item, convertView);
			// 初始化布局
			item.init(convertView);
			// 设置Tag标记
			convertView.setTag(item);
		}
		// 获取item
		item = item == null ? (SKYAdapterItem) convertView.getTag() : item;
		// 绑定数据
		item.bindData(getItem(position), position, getCount());
		return convertView;
	}

	public SKYView getUI() {
		return j2WView;
	}

	public <V extends SKYFragment> V fragment() {
		return j2WView.fragment();
	}

	public <A extends SKYActivity> A activity() {
		return j2WView.activity();
	}

	public <D extends SKYDialogFragment> D dialogFragment() {
		return j2WView.dialogFragment();
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
	 * 单类型
	 * 
	 * @return
	 */
	private SKYAdapterItem createItem() {
		SKYAdapterItem itemClone = (SKYAdapterItem) this.j2WAdapterItem.clone();
		itemClone.setJ2WView(j2WView);
		return itemClone;
	}

	/**
	 * 多类型
	 * 
	 * @param position
	 * @return
	 */
	private SKYAdapterItem createMultiItem(int position) {
		int type = getItemViewType(position);
		return j2WListViewMultiLayout.getJ2WAdapterItem(type);
	}

	public void detach() {
		if (mItems != null) {
			mItems.clear();
			mItems = null;
		}
		j2WView = null;
		j2WAdapterItem = null;
		j2WListViewMultiLayout = null;
	}
}