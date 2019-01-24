package sky.example.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import sk.SKAdapter;
import sk.SKHolder;
import sk.livedata.SKLoadMoreCallBack;
import sky.example.MainBiz;
import sky.example.R;
import sky.example.fragment.HelloBiz;
import sky.example.http.model.Model;

/**
 * @author sky
 * @version 1.0 on 2018-08-03 上午11:56
 * @see OneAdapter
 */
public class OneAdapter extends SKAdapter<Model, SKHolder> {

	public OneAdapter(@NonNull SKLoadMoreCallBack skLoadMoreCallBac) {
		super(new DiffUtil.ItemCallback<Model>() {

			@Override public boolean areItemsTheSame(Model oldItem, Model newItem) {
				return oldItem.getClass() == newItem.getClass();
			}

			@Override public boolean areContentsTheSame(Model oldItem, Model newItem) {
				return oldItem.id.equals(newItem.id);
			}
		}, skLoadMoreCallBac);
	}

	@Override public int layoutID(int position) {
		return R.layout.item_oder;
	}

	@Override public SKHolder newHolder(int viewType, View view, Context context) {
		return new ItemHolder(view);
	}

	public class ItemHolder extends SKHolder<Model> {

		@BindView(R.id.iv_item) ImageView	ivItem;

		@BindView(R.id.tv_txt) TextView		tvTxt;

		public ItemHolder(View itemView) {
			super(itemView);
		}

		@Override public void bindData(Model model, int position) {
			tvTxt.setText(String.valueOf(model.id));
			Glide.with(ivItem.getContext()).load(model.img).into(ivItem);
		}

		@OnClick(R.id.iv_item) public void onViewClicked() {
			// Model model = getItem(getAdapterPosition());
			// model.id ="哈哈哈哈哈哈";
			// notifyItemChanged(getAdapterPosition());
			biz(HelloBiz.class).change(getAdapterPosition());
		}

	}

}
