package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.model.enums.GenderType;

/**
 * Created by MrJiang on 4/28/2016.
 */
public class PersonalFocusViewHolder extends EasyViewHolder<User> {
  @Bind(R.id.focus_simple) SimpleDraweeView mFocusSimple;
  @Bind(R.id.focus_username) TextView mFocusUsername;
  @Bind(R.id.focus_sex) ImageView mFocusSex;
  @Bind(R.id.focus_content) TextView mFocusContent;

  public PersonalFocusViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_personal_focus);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, User value) {
    mFocusSimple.setImageURI(value.uri());
    mFocusUsername.setText(value.nickname);
    mFocusContent.setText(value.signature);
    mFocusSex.setImageResource(GenderType.getIconResource(value.gender));
  }
}
