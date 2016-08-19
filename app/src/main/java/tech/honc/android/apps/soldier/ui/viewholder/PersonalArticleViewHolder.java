package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Article;
import tech.honc.android.apps.soldier.utils.toolsutils.DateFormat;

/**
 * Created by MrJiang
 * on 2016/4/21.
 */
public class PersonalArticleViewHolder extends EasyViewHolder<Article> {
  @Bind(R.id.article_title) TextView mArticleTitle;
  @Bind(R.id.simple) SimpleDraweeView mSimple;
  @Bind(R.id.type_value) TextView mTypeValue;
  @Bind(R.id.article_comment) TextView mArticleComment;
  @Bind(R.id.article_satr) TextView mArticleSatr;
  @Bind(R.id.type_time) TextView mTypeTime;

  public PersonalArticleViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_article);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, Article value) {
    mArticleTitle.setText(value.title);
    mSimple.setImageURI(value.uri() != null ? value.uri() : null);
    mTypeValue.setText(" " + value.categoryName);
    mArticleComment.setText(value.commentTimes != null ? " " + value.commentTimes + "" : "0");
    mArticleSatr.setText(value.likeTimes != null ? " " + value.likeTimes + "" : "0");
    mTypeTime.setText(" " + DateFormat.getRelativeTime(value.createdAt));
  }
}
