package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.utilities.DateTimeUtils;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.ArticleComment;

/**
 * Created by MrJiang on 5/1/2016.
 * 文章评论ViewHolder
 */
public class ArticleCommentViewHolder extends EasyViewHolder<ArticleComment> {

  @Bind(R.id.info_comment_header) SimpleDraweeView mInfoCommentHeader;
  @Bind(R.id.info_detail_username) TextView mInfoDetailUsername;
  @Bind(R.id.info_comment_time) TextView mInfoCommentTime;
  @Bind(R.id.info_comment_content) TextView mInfoCommentContent;

  public ArticleCommentViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_article_comment);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, ArticleComment value) {
    if (value.account!=null&&value.account.uri()!=null)
    mInfoCommentHeader.setImageURI(value.account.uri());
    if (value.account != null) {
      mInfoDetailUsername.setText(value.account.nickname);
    }
    mInfoCommentTime.setText(DateTimeUtils.getRelativeTime(value.createdAt));
    mInfoCommentContent.setText(value.content);
  }
}
