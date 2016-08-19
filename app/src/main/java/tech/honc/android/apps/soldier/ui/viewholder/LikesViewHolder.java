package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Likes;

/**
 * Created by Administrator on 2016/4/25.
 */
public class LikesViewHolder extends EasyViewHolder<Likes> {
  @Bind(R.id.simple_model) SimpleDraweeView mSimpleModel;

  public LikesViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_likes);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, Likes value) {
    if (value.user.uri() != null) {
      mSimpleModel.setImageURI(value.user.uri());
    }
  }
}
