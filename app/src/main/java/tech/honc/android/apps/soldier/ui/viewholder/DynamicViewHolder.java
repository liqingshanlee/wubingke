package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.ui.widget.AccountDetailCollectionView;
import tech.honc.android.apps.soldier.utils.settings.SettingItems;
import tech.honc.android.apps.soldier.utils.toolsutils.DateFormat;

/**
 * Created by MrJiang on 4/17/2016.
 * 动态
 */
public class DynamicViewHolder extends BaseViewHolder {
  @Bind(R.id.detail_article_content) TextView mDetailArticleContent;
  @Bind(R.id.photo_collections) AccountDetailCollectionView mPhotoCollections;
  @Bind(R.id.article_date) TextView mArticleDate;
  @Bind(R.id.article_time) TextView mArticleTime;
  @Bind(R.id.container_dynamic) RelativeLayout mContainer;

  public DynamicViewHolder(Context context, ViewGroup parent) {
    super(context,
        LayoutInflater.from(context).inflate(R.layout.list_item_single_dynamic, parent, false));
    ButterKnife.bind(this, itemView);
  }

  @Override protected void bindTo(SettingItems settingItem) {
    super.bindTo(settingItem);
    final Feed feed = (Feed) settingItem.mData;
    if (feed != null && feed.id != null) {
      mContainer.setVisibility(View.VISIBLE);
      if (TextUtils.isEmpty(feed.content) || feed.content == null) {
        mDetailArticleContent.setVisibility(View.GONE);
      } else {
        mDetailArticleContent.setVisibility(View.VISIBLE);
        mDetailArticleContent.setText(feed.content);
      }
      if (feed.images == null || feed.images.size() == 0) {
        mPhotoCollections.setVisibility(View.GONE);
      } else {
        mPhotoCollections.setVisibility(View.VISIBLE);
        mPhotoCollections.setData(feed.images);
      }
      mArticleDate.setText(DateFormat.getRelativeTime(feed.createdAt));
      mArticleTime.setText(DateFormat.getCommonTime(feed.createdAt));
    } else {
      mContainer.setVisibility(View.GONE);
    }
  }
}
