package tech.honc.android.apps.soldier.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;
import info.hoang8f.widget.FButton;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AuthService;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by Administrator on 2016/4/17.
 */
public class FirstLoginActivity extends BaseActivity {
  private UMShareAPI mShareAPI = null;
  private SHARE_MEDIA platform = null;
  private AuthService mAuthService;
  private User mUser;
  private String mPlatform;
  private String openid;
  @Bind(R.id.f_weibo_button) FButton mFButton;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_first_login);
    mShareAPI = UMShareAPI.get(this);
    mAuthService = ApiService.createAuthService();
    isKickOff();
    initSocialSDK();
    mFButton.setVisibility(View.GONE);
  }

  private void initSocialSDK() {
    Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
  }

  private void isKickOff() {
    String action = getIntent().getAction();
    if (!TextUtils.isEmpty(action) && action.equals("LOGON_FAIL_KICKOFF")) {
      new MaterialDialog.Builder(FirstLoginActivity.this).title("警告")
          .content("你的账号在其他设备上登录，你已被踢下线，如果这不是你自己的操作，请尽快修改密码以保证账户的安全!")
          .cancelable(false)
          .autoDismiss(false)
          .positiveText("重新登录")
          .onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              dialog.dismiss();
            }
          })
          .build()
          .show();
    }
  }

  @OnClick({ R.id.f_weixin_button, R.id.f_weibo_button, R.id.f_qq_button, R.id.f_number_button })
  public void onClick(View view) {
    switch (view.getId()) {

      case R.id.f_weixin_button: {
        platform = SHARE_MEDIA.WEIXIN;
        mShareAPI.doOauthVerify(FirstLoginActivity.this, platform, umAuthListener);
        break;
      }
      case R.id.f_weibo_button: {
        platform = SHARE_MEDIA.SINA;
        mShareAPI.doOauthVerify(FirstLoginActivity.this, platform, umAuthListener);
        break;
      }
      case R.id.f_qq_button: {
        platform = SHARE_MEDIA.QQ;
        mShareAPI.doOauthVerify(FirstLoginActivity.this, platform, umAuthListener);
        break;
      }
      case R.id.f_number_button: {
        Navigator.startLoginActivity(this);
        break;
      }
    }
  }

  private UMAuthListener umAuthListener = new UMAuthListener() {
    @Override public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
      mShareAPI.getPlatformInfo(FirstLoginActivity.this, platform, umAuthInfoListener);
      Toast.makeText(getApplicationContext(), "授权登录成功", Toast.LENGTH_SHORT).show();
    }

    @Override public void onError(SHARE_MEDIA platform, int action, Throwable t) {
      Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
    }

    @Override public void onCancel(SHARE_MEDIA platform, int action) {
      Toast.makeText(getApplicationContext(), "取消授权", Toast.LENGTH_SHORT).show();
    }
  };
  private UMAuthListener umAuthInfoListener = new UMAuthListener() {
    @Override public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
      mPlatform = platform.toString();
      if (data != null) {
        if (platform.equals(SHARE_MEDIA.WEIXIN)) {
          openid = data.get("unionid");
        } else {
          openid = data.get("openid");
        }
        showHud(mPlatform + "登陆中");
        if (platform.equals(SHARE_MEDIA.WEIXIN)) {
          Call<User> call = mAuthService.loginThird("wechat", openid);
          call.enqueue(new Callback<User>() {
            @Override public void onResponse(Call<User> call, Response<User> response) {
              if (response.isSuccessful()) {
                mUser = response.body();
                AccountManager.logout();
                AccountManager.setCurrentAccount(response.body());
                AccountManager.store();
                AccountManager.notifyDataChanged();
                if (mUser.nickname == null
                    || mUser.avatar == null
                    || mUser.nickname.length() == 0
                    || mUser.avatar.length() == 0) {
                  dismissHud();
                  Navigator.startProfileRegisterActivity(FirstLoginActivity.this);
                  return;
                }
                loginOpenIm(mUser);
              }
            }

            @Override public void onFailure(Call<User> call, Throwable t) {
              dismissHud();
              Toast.makeText(getApplicationContext(), "服务器异常无法登录", Toast.LENGTH_SHORT).show();
            }
          });
        } else {
          Call<User> call = mAuthService.loginThird(mPlatform.toLowerCase(), openid);
          call.enqueue(new Callback<User>() {
            @Override public void onResponse(Call<User> call, Response<User> response) {
              if (response.isSuccessful()) {
                mUser = response.body();
                AccountManager.logout();
                AccountManager.setCurrentAccount(response.body());
                AccountManager.store();
                AccountManager.notifyDataChanged();
                if (mUser.avatar == null
                    || mUser.nickname == null
                    || mUser.nickname.length() == 0
                    || mUser.avatar.length() == 0) {
                  dismissHud();
                  Navigator.startProfileRegisterActivity(FirstLoginActivity.this);
                  return;
                }
                loginOpenIm(mUser);
              }
            }

            @Override public void onFailure(Call<User> call, Throwable t) {
              dismissHud();
              Toast.makeText(getApplicationContext(), "服务器异常无法登录", Toast.LENGTH_SHORT).show();
            }
          });
        }
      }
    }

    @Override public void onError(SHARE_MEDIA platform, int action, Throwable t) {
      Toast.makeText(getApplicationContext(), "获取资料失败", Toast.LENGTH_SHORT).show();
    }

    @Override public void onCancel(SHARE_MEDIA platform, int action) {
      Toast.makeText(getApplicationContext(), "取消登录", Toast.LENGTH_SHORT).show();
    }
  };

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d("auth", "on activity re 2");
    mShareAPI.onActivityResult(requestCode, resultCode, data);
    Log.d("auth", "on activity re 3");
  }

  private void loginOpenIm(User user) {
    if (user == null) {
      Navigator.startMainActivity(FirstLoginActivity.this);
      return;
    }
    if (LoginHelper.getInstance().getYWIMKit() == null) {
      LoginHelper.getInstance().initIMKit(user.openIm.userId, BuildConfig.IM_APP_KEY);
    }
    LoginHelper.getInstance().logIn(user.openIm.userId, user.openIm.password, new IWxCallback() {
      @Override public void onSuccess(Object... objects) {
        dismissHud();
        Navigator.startMainActivity(FirstLoginActivity.this);
        finish();
      }

      @Override public void onError(int i, String s) {
        dismissHud();
        Toast.makeText(FirstLoginActivity.this, "登录错误,请检查网络连接", Toast.LENGTH_SHORT).show();
        // TODO: 2016-5-20-0020 此处待 李青山优化
      }

      @Override public void onProgress(int i) {
        dismissHud();
      }
    });
  }
}
