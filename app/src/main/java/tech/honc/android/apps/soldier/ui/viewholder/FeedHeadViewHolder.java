package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.utilities.DateTimeUtils;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.ui.widget.PhotoCollectionView;

/**
 * Created by MrJiang on 4/26/2016.
 * feed head
 */
public class FeedHeadViewHolder extends EasyViewHolder<Feed> {

  @Bind(R.id.dynamic_details_img) SimpleDraweeView mDynamicDetailsImg;
  @Bind(R.id.dynamic_details_name) TextView mDynamicDetailsName;
  @Bind(R.id.dynamic_details_sex) ImageView mDynamicDetailsSex;
  @Bind(R.id.dynamic_details_content) TextView mDynamicDetailsContent;
  @Bind(R.id.dynamic_details_gv) PhotoCollectionView mDynamicDetailsGv;
  @Bind(R.id.dynamic_details_time) TextView mDynamicDetailsTime;

  public FeedHeadViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_feed_head);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, final Feed feed) {
    if (feed != null) {
      mDynamicDetailsImg.setImageURI(feed.user.uri());
      mDynamicDetailsName.setText(feed.user.nickname);
      if (feed.user.gender.toString().equals("female")) {
        mDynamicDetailsSex.setImageResource(R.mipmap.ic_sex_man);
      } else {
        mDynamicDetailsSex.setImageResource(R.mipmap.ic_sex_woman);
      }
      mDynamicDetailsContent.setText(feed.content);
      if (feed.images != null) {
        mDynamicDetailsGv.setData(feed.images);
      }
      mDynamicDetailsTime.setText(DateTimeUtils.getRelativeTime(feed.createdAt));
    }
  }
}
