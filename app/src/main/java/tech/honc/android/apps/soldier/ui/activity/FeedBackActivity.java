package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import com.smartydroid.android.starter.kit.utilities.Strings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.UpdateProfileService;
import tech.honc.android.apps.soldier.model.UpdateData;

/**
 * Created by Administrator on 2016/4/25.
 */
public class FeedBackActivity extends BaseActivity
{
  @Bind(R.id.signature_text) EditText mEditText;
  private UpdateProfileService mUpdateProfileService;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feed_back);
    mUpdateProfileService = ApiService.createUpdateProfileService();
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.save_ok) {
      postFeedBack();
    } else if (item.getItemId() == android.R.id.home) {
      finish();
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_feedback, menu);
    return super.onCreateOptionsMenu(menu);
  }

  private void postFeedBack() {
    final String mFeedBack = mEditText.getText().toString();
    if (Strings.isBlank(mFeedBack)) {
      Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
      return;
    }
    Call<UpdateData> callSoldier = mUpdateProfileService.postFeedBack(mFeedBack);
    showHud("正在提交");
    callSoldier.enqueue(new Callback<UpdateData>()
    {
      @Override public void onResponse(Call<UpdateData> call, Response<UpdateData> response) {
        if (response.isSuccessful()) {
          dismissHud();
          FeedBackActivity.this.finish();
        }
      }

      @Override public void onFailure(Call<UpdateData> call, Throwable t) {

      }
    });
  }
}
