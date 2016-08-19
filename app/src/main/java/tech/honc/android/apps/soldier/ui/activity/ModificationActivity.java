package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.smartydroid.android.starter.kit.app.StarterNetworkActivity;
import retrofit2.Call;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AuthService;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by Administrator on 2016/4/25.
 */
public class ModificationActivity extends StarterNetworkActivity<User>
{
  @Bind(R.id.et_old_username) EditText mOlduserName;
  @Bind(R.id.et_new_password) EditText mNewuserName;
  @Bind(R.id.et_affirm_password) EditText mAffirmuserName;

  private AuthService mAuthService;
  private boolean mSuccess = false;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_modificatoin_passwrod);
    mAuthService = ApiService.createAuthService();
  }

  @OnClick({ R.id.btn_modification_login }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_modification_login: {
        doModification();
        break;
      }
    }
  }

  private void doModification() {
    final String oldPasswrod = mOlduserName.getText().toString();
    final String newPasswrod = mNewuserName.getText().toString();
    final String affirmPasswrod = mAffirmuserName.getText().toString();
    User user = AccountManager.getCurrentAccount();
    final String username = user.mobile;

    if (mOlduserName.getText().length() == 0) {
      Toast.makeText(this, "请填旧密码", Toast.LENGTH_SHORT).show();
      return;
    }
    if (mNewuserName.getText().length() < 6) {
      Toast.makeText(this, "密码不能少于六位", Toast.LENGTH_SHORT).show();
      return;
    }
    if (!newPasswrod.equals(affirmPasswrod)) {

      Toast.makeText(this, "确认密码不符", Toast.LENGTH_SHORT).show();
      return;
    }
    Call<User> userCall = mAuthService.modification(username, oldPasswrod, newPasswrod);
    networkQueue().enqueue(userCall);
  }

  @Override public void startRequest() {
    showHud("正在修改");
  }

  @Override public void respondSuccess(User data) {
    super.respondSuccess(data);
    AccountManager.store(data);
    AccountManager.setCurrentAccount(data);
    showHud("修改成功");
    mSuccess = true;
  }

  @Override public void endRequest() {

    if (mSuccess) {
      mSuccess = false;
      Navigator.startSettingActivity(this);
    }
    dismissHud();
  }
}
