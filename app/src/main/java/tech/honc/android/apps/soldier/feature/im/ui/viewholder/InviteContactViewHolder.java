package tech.honc.android.apps.soldier.feature.im.ui.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.widget.ForegroundRelativeLayout;
import tech.honc.android.apps.soldier.R;

/**
 * Created by MrJiang on 2016/6/3.
 */
public class InviteContactViewHolder extends RecyclerView.ViewHolder {
  @Bind(R.id.im_simple_avatar) public SimpleDraweeView mImSimpleAvatar;
  @Bind(R.id.im_text_username) public TextView mImTextUsername;
  @Bind(R.id.im_text_mobile) public TextView mImTextMobile;
  @Bind(R.id.im_text_invite) public TextView mImTextInvite;
  @Bind(R.id.im_text_view_letter) public TextView mImTextLetter;
  @Bind(R.id.ui_invite_contacts_container) public ForegroundRelativeLayout mRelaytiveLayout;
  private Context mContext;

  public InviteContactViewHolder(Context context,View itemView) {
    super(itemView);
    mContext = context;
    ButterKnife.bind(this,itemView);
  }
}
