package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import butterknife.Bind;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import com.smartydroid.android.starter.kit.account.AccountManager;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.feature.im.widget.ClearEditText;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by wangh on 2016-3-22-0022.
 */
public class SendAddContactRequestActivity extends BaseActivity implements View.OnClickListener {

  @Bind(R.id.et_clear) ClearEditText mClearEditText;
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.btn_send) TextView mSendReq;

  public static final int SEND_MSG_COMPLETED = 201;
  public static final int CANCEL_SEND_MSG = 202;

  private String mTargetId;
  private User mUser;
  private String mMsg;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_add_request);
    initData();
    initTitle();
  }

  private void initTitle() {
    initToolbar();
    mSendReq.setOnClickListener(this);
  }

  private void initToolbar() {
    mToolbar.setTitle("好友验证");
    mToolbar.setTitleTextColor(Color.WHITE);
    mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    setSupportActionBar(mToolbar);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        setResult(RESULT_CANCELED);
        //SendAddContactRequestActivity.this.finishActivity(SearchUserResultActivity.REQ_ADD_FRI);
        finish();
      }
    });
  }

  @SuppressLint("SetTextI18n") private void initData() {
    mTargetId = getIntent().getStringExtra("id");
    mUser = AccountManager.getCurrentAccount();
    mClearEditText.setText("我是" + mUser.nickname);
    int nameLength = mUser.nickname.length();
    mClearEditText.setSelection(2, nameLength + 2);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
  }

  /**
   * @param targetId 添加对象的id
   * @param appKey 添加对象的appkey
   * @param nickName 添加对象的备注
   * @param msg 添加对象的发送描述内容
   * @param callback 请求回调
   */
  private void sendAddContactRequest(String targetId, String appKey, String nickName, String msg,
      IWxCallback callback) {
    getContactService().addContact(targetId, appKey, nickName, msg, callback);
  }

  private IYWContactService getContactService() {
    return LoginHelper.getInstance().getYWIMKit().getContactService();
  }

  @Override public void onClick(View v) {
    mMsg = mClearEditText.getText().toString();
    if (TextUtils.isEmpty(mMsg)) {
      sendAddContactRequest(mTargetId, getContactService().getWXIMContact(mTargetId).getAppKey(),
          getContactService().getWXIMContact(mTargetId).getShowName(), "请求添加你为好友", callback);
    } else {
      sendAddContactRequest(mTargetId, getContactService().getWXIMContact(mTargetId).getAppKey(),
          getContactService().getWXIMContact(mTargetId).getShowName(), mMsg, callback);
    }
  }

  private IWxCallback callback = new IWxCallback() {
    @Override public void onSuccess(Object... objects) {
      SnackBarUtil.showText(SendAddContactRequestActivity
          .this, "好友请求发送成功");
      setResult(RESULT_OK);
      SendAddContactRequestActivity.this.finish();
    }

    @Override public void onError(int i, String s) {
      SnackBarUtil.showText(SendAddContactRequestActivity
          .this, "好友请求发送失败，请检查网络连接");
    }

    @Override public void onProgress(int i) {

    }
  };
}
