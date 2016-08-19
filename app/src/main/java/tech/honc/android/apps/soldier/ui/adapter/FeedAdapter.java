package tech.honc.android.apps.soldier.ui.adapter;

import android.content.Context;
import java.util.ArrayList;
import tech.honc.android.apps.soldier.model.Comments;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by MrJiang on 4/17/2016.
 * 复用adapter
 */
public class FeedAdapter {

  public static final int TYPE_ITEM_HEAD = 0;
  public static final int TYPE_ITEM_LIKES = 1;
  public static final int TYPE_ITEM_COMMENTS = 2;

  private Context context;
  private Feed feed;
  private ArrayList<User> likesArrayList;
  private ArrayList<Comments> commentsArrayList;

  public FeedAdapter(Context context, Feed feed, ArrayList<User> likesArrayList,
      ArrayList<Comments> commentsArrayList) {
    this.context = context;
    this.feed = feed;
    this.likesArrayList = likesArrayList;
    this.commentsArrayList = commentsArrayList;
  }

  //@Override public BaseFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
  //
  //  switch (viewType) {
  //    case TYPE_ITEM_HEAD:
  //      return new FeedHeadViewHolder(context,
  //          View.inflate(context, R.layout.list_item_feed_head, null));
  //  }
  //  return null;
  //}
  //
  //@Override public void onBindViewHolder(BaseFeedViewHolder holder, int position) {
  //  if (holder instanceof FeedHeadViewHolder) {
  //    ((FeedHeadViewHolder) holder).initData(feed);
  //  }
  //
  //}
  //
  //@Override public int getItemCount() {
  //  if (likesArrayList.size() == 0 && commentsArrayList.size() == 0) {
  //    return 1;
  //  } else if (likesArrayList.size() != 0 && commentsArrayList.size() == 0) {
  //    return 2;
  //  } else if (likesArrayList.size() == 0 && commentsArrayList.size() != 0) {
  //    return commentsArrayList.size() + 1;
  //  }
  //  return commentsArrayList.size() + 2;
  //}
  //
  //@Override public int getItemViewType(int position) {
  //  if (position == 0) {
  //    return TYPE_ITEM_HEAD;
  //  } else if (position == 1 && likesArrayList.size() != 0) {
  //    return TYPE_ITEM_LIKES;
  //  }
  //  return TYPE_ITEM_COMMENTS;
  //}
}
