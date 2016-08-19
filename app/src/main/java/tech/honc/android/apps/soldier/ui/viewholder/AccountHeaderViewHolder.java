package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.smartydroid.android.starter.kit.account.AccountManager;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.appInterface.AccoutHeaderOnClickListener;
import tech.honc.android.apps.soldier.ui.fragment.AccountFragment;

/**
 * Created by YuGang Yang on 04 19, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public class AccountHeaderViewHolder extends EasyViewHolder<User> {
  private AccoutHeaderOnClickListener mAccoutHeaderOnClickListener;
  public User mUser = AccountManager.getCurrentAccount();
  @Bind(R.id.avatar_image) SimpleDraweeView mAvatarView;
  @Bind(R.id.text_account_nickname) TextView mNickname;
  @Bind(R.id.text_account_location) TextView mLocation;
  @Bind(R.id.text_account_followers) TextView mFollowers;
  @Bind(R.id.text_account_following) TextView mFollowing;
  @Bind(R.id.text_account_helps) TextView mHelps;

  public AccountHeaderViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.account_header);
    ButterKnife.bind(this, itemView);
    setAccoutHeaderOnClickListener(AccountFragment.mAccoutHeaderOnClickListener);
  }

  @Override public void bindTo(int position, User value) {
    if (value.uri() != null && value.uri().toString().length() != 0) {
      mAvatarView.setImageURI(value.uri());
    }

    mNickname.setText(value.nickname != null ? value.nickname : "未取名.. ");
    mLocation.setText(value.city != null ? value.city : "未选择..");
    mFollowers.setText(value.followers + "");
    mFollowing.setText(value.followings + "");
    mHelps.setText(value.helps + "");
  }

  @OnClick({
      R.id.avatar_image, R.id.nickname_container, R.id.fans_container, R.id.focus_container,
      R.id.help_container
  }) public void OnClick(View v) {
    if (mAccoutHeaderOnClickListener == null) {
      return;
    }
    switch (v.getId()) {
      case R.id.avatar_image:
        mAccoutHeaderOnClickListener.onImageOnClickListener(mUser);
        break;
      case R.id.nickname_container:
        mAccoutHeaderOnClickListener.onNicknameOnClickListener(mUser);
        break;

      case R.id.fans_container:
        mAccoutHeaderOnClickListener.onFansOnClickListener(mUser);
        break;

      case R.id.focus_container:
        mAccoutHeaderOnClickListener.onFocusOnClickListener(mUser);
        break;
      case R.id.help_container:
        mAccoutHeaderOnClickListener.onHelpOnClickListener(mUser);
        break;
    }
  }

  public void setAccoutHeaderOnClickListener(
      AccoutHeaderOnClickListener accoutHeaderOnClickListener) {
    this.mAccoutHeaderOnClickListener = accoutHeaderOnClickListener;
  }
}
