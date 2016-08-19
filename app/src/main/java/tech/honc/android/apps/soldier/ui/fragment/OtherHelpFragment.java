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
 * Created by Administrator on 2016/5/16.
 */
public class OtherHelpFragment extends BaseKeysFragment<Feed> {

  private FeedService mFeedService;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFeedService = ApiService.createFeedService();
  }

  public static OtherHelpFragment newInstance() {
    return new OtherHelpFragment();
  }

  @Override public Call<ArrayList<Feed>> paginate(Feed sinceItem, Feed maxItem, int perPage) {
    return mFeedService.otherFeed(getArguments().getInt("id"));
  }

  @Override public Object getKeyForData(Feed item) {
    return item.id;
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(Feed.class, TaskPersonalViewHolder.class);
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
    final Feed feed = (Feed) getAdapter().get(position);
    Navigator.startTaskDynamicDetailsActivity(getActivity(), feed.id);
  }
}
