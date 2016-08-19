package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import com.alibaba.mobileim.WXAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.utility.IMNotificationUtils;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.group.TribeConstants;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;

/**
 * Created by weiquanyun on 15/11/2.
 */
public class EditMyTribeProfileActivity extends BaseActivity {

  private long tribeId;
  private String userId;
  private String oldNick;
  private YWIMKit mIMKit;
  private IYWTribeService mTribeService;
  private EditText mNickInput;
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.submit) TextView mSubmit;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.im_activity_edit_my_tribe_profile);
    Intent intent = getIntent();
    if (intent != null) {
      tribeId = intent.getLongExtra(TribeConstants.TRIBE_ID, 0);
      oldNick = intent.getStringExtra(TribeConstants.TRIBE_NICK);
    }
    if (tribeId == 0) {
      //TODO 群ID没获取到的处理逻辑
      Toast.makeText(this,"没有获取到任何群",Toast.LENGTH_SHORT).show();
    }
    init();
    initToolbar();
    initViews();
  }

  private void init() {
    mIMKit = LoginHelper.getInstance().getYWIMKit();
    mTribeService = mIMKit.getTribeService();
    userId = WXAPI.getInstance().getLongLoginUserId();
  }

  private void initToolbar() {
    mToolbar.setTitle("群资料");
    mToolbar.setTitleTextColor(Color.WHITE);
    setSupportActionBar(mToolbar);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    mSubmit.setClickable(true);
    mSubmit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        uploadModifiedUserNick(mNickInput.getText().toString());
      }
    });
  }

  private void initViews() {
    mNickInput = (EditText) findViewById(R.id.my_profile_input);
    mNickInput.setText(oldNick);
  }
  
  private void uploadModifiedUserNick(final String userNick) {
    mTribeService.modifyTribeUserNick(tribeId, mIMKit.getIMCore().getAppKey(), userId, userNick,
        new IWxCallback() {
          @Override public void onSuccess(Object... result) {
            IMNotificationUtils.showToast("修改成功", EditMyTribeProfileActivity.this);
            Intent intent = new Intent();
            intent.putExtra(TribeConstants.TRIBE_NICK, userNick);
            setResult(Activity.RESULT_OK, intent);
            EditMyTribeProfileActivity.this.finish();
          }

          @Override public void onError(int code, String info) {
            IMNotificationUtils.showToast("修改失败", EditMyTribeProfileActivity.this);
          }

          @Override public void onProgress(int progress) {

          }
        });
  }
}
