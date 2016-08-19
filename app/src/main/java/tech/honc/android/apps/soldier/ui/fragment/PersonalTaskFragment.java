package tech.honc.android.apps.soldier.ui.fragment;

import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import retrofit2.Call;
import support.ui.adapters.EasyRecyclerAdapter;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FeedService;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.ui.viewholder.TaskPersonalViewHolder;

/**
 * Created by MrJiang on 2016/4/14.
 * 我的求助
 */
public class PersonalTaskFragment extends BaseKeysFragment<Feed> {

  private FeedService mFeedService;

  public static PersonalTaskFragment create() {
    return new PersonalTaskFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFeedService = ApiService.createFeedService();
  }

  @Override public Call<ArrayList<Feed>> paginate(Feed sinceItem, Feed maxItem, int perPage) {
    return mFeedService.myFeed(maxItem != null && maxItem.id != 0 ? maxItem.id : 0);
  }

  @Override public Object getKeyForData(Feed item) {
    return item != null && item.id != 0 ? item.id : 0;
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(Feed.class, TaskPersonalViewHolder.class);
    adapter.setOnClickListener(this);
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
    final Feed feed = (Feed) getAdapter().get(position);
    Navigator.startPersonalTaskDetailActivity(getActivity(), feed.id);
  }

  @Override public void onResume() {
    super.onResume();
    refresh();
  }
}
