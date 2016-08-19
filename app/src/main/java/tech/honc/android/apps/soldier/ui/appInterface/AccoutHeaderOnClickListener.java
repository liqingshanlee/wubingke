package tech.honc.android.apps.soldier.ui.appInterface;

import tech.honc.android.apps.soldier.model.User;

/**
 * Created by Administrator on 2016/6/23.
 */
public interface AccoutHeaderOnClickListener {
  void onImageOnClickListener(User user);

  void onNicknameOnClickListener(User user);

  void onFansOnClickListener(User user);

  void onFocusOnClickListener(User user);

  void onHelpOnClickListener(User user);
}
