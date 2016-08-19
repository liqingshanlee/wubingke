package tech.honc.android.apps.soldier.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import java.util.ArrayList;
import retrofit2.Call;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.app.SupportApp;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.ArticleService;
import tech.honc.android.apps.soldier.model.Article;
import tech.honc.android.apps.soldier.ui.viewholder.ArticleViewHolder;

/**
 * 资讯
 * MrJiang
 */
public class InformationFragment extends BaseKeysFragment<Article>
{

  @Bind(R.id.toolbar) Toolbar mToolbar;
  private ArticleService mArticleService;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mArticleService = ApiService.createArticleService();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setUpToolbar();
  }

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_information;
  }

  @Override
  public Call<ArrayList<Article>> paginate(Article sinceItem, Article maxItem, int perPage) {
    return mArticleService.getArtice(maxItem != null && maxItem.id != 0 ? maxItem.id : 0);
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(Article.class, ArticleViewHolder.class);
  }

  @Override public Object getKeyForData(Article item) {
    return item != null && item.id != 0 ? item.id : 0;
  }

  private void setUpToolbar() {
    TextView mTextView = new TextView(getContext());
    mTextView.setText("资讯");
    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
        getResources().getDimensionPixelSize(R.dimen.text_size_18));
    mTextView.setTextColor(SupportApp.color(R.color.white));
    Toolbar.LayoutParams paramsTextView =
        new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
    mToolbar.addView(mTextView, paramsTextView);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
    Article article = (Article) getAdapter().get(position);
    Navigator.startInformationDetailActivity(getActivity(), article.id);
  }
}
