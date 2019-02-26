package sky.example.fragment;

import androidx.paging.PagedList;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;
import sk.SKFragment;
import sk.SKFragmentBuilder;
import sk.SKHelper;
import sk.livedata.SKViewState;
import sky.example.R;
import sky.example.adapter.OneAdapter;
import sky.example.common.UserPublicBiz;
import sky.example.di.TestID;
import sky.example.http.model.Model;

/**
 * @author sky
 * @version 1.0 on 2019-01-24 9:56 PM
 * @see HelloFragment
 */
public class HelloFragment extends SKFragment<HelloBiz> {

	public static final HelloFragment getInstance() {
		HelloFragment helloFragment = new HelloFragment();
		return helloFragment;
	}

	@BindView(R.id.tv_one) TextView							textView;

	@BindView(R.id.tv_two) TextView							textTwo;

	@BindView(R.id.tv_three) TextView						tvThree;

	@BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout	swipeRefreshLayout;

	@Override protected SKFragmentBuilder build(SKFragmentBuilder skBuilder) {
		skBuilder.layoutId(R.layout.fragment_hello);
		skBuilder.recyclerviewId(R.id.rv_oder, new OneAdapter((action, objects) -> biz().reload()));
		return skBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
//		textTwo.setVisibility(View.GONE);
//		tvThree.setVisibility(View.GONE);

		swipeRefreshLayout.setOnRefreshListener(() -> biz().refresh());

		biz().getListSKData().observe(this, new SKViewObserver<PagedList<List<Model>>>() {

			@Override public void onAction(SKViewState state) {
				super.onAction(state);
				if (state == SKViewState.CLOSE_LOADING) {
					swipeRefreshLayout.setRefreshing(false);
				}
			}

			@Override public void onChanged(@Nullable PagedList<List<Model>> lists) {
				adapter().setItems(lists);

			}
		});

		biz().getItemPositoin().observe(this, (integer) -> adapter().notifyItemChanged(integer));
	}

	@OnClick(R.id.tv_one) public void onViewClicked() {
		// OneActivity.intent();
		// biz().test();

		Model i = SKHelper.moduleBiz(TestID.D).run();

		SKHelper.toast().show("打印" + i);
		TipDialogFragment.getInstance().show(getFragmentManager(), "");
	}

	@OnClick(R.id.tv_two) public void onViewClickedTwo() {
		biz().test();
	}
	@OnClick(R.id.tv_three) public void onViewClickedThree() {
		biz(UserPublicBiz.class).showTip();
	}
}
