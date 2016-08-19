package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.smartydroid.android.starter.kit.app.StarterNetworkActivity;
import com.smartydroid.android.starter.kit.model.ErrorModel;
import com.smartydroid.android.starter.kit.network.callback.MessageCallback;
import com.smartydroid.android.starter.kit.utilities.NetworkUtils;
import com.smartydroid.android.starter.kit.utilities.Strings;
import retrofit2.Call;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AuthService;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.TimerCount;

/**
 * Created by Administrator on 2016/4/18.
 */
public class RegisterActivity extends StarterNetworkActivity<User>
{

  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.btn_code) Button mGetIdentifying;
  @Bind(R.id.et_register_username) EditText mEditTextUsername;
  @Bind(R.id.et_register_password) EditText mEditTextPassword;
  @Bind(R.id.et_register_code) EditText mEditTextCode;
  private AuthService mAuthService;
  private NetworkUtils<ErrorModel> mNetworkUtils;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
    ButterKnife.bind(this);
    initToolbar();
    mAuthService = ApiService.createAuthService();
  }

  private void initToolbar() {

    mToolbar.setNavigationIcon(R.mipmap.ic_back);
    mToolbar.setTitle("注册");
    mToolbar.setNavigationOnClickListener(new View.OnClickListener()
    {
      @Override public void onClick(View v) {
        RegisterActivity.this.finish();
      }
    });
  }

  @OnClick({ R.id.btn_code, R.id.btn_register_ok }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_code: {
        doPostCode();
        break;
      }
      case R.id.btn_register_ok: {
        doRegister();
        break;
      }
    }
  }

  private void doPostCode() {
    final String username = mEditTextUsername.getText().toString();
    if (username.length() != 11) {
      Toast.makeText(this, "手机号不正确", Toast.LENGTH_SHORT).show();
      return;
    }
    Call<ErrorModel> codeCall = mAuthService.postCode(username);
    showHud("获取验证码");
    mNetworkUtils = NetworkUtils.<ErrorModel>create(new MessageCallback<ErrorModel>(this)
    {
      @Override public void respondSuccess(ErrorModel data) {
        super.respondSuccess(data);
        SnackBarUtil.showText(RegisterActivity.this, data.getMessage());
        dismissHud();
        TimerCount timer = new TimerCount(60000, 1000, mGetIdentifying);
        timer.start();
      }

      @Override public void endRequest() {

      }
    });
    mNetworkUtils.enqueue(codeCall);
  }

  private void doRegister() {
    final String userName = mEditTextUsername.getText().toString();
    final String passWord = mEditTextPassword.getText().toString();
    final String code = mEditTextCode.getText().toString();
    if (Strings.isBlank(userName)) {
      Toast.makeText(this, "用户账号不规范", Toast.LENGTH_SHORT).show();
      return;
    }

    if (mEditTextPassword.getText().length() < 6 || mEditTextPassword.getText().length() > 18) {
      Toast.makeText(this, "用户密码长度6-18位", Toast.LENGTH_SHORT).show();
      return;
    }
    if (Strings.isBlank(code)) {
      Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
      return;
    }
    Call<User> userCall = mAuthService.register(userName, code, passWord);
    networkQueue().enqueue(userCall);
  }

  @Override public void startRequest() {
    showHud("正在注册");
  }

  private boolean mSuccess = false;

  @Override public void respondSuccess(User data) {
    super.respondSuccess(data);
    AccountManager.store(data);
    mSuccess = true;
  }

  @Override public void endRequest() {
    if (mSuccess) {
      Navigator.startProfileRegisterActivity(this);
    }
    dismissHud();
  }
}
