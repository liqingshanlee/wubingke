package tech.honc.android.apps.soldier.ui.fragment;

import android.os.Bundle;
import android.view.View;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.smartydroid.android.starter.kit.account.AccountManager;
import java.util.ArrayList;
import retrofit2.Call;
import support.ui.adapters.EasyRecyclerAdapter;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.amap.AmapLocation;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FeedService;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.viewholder.TaskFragmentViewHolder;

/**
 * Created by Administrator on 2016/4/14.
 * 任务fragment
 */
public class TaskFragment extends BaseKeysFragment<Feed> implements AMapLocationListener {

  private FeedService mFeedService;
  private AmapLocation mAmapLocation;
  private boolean mIsFirstIn;

  public static TaskFragment newInstance() {
    return new TaskFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mIsFirstIn = true;
    mFeedService = ApiService.createFeedService();
    init();
  }

  private void init() {
    mAmapLocation = new AmapLocation(getContext());
    mAmapLocation.initLocations(getContext(), this);
    mAmapLocation.startLocations();
  }

  @Override public Call<ArrayList<Feed>> paginate(Feed sinceItem, Feed maxItem, int perPage) {
    User user = AccountManager.getCurrentAccount();
    return mFeedService.getTaskFeeds(user != null && user.latitude != null ? user.latitude : 0,
        user != null && user.longitude != null ? user.longitude : 0);
  }

  @Override public Object getKeyForData(Feed item) {
    return item.id;
  }

  @Override public void bindViewHolders(EasyRecyclerAdapter adapter) {
    adapter.bind(Feed.class, TaskFragmentViewHolder.class);
  }

  @Override public void onItemClick(int position, View view) {
    super.onItemClick(position, view);
        final Feed feed = (Feed) getAdapter().get(position);
        Navigator.startTaskDynamicDetailsActivity(getActivity(), feed.id);
  }

  @Override public void onResume() {
    super.onResume();
    if (!mIsFirstIn) {
      onRefresh();
    }
    mIsFirstIn = false;
  }

  @Override public void onRefresh() {
    super.onRefresh();
    mAmapLocation.startLocations();
  }

  @Override public void onLocationChanged(AMapLocation aMapLocation) {
    if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
      User user = AccountManager.getCurrentAccount();
      if (user == null) {
        user = new User();
      }
      user.city = aMapLocation.getCity();
      user.latitude = aMapLocation.getLatitude();
      user.longitude = aMapLocation.getLongitude();
      AccountManager.setCurrentAccount(user);
      AccountManager.notifyDataChanged();
      refresh();
      mAmapLocation.stopLocations();
    }
  }

  @Override public void onStop() {
    super.onStop();
    mAmapLocation.stopLocations();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    mAmapLocation.destroyLocations();
  }
}
