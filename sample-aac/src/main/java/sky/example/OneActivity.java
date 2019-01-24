package sky.example;

import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import sk.SKActivity;
import sk.SKActivityBuilder;
import sk.SKHelper;
import sk.livedata.SKViewState;
import sky.example.adapter.OneAdapter;
import sky.example.http.model.Model;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 下午1:52
 * @see OneActivity
 */
public class OneActivity extends SKActivity<OneBiz> {

	@BindView(R.id.tv_me) TextView		tvMe;

	@BindView(R.id.tv_me_two) TextView	tvMeTwo;

	public static final void intent() {
		SKHelper.display().intent(OneActivity.class);
	}

	@Override protected SKActivityBuilder build(SKActivityBuilder skBuilder) {
		skBuilder.layoutId(R.layout.activity_one);
		skBuilder.recyclerviewId(R.id.rv_oder, new OneAdapter((action, objects) -> biz().reload()));
		return skBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
		// tvMe.setText(user.name + "是个大好人" + string + ":" + skData);
		biz().getSkData().observe(this, user -> tvMe.setText(user.name));
	}

	@OnClick(R.id.tv_me) public void onViewClicked() {
		// biz().change("我是第二页");

		biz(MainBiz.class).change(2);
		biz().getListSKData().observe(this, new SKViewObserver<PagedList<List<Model>>>() {

			@Override public void onAction(SKViewState state) {
				super.onAction(state);
			}

			@Override public void onChanged(@Nullable PagedList<List<Model>> lists) {
				adapter().setItems(lists);
			}
		});
		// model.update();
	}
}
