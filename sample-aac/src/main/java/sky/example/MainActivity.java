package sky.example;

import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import sk.SKActivity;
import sk.SKActivityBuilder;
import sk.builder.SKViewStub;
import sky.example.adapter.OneAdapter;
import sky.example.http.model.Model;

/**
 * @author sky
 * @version 1.0 on 2018-04-28 上午10:07
 * @see MainActivity
 */
public class MainActivity extends SKActivity<MainBiz> {

	@BindView(R.id.tv_one) TextView		textView;

	@BindView(R.id.tv_two) TextView		textTwo;

	@BindView(R.id.tv_three) TextView	tvThree;

	@Override protected SKActivityBuilder build(SKActivityBuilder skBuilder) {
		skBuilder.layoutId(R.layout.activity_main);
		skBuilder.recyclerviewId(R.id.rv_oder, new OneAdapter(null, (actoin, objects) -> {
			biz().reload();
		}));
		skBuilder.layoutErrorViewSub(new Error());
		return skBuilder;
	}

	@Override protected void initData(Bundle savedInstanceState) {
		biz.listSKData.observe(this, new SKViewObserver<PagedList<List<Model>>>() {

			@Override public void onChanged(@Nullable PagedList<List<Model>> lists) {
				adapter().submitList(lists);
			}
		});
		biz.listSKData.observe(this, lists -> adapter().submitList(lists));
	}

	@OnClick(R.id.tv_one) public void onViewClicked() {
		OneActivity.intent();
	}

	public class Error extends SKViewStub {

		@OnClick(R.id.tv_error) public void onError() {
			biz().reload();
		}
	}
}
