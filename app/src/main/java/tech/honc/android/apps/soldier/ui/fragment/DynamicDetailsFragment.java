package tech.honc.android.apps.soldier.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FeedService;

/**
 * Created by Administrator on 2016/4/21.
 */
public class DynamicDetailsFragment extends BaseFragment{

  public int feedId;
  private FeedService mFeedService;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFeedService = ApiService.createFeedService();
    Intent intent = getActivity().getIntent();
    feedId = intent.getIntExtra("feedid", 0);
  }

  @Override protected int getFragmentLayout() {
    return R.layout.list_item_feed_head;
  }


}
