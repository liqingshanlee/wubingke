package tech.honc.android.apps.soldier.feature.im.ui.viewholder;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.account.AccountManager;
import support.ui.adapters.EasyViewHolder;
import support.ui.app.SupportApp;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.model.IMProfile;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by MrJiang on 2016/5/31.
 * 加载好友的搜索
 */
public class IMProfileViewHolder extends EasyViewHolder<IMProfile> {

  @Bind(R.id.improfile_avatar) SimpleDraweeView mImprofileAvatar;
  @Bind(R.id.improfile_nick_name) TextView mImprofileNickName;
  @Bind(R.id.improfile_phone) TextView mImprofilePhone;
  @Bind(R.id.im_friend) TextView mImFriend;

  public IMProfileViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_improfile);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, IMProfile value) {
    if (value != null) {
      mImprofileAvatar.setImageURI(Uri.parse(value.avatar));
      mImprofileNickName.setText(value.nickname);
      mImprofilePhone.setText(value.signature);
      User user = AccountManager.getCurrentAccount();
      if (user!=null&&user.openIm.userId!=value.open_im_id) {
        if (value.firend) {
          mImFriend.setText("已是好友");
          mImFriend.setBackgroundColor(SupportApp.color(R.color.dim_foreground));
        } else {
          mImFriend.setText("加好友");
          mImFriend.setBackgroundColor(SupportApp.color(R.color.colorPrimary));
        }
      }else {
        mImFriend.setText("本人");
        mImFriend.setBackgroundColor(SupportApp.color(R.color.dim_foreground));
      }
    }
  }
}
