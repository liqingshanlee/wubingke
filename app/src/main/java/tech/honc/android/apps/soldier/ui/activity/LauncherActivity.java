package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.smartydroid.android.starter.kit.account.AccountManager;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.app.SoldierApp;

/**
 * Created by Administrator on 2016/4/26.
 */
public class LauncherActivity extends BaseActivity
{
  private static final int SPLASH_DISPLAY_LENGTH = 1000;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
    new Handler().postDelayed(new Runnable()
    {
      @Override public void run() {
        if (SoldierApp.isFirstEnterApp()) {
          SoldierApp.enterApp();
          Navigator.startSoldierAppIntro(LauncherActivity.this);
          return;
        }
        //open connect chat
        User user = AccountManager.getCurrentAccount();
        loginOpenIm(user);
      }
    }, SPLASH_DISPLAY_LENGTH);
  }

  private void loginOpenIm(User user) {
    if (user == null) {
      Navigator.startMainActivity(LauncherActivity.this);
      return;
    }
    if (LoginHelper.getInstance().getYWIMKit() == null) {
      LoginHelper.getInstance().initIMKit(user.openIm.userId, BuildConfig.IM_APP_KEY);
    }
    LoginHelper.getInstance().logIn(user.openIm.userId, user.openIm.password, new IWxCallback()
    {
      @Override public void onSuccess(Object... objects) {
        dismissHud();
        Navigator.startMainActivity(LauncherActivity.this);
      }

      @Override public void onError(int i, String s) {
        dismissHud();
        Toast.makeText(LauncherActivity.this, "登录错误,错误码:" + i + "请检查网络是否连接", Toast.LENGTH_SHORT)
            .show();
        // TODO: 2016-5-20-0020 此处待 李青山优化 否则将会卡顿在启动页
        Navigator.startFirstLoginActivity(LauncherActivity.this);
      }

      @Override public void onProgress(int i) {

      }
    });
  }
}
