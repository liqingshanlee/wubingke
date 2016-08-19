package tech.honc.android.apps.soldier.utils.toolsutils;

import com.smartydroid.android.starter.kit.account.AccountManager;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by MrJiang on 2016/5/18.
 * 用户当前的状态
 */
public final class LoginNavigationsUtil {

  public static final int TAG_NO_REGISTER = 100;
  public static final int TAG_HAS_LOGIN = 300;

  /**
   * @return isLogin
   */
  public static int navigationActivity() {
    User user = AccountManager.getCurrentAccount();
    if (AccountManager.isLogin()
        && AccountManager.getCurrentAccount().token() != null) {
      return TAG_HAS_LOGIN;
    } else {
      return TAG_NO_REGISTER;
    }
  }
}
