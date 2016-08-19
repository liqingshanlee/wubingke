package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import com.smartydroid.android.starter.kit.account.AccountManager;
import com.smartydroid.android.starter.kit.app.StarterActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.SignatureService;
import tech.honc.android.apps.soldier.model.Signature;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by Administrator on 2016/5/3.
 */
public class SignatureActivity extends StarterActivity {
  @Bind(R.id.signature_text) EditText mEditText;
  private SignatureService mSignatureService;
  private User mUser = AccountManager.getCurrentAccount();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feed_back);
    mSignatureService = ApiService.createSignatureService();
    assert mUser.signature != null;
    mEditText.setText(mUser.signature);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.save_ok) {
      postSignature();
    } else if (item.getItemId() == android.R.id.home) {
      finish();
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_save, menu);
    return super.onCreateOptionsMenu(menu);
  }

  public void postSignature() {
    final String mEditTextData = mEditText.getText().toString();
    if (mEditTextData.length() > 30 || mEditTextData.equals("")) {
      Toast.makeText(this, "签名字数不超过30个", Toast.LENGTH_SHORT).show();
      return;
    }

    Call<Signature> call = mSignatureService.postSign(mEditTextData);
    showHud("数据提交中");
    call.enqueue(new Callback<Signature>() {
      @Override public void onResponse(Call<Signature> call, Response<Signature> response) {
        if (response.isSuccessful()) {
          mUser.signature = response.body().sign;
          AccountManager.setCurrentAccount(mUser);
          AccountManager.notifyDataChanged();
          dismissHud();
          SignatureActivity.this.finish();
        }
      }

      @Override public void onFailure(Call<Signature> call, Throwable t) {

      }
    });
  }
}
