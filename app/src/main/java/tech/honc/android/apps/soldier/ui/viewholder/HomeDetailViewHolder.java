package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.utilities.ScreenUtils;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.model.enums.RoleType;
import tech.honc.android.apps.soldier.utils.toolsutils.LevelUtils;

/**
 * Created by MrJiang on 4/15/2016.
 * 首页中军人viewHolder
 */
public class HomeDetailViewHolder extends EasyViewHolder<User> {
  @Bind(R.id.simple_avatar) SimpleDraweeView mSimpleAvatar;
  @Bind(R.id.soldier_username) TextView mSoldierUsername;
  @Bind(R.id.soldier_rank) ImageView mSoldierRank;
  @Bind(R.id.soldier_sigture) TextView mSoldierSigture;
  @Bind(R.id.soldier_background) RelativeLayout mSoldierBackground;

  public HomeDetailViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_home_detail);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, User value) {
    if (value.uri() != null && value.uri().toString() != null && value.avatar != null) {
      mSimpleAvatar.setImageURI(value.uri(ScreenUtils.dp2px(120), ScreenUtils.dp2px(120)));
    }
    mSoldierUsername.setText(value.nickname);
    mSoldierSigture.setText(value.signature);
    if (value.role != null) {
      if (value.role.toString().equals(RoleType.SOLDIER.toString())) {
        mSoldierRank.setImageResource(LevelUtils.getManMipmapFromLevel(value.level));
      } else if (value.role.toString().equals(RoleType.JUNSAO.toString())) {
        mSoldierRank.setImageResource(LevelUtils.getWoManMipmapFromLevel(value.level));
      }
    }
  }
}
