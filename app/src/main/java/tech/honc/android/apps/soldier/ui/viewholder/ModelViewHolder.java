package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by MrJiang
 * on 2016/4/19.
 */
public class ModelViewHolder extends EasyViewHolder<User> {
  @Bind(R.id.simple_model) SimpleDraweeView mSimpleModel;

  public ModelViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_home_model);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, User value) {
    if (value.uri() != null) {
      mSimpleModel.setImageURI(value.uri());
    }
  }
}
