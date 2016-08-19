package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.smartydroid.android.starter.kit.utilities.DateTimeUtils;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.widget.PhotoCollectionView;

/**
 * Created by MrJiang on 2016/4/28.
 */
public class UserFeedViewHolder extends EasyViewHolder<Feed> {

  @Bind(R.id.feed_content) TextView mFeedContent;
  @Bind(R.id.photo_image) PhotoCollectionView mPhotoImage;
  @Bind(R.id.feed_time) TextView mFeedTime;
  @Bind(R.id.dynamic_praise_num) TextView mDynamicStar;
  @Bind(R.id.dynamic_msg_num) TextView mDynamicComment;
  private User mUser;

  public UserFeedViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_user_feed);
    ButterKnife.bind(this, itemView);
    if (mUser == null) {
      mUser = new User();
    }
  }

  @Override public void bindTo(int position, Feed value) {
    if (TextUtils.isEmpty(value.content)) {
      mFeedContent.setVisibility(View.GONE);
    } else {
      mFeedContent.setVisibility(View.VISIBLE);
      mFeedContent.setText(value.content != null ? value.content : "");
    }

    if (value.images != null && value.images.size() != 0) {
      mPhotoImage.setVisibility(View.VISIBLE);
      mPhotoImage.setData(value.images);
    } else {
      mPhotoImage.setVisibility(View.GONE);
    }
    mFeedTime.setText(DateTimeUtils.getRelativeTime(value.createdAt));
    mDynamicStar.setText(value.likeTimes != null ? value.likeTimes + "" : "0");
    mDynamicComment.setText(value.commentTimes != null ? value.commentTimes + "" : "0");
  }
}
