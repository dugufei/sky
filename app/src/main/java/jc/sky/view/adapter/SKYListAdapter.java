package jc.sky.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import butterknife.ButterKnife;
import jc.sky.common.utils.SKYCheckUtils;
import jc.sky.display.SKYIDisplay;
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

	SKYView SKYView;

	/**
	 * 适配器Item
	 */
	private SKYAdapterItem SKYAdapterItem;

	/**
	 * 多布局接口
	 */
	private SKYListViewMultiLayout SKYListViewMultiLayout;

	public SKYListAdapter(SKYView SKYView, SKYAdapterItem SKYAdapterItem) {
		SKYCheckUtils.checkNotNull(SKYView, "View层不存在");
		SKYCheckUtils.checkNotNull(SKYAdapterItem, "ListView Item类不存在");
		this.SKYView = SKYView;
		this.SKYAdapterItem = SKYAdapterItem;
	}

	public SKYListAdapter(SKYView SKYView, SKYListViewMultiLayout SKYListViewMultiLayout) {
		SKYCheckUtils.checkNotNull(SKYView, "View层不存在");
		SKYCheckUtils.checkNotNull(SKYListViewMultiLayout, "ListView 多布局接口不存在");
		this.SKYView = SKYView;
		this.SKYListViewMultiLayout = SKYListViewMultiLayout;
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
		return SKYListViewMultiLayout == null ? 1 : SKYListViewMultiLayout.getSKYViewTypeCount();
	}

	@Override public int getItemViewType(int position) {
		return SKYListViewMultiLayout == null ? 0 : SKYListViewMultiLayout.getSKYViewType(position);
	}

	@Override public View getView(int position, View convertView, ViewGroup parent) {
		SKYAdapterItem item = null;
		if (convertView == null) {
			if (SKYListViewMultiLayout == null) {
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
		return SKYView;
	}

	public <V extends SKYFragment> V fragment() {
		return SKYView.fragment();
	}

	public <A extends SKYActivity> A activity() {
		return SKYView.activity();
	}

	public <D extends SKYDialogFragment> D dialogFragment() {
		return SKYView.dialogFragment();
	}

	/**
	 * 获取调度
	 *
	 * @param e
	 * @param <E>
	 * @return
	 */
	protected <E extends SKYIDisplay> E display(Class<E> e) {
		return SKYView.display(e);
	}

	/**
	 * 获取fragment
	 *
	 * @param clazz
	 * @return
	 */
	public <T> T findFragment(Class<T> clazz) {
		SKYCheckUtils.checkNotNull(clazz, "class不能为空");
		return (T) SKYView.manager().findFragmentByTag(clazz.getSimpleName());
	}

	/**
	 * 单类型
	 * 
	 * @return
	 */
	private SKYAdapterItem createItem() {
		SKYAdapterItem itemClone = (SKYAdapterItem) this.SKYAdapterItem.clone();
		itemClone.setSKYView(SKYView);
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
		return SKYListViewMultiLayout.getSKYAdapterItem(type);
	}

	public void detach() {
		if (mItems != null) {
			mItems.clear();
			mItems = null;
		}
		SKYView = null;
		SKYAdapterItem = null;
		SKYListViewMultiLayout = null;
	}
}