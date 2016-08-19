package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import support.ui.adapters.BaseEasyViewHolderFactory;
import support.ui.adapters.EasyViewHolder;

/**
 * Created by Administrator on 2016/4/26.
 */
public class DynamicDetailsFactory extends BaseEasyViewHolderFactory {

  private static final int VIEW_TYPE_DETAILS = 0;
  private static final int VIEW_TYPE_PRAISE = 1;
  private static final int VIEW_TYPE_COMMENTS = 2;

  public DynamicDetailsFactory(Context context) {
    super(context);
  }

  @Override public EasyViewHolder create(int viewType, ViewGroup parent) {
    EasyViewHolder viewHolder = null;
    switch (viewType) {
      case VIEW_TYPE_DETAILS:
        viewHolder = new FeedHeadViewHolder(parent.getContext(), parent);
        break;
      case VIEW_TYPE_PRAISE:
        viewHolder = new FeedLikeViewHolder(parent.getContext(), parent);
        break;
      case VIEW_TYPE_COMMENTS:
        viewHolder = new FeedCommentViewHolder(parent.getContext(), parent);
        break;
    }

    return viewHolder;
  }

  @Override public int itemViewType(Object object) {
    //if (object instanceof Feed) {
    //  Feed feed = (Feed) object;
    //  if(feed.images.isEmpty()){
    //    return VIEW_TYPE_DETAILS;
    //  }else{
    //    return VIEW_TYPE_COMMENTS;
    //  }
    //}
    return VIEW_TYPE_COMMENTS;
  }
}
