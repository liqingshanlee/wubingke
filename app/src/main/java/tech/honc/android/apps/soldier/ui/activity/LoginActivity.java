package tech.honc.android.apps.soldier.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.smartydroid.android.starter.kit.app.StarterNetworkActivity;
import com.smartydroid.android.starter.kit.model.ErrorModel;
import retrofit2.Call;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AuthService;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by Administrator on 2016/4/15.
 */
public class LoginActivity extends StarterNetworkActivity<User> {
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.et_login_username) EditText mEditTextUsername;
  @Bind(R.id.et_login_password) EditText mEditTextPassword;
  private AuthService mAuthService;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    initToolbar();
    mAuthService = ApiService.createAuthService();
    SharedPreferences settings = getSharedPreferences("settings_infos", Context.MODE_PRIVATE);
    mEditTextUsername.setText(settings.getString("username", ""));
    mEditTextPassword.setText(settings.getString("password", ""));
  }

  private void initToolbar() {
    mToolbar.setNavigationIcon(R.mipmap.ic_close_white);
    mToolbar.setTitle("手机号登陆");
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        LoginActivity.this.finish();
      }
    });
  }

  @OnClick({ R.id.btn_login_forgot, R.id.btn_login_register, R.id.btn_login_login })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_login_forgot: {
        Navigator.startForgotActivity(this);
        break;
      }
      case R.id.btn_login_register: {
        Navigator.startRegisterActivity(this);
        break;
      }
      case R.id.btn_login_login: {
        doLogin();
      }
    }
  }

  private void doLogin() {
    final String userName = mEditTextUsername.getText().toString();
    final String passWord = mEditTextPassword.getText().toString();
    SharedPreferences msharedPreferences =
        getSharedPreferences("settings_infos", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = msharedPreferences.edit();
    if (userName.length() != 11) {
      Toast.makeText(this, "手机号必须是11位哦", Toast.LENGTH_SHORT).show();
      return;
    }
    if (passWord.length() < 6) {
      Toast.makeText(this, "密码长度不能小于6位哦", Toast.LENGTH_SHORT).show();
      return;
    }
    editor.putString("username", userName);
    editor.putString("password", passWord);
    editor.apply();
    Call<User> userCall = mAuthService.login(userName, passWord);
    networkQueue().enqueue(userCall);
  }

  @Override public void startRequest() {
    showHud("正在登陆...");
  }

  @Override public void respondSuccess(User data) {
    super.respondSuccess(data);
    if (data.nickname.length() != 0 && data.avatar.length() != 0) {
      AccountManager.setCurrentAccount(data);
      AccountManager.store();
      User user = AccountManager.getCurrentAccount();
      loginOpenIm(user);
    } else {
      dismissHud();
      AccountManager.setCurrentAccount(data);
      AccountManager.store();
      Navigator.startProfileRegisterActivity(LoginActivity.this);
    }
  }

  private void loginOpenIm(User user) {
    if (LoginHelper.getInstance().getYWIMKit() == null) {
      LoginHelper.getInstance().initIMKit(user.openIm.userId, BuildConfig.IM_APP_KEY);
    }
    LoginHelper.getInstance().logIn(user.openIm.userId, user.openIm.password, new IWxCallback() {
      @Override public void onSuccess(Object... objects) {
        dismissHud();
        SnackBarUtil.showText(LoginActivity.this, "登陆成功");
        Navigator.startMainActivity(LoginActivity.this);
        finish();
      }

      @Override public void onError(int i, String s) {
        dismissHud();
        SnackBarUtil.showText(LoginActivity.this, "登陆失败，账号或密码错误");
      }

      @Override public void onProgress(int i) {
      }
    });
  }

  @Override public void error(ErrorModel errorModel) {
    super.error(errorModel);
    SnackBarUtil.showText(this, "登陆失败，账号或密码错误");
    dismissHud();
  }

  @Override public void errorForbidden(ErrorModel errorModel) {
    super.errorForbidden(errorModel);
    SnackBarUtil.showText(LoginActivity.this, "登陆失败，账号或密码错误");
    dismissHud();
  }

  @Override public void respondWithError(Throwable t) {
    dismissHud();
    SnackBarUtil.showText(LoginActivity.this, "登陆失败，账号或密码错误");
  }
}