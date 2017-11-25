package sky.core.view.sticky.stickyheader;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface SKYStickyHeaders<VH extends RecyclerView.ViewHolder> {

	/**
	 * 
	 * @param position
	 *            参数
	 * @return 返回值
	 */
	long getHeaderId(int position);

	/**
	 * Creates a new ViewHolder for a header. This works the same way
	 * onCreateViewHolder in Recycler.Adapter, ViewHolders can be reused for
	 * different views. This is usually a good place to inflate the layout for
	 * the header.
	 *
	 * @param parent
	 *            参数
	 * @return 返回值
	 */
	VH onCreateHeaderViewHolder(ViewGroup parent);

	/**
	 * Binds an existing ViewHolder to the specified adapter position.
	 *
	 * @param holder
	 *            参数
	 * @param position
	 *            参数
	 */
	void onBindHeaderViewHolder(VH holder, int position);

	/**
	 * @return 返回值
	 */
	int getItemCount();
}
