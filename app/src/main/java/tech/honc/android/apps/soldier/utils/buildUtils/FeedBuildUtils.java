package tech.honc.android.apps.soldier.utils.buildUtils;

import java.util.ArrayList;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.utils.settings.FeedItems;

/**
 * Created by MrJiang on 4/26/2016.
 */
@Deprecated public class FeedBuildUtils {

  public static ArrayList<FeedItems> buildFeedData(Feed feed) {
    ArrayList<FeedItems> items = new ArrayList<>();
    items.add(
        new FeedItems.Builder().data(feed).itemViewType(FeedItems.VIEW_TYPE_FEED_CELL).build());

    items.add(
        new FeedItems.Builder().data(feed).itemViewType(FeedItems.VIEW_TYPE_FEED_LIKES).build());

    items.add(
        new FeedItems.Builder().data(feed).itemViewType(FeedItems.VIEW_TYPE_FEED_COMMENTS).build());

    return items;
  }
}
