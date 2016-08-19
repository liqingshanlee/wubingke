package tech.honc.android.apps.soldier.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import java.util.ArrayList;
import retrofit2.Call;
import support.ui.adapters.EasyRecyclerAdapter;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.CollectionService;
import tech.honc.android.apps.soldier.model.Article;
import tech.honc.android.apps.soldier.ui.viewholder.PersonalArticleViewHolder;

/**
 * Created by MrJiang on 2016/5/3.
 * 我的收藏
 */
public class PersonalCollectionFragment extends BaseKeysFragment<Article> {

  private CollectionService mCollectionService;

  public static PersonalCollectionFragment create() {
    return new PersonalCollectionFragment();
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mCollectionService = ApiService.createCollectionService();
  }

  @Override
  public Call<ArrayList<Article>> paginate(Article sinceItem, Article maxItem, int perPage) {
    return mCollectionService.personalAllArticle(
        maxItem != null && maxItem.id != 0 ? maxItem.id : 0);
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(Article.class, PersonalArticleViewHolder.class);
  }

  @Override public Object getKeyForData(Article item) {
    return item != null && item.id != 0 ? item.id : 0;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
    Article article = (Article) getAdapter().get(position);
    Navigator.startInformationDetailActivity(getActivity(), article.articleId );
  }

  @Override public void onResume() {
    super.onResume();
    refresh();
  }
}
