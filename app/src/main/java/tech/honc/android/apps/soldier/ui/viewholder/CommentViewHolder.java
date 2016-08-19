package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Comments;

/**
 * Created by Administrator on 2016/4/25.
 */
public class CommentViewHolder extends EasyViewHolder<Comments> {
  @Bind(R.id.simple_model) SimpleDraweeView mSimpleModel;
  @Bind(R.id.detail_username) TextView mDetailUsername;
  @Bind(R.id.detail_content) TextView mDetailContent;
  @Bind(R.id.detail_time) TextView mDetailTime;

  public CommentViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_comment);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, Comments value) {
    mSimpleModel.setImageURI(value.Auser.uri());
    mDetailUsername.setText(value.Auser.nickname);
    mDetailContent.setText(value.content);
    mDetailTime.setText("五天前");
  }
}
