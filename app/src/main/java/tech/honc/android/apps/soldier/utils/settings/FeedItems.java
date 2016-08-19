package tech.honc.android.apps.soldier.utils.settings;

/**
 * Created by MrJiang on 4/26/2016.
 */
public class FeedItems {

  public static final int VIEW_TYPE_FEED_CELL = 0;
  public static final int VIEW_TYPE_FEED_LIKES = 1;
  public static final int VIEW_TYPE_FEED_COMMENTS = 2;

  public int itemViewType;
  public Object mData;

  private FeedItems() {
  }

  public static class Builder {
    private int itemViewType;
    private Object mData;

    public FeedItems build() {
      FeedItems items = new FeedItems();
      items.itemViewType = itemViewType;
      items.mData = mData;
      return items;
    }

    public Builder itemViewType(int itemViewType) {
      this.itemViewType = itemViewType;
      return this;
    }

    public Builder data(Object data) {
      this.mData = data;
      return this;
    }
  }
}
