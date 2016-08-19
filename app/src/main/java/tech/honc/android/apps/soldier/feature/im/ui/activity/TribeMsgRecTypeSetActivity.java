package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Bind;
import com.alibaba.mobileim.channel.constant.YWProfileSettingsConstants;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;

public class TribeMsgRecTypeSetActivity extends BaseActivity implements View.OnClickListener {

  private int flag;
  private ImageView mTribeMsgRecCheck;
  private ImageView mTribeMsgRecNotRemindCheck;
  private ImageView mTribeMsgRejCheck;

  private RelativeLayout mTribeMsgRecLayout;
  private RelativeLayout mTribeMsgRecNotRemindLayout;
  private RelativeLayout mTribeMsgRejLayout;
  @Bind(R.id.toolbar) Toolbar mToolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.im_activity_tribe_msg_rec_type_set);
    initToolbar();
    Intent intent = getIntent();
    flag = intent.getIntExtra("Flag", YWProfileSettingsConstants.TRIBE_MSG_REJ);
    initViews();
    initMsgRecFlag(flag);
  }

  private void initToolbar() {
    mToolbar.setTitle("聊天设置");
    mToolbar.setTitleTextColor(Color.WHITE);
    setSupportActionBar(mToolbar);
    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
  }

  private void initViews() {
    mTribeMsgRecLayout = (RelativeLayout) findViewById(R.id.receive_and_remind_layout);
    mTribeMsgRecNotRemindLayout = (RelativeLayout) findViewById(R.id.only_receive_layout);
    mTribeMsgRejLayout = (RelativeLayout) findViewById(R.id.not_receive_layout);

    mTribeMsgRecCheck = (ImageView) findViewById(R.id.receive_and_remind);
    mTribeMsgRecNotRemindCheck = (ImageView) findViewById(R.id.only_receive);
    mTribeMsgRejCheck = (ImageView) findViewById(R.id.not_receive);

    mTribeMsgRecLayout.setOnClickListener(this);
    mTribeMsgRecNotRemindLayout.setOnClickListener(this);
    mTribeMsgRejLayout.setOnClickListener(this);
  }

  public static Intent getTribeMsgRecTypeSetActivityIntent(Context context, int flag) {
    Intent intent = new Intent(context, TribeMsgRecTypeSetActivity.class);
    intent.putExtra("Flag", flag);
    return intent;
  }

  private void initMsgRecFlag(int flag) {
    switch (flag) {
      case YWProfileSettingsConstants.TRIBE_MSG_REC:
        mTribeMsgRecCheck.setVisibility(View.VISIBLE);
        mTribeMsgRecNotRemindCheck.setVisibility(View.GONE);
        mTribeMsgRejCheck.setVisibility(View.GONE);
        break;
      case YWProfileSettingsConstants.TRIBE_MSG_REC_NOT_REMIND:
        mTribeMsgRecCheck.setVisibility(View.GONE);
        mTribeMsgRecNotRemindCheck.setVisibility(View.VISIBLE);
        mTribeMsgRejCheck.setVisibility(View.GONE);
        break;
      case YWProfileSettingsConstants.TRIBE_MSG_REJ:
        mTribeMsgRecCheck.setVisibility(View.GONE);
        mTribeMsgRecNotRemindCheck.setVisibility(View.GONE);
        mTribeMsgRejCheck.setVisibility(View.VISIBLE);
        break;
    }
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.receive_and_remind_layout:
        flag = YWProfileSettingsConstants.TRIBE_MSG_REC;
        Intent intent1 = new Intent();
        intent1.putExtra("Flag", flag);
        setResult(RESULT_OK, intent1);
        initMsgRecFlag(flag);
        finish();
        break;
      case R.id.only_receive_layout:
        flag = YWProfileSettingsConstants.TRIBE_MSG_REC_NOT_REMIND;
        Intent intent2 = new Intent();
        intent2.putExtra("Flag", flag);
        setResult(RESULT_OK, intent2);
        initMsgRecFlag(flag);
        finish();
        break;
      case R.id.not_receive_layout:
        flag = YWProfileSettingsConstants.TRIBE_MSG_REJ;
        Intent intent3 = new Intent();
        intent3.putExtra("Flag", flag);
        setResult(RESULT_OK, intent3);
        initMsgRecFlag(flag);
        finish();
        break;
    }
  }

  @Override public void onBackPressed() {
    setResult(flag);
    super.onBackPressed();
  }
}
