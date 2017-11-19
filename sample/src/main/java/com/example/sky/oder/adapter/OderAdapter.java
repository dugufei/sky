package com.example.sky.oder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sky.R;
import com.example.sky.http.model.Model;

import butterknife.BindView;
import butterknife.OnClick;
import jc.sky.view.SKYActivity;
import jc.sky.view.SKYHolder;
import jc.sky.view.SKYRVAdapter;

public class OderAdapter extends SKYRVAdapter<OderAdapter.Model, OderAdapter.ItemHolder> {

	public OderAdapter(SKYActivity SKYActivity) {
		super(SKYActivity);
	}

	@Override public ItemHolder newViewHolder(ViewGroup viewGroup, int type) {
		// 修改布局文件
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_oder, viewGroup, false);
		ItemHolder itemHolder = new ItemHolder(view);
		return itemHolder;
	}

	public class ItemHolder extends SKYHolder<Model> {

		@BindView(R.id.iv_item) ImageView	ivItem;

		@BindView(R.id.tv_txt)
		TextView tvTxt;

		public ItemHolder(View itemView) {
			super(itemView);
		}

		@Override public void bindData(Model model, int position) {
			tvTxt.setText(String.valueOf(model.limitModel.resources.core.reset));
			Glide.with(ivItem.getContext()).load(getItem(position).img).into(ivItem);
		}

		@OnClick(R.id.iv_item) public void onViewClicked() {}

	}

	public static class Model {

		public String							img;

		public com.example.sky.http.model.Model	limitModel;

	}
}