package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWDBContact;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;
import support.ui.app.SupportApp;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.feature.im.model.IMProfileInfo;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;
import tech.honc.android.apps.soldier.utils.toolsutils.Md5Util;

/**
 * Created by kevin on 16-5-30.
 * 添加好友
 */
public class AddFriendActivity extends BaseActivity {

  public static final int REQUEST_CODE = 100;
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.img_avatar) SimpleDraweeView mImgAvatar;
  @Bind(R.id.user_nick_name) TextView mUserNickName;
  @Bind(R.id.user_phone_number) TextView mUserPhoneNumber;
  @Bind(R.id.btn_add_or_send) AppCompatButton mBtnAddOrSend;
  private IMProfileInfo mIMProfileInfo;
  private List<IYWDBContact> contactsFromCache;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_friend);
    ButterKnife.bind(this);
    initIntendData();
    initView();
  }

  private void initView() {
    mToolbar.setTitleTextColor(Color.WHITE);
    mToolbar.setTitle("详细资料");
    mToolbar.setNavigationIcon(SupportApp.drawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    if (mIMProfileInfo != null) {
      mImgAvatar.setImageURI(Uri.parse(mIMProfileInfo.icon));
      mUserNickName.setText("用户名: " + mIMProfileInfo.nick);
      mUserPhoneNumber.setText("手机号: " + mIMProfileInfo.mobile);
      //判断是否为好友,如果是，显示发送消息，如果不是，显示加好友
      if (isHasFriend(mIMProfileInfo.userId)) {
        mBtnAddOrSend.setText("发消息");
      } else {
        mBtnAddOrSend.setText("加好友");
      }
    }
  }

  private void initIntendData() {
    mIMProfileInfo = getIntent().getParcelableExtra("profile");
  }

  /**
   * 是否已经是好友
   *
   * @param targetId 目标用户的openIM MD5 ID编号
   */
  private boolean isHasFriend(String targetId) {
    contactsFromCache = getContactService().getContactsFromCache();
    for (IYWDBContact contact : contactsFromCache) {
      if (contact.getUserId().equals(targetId)) {
        return true;
      }
    }
    return false;
  }

  private IYWContactService getContactService() {
    return LoginHelper.getInstance().getYWIMKit().getContactService();
  }

  @OnClick(R.id.btn_add_or_send) public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_add_or_send:
        if (isHasFriend(mIMProfileInfo.userId)) {
          //跳转到发送消息界面
          Intent intent = LoginHelper.getInstance()
              .getYWIMKit()
              .getChattingActivityIntent(mIMProfileInfo.userId,
                  getContactService().getWXIMContact(mIMProfileInfo.userId).getAppKey());
          if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
          }
        } else {
          //添加好友请求
          Navigator.startSendAddContactRequestActivity(AddFriendActivity.this, Md5Util.encode(mIMProfileInfo.mobile));
        }
        break;
    }
  }
}
