package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.mobileim.YWAccount;
import com.alibaba.mobileim.channel.constant.YWProfileSettingsConstants;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.YWContactManager;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.utility.IMNotificationUtils;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;

public class ContactSettingActivity extends BaseActivity {

  private ImageView contactHead;
  private TextView contactShowName;
  private SwitchCompat msgRemindSwitch;
  private RelativeLayout clearMsgRecordLayout;
  private String appKey;
  private String userId;
  private int msgRecFlag = YWProfileSettingsConstants.RECEIVE_PEER_MSG;
  private YWAccount account;

  private YWContactManager contactManager;
  private IYWContact contact;

  private IYWConversationService conversationService;
  private YWConversation conversation;
  private Handler uiHandler;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (intent != null) {
      appKey = intent.getStringExtra("AppKey");
      userId = intent.getStringExtra("UserId");
    }
    uiHandler = new Handler(Looper.getMainLooper());
    account = LoginHelper.getInstance().getYWIMKit().getIMCore();
    contactManager = (YWContactManager) LoginHelper.getInstance().getYWIMKit().getContactService();
    contact = contactManager.getContactProfileInfo(userId, appKey);
    conversationService = LoginHelper.getInstance().getYWIMKit().getConversationService();
    conversation = conversationService.getConversationByUserId(userId);
    if (contactManager != null) {
      msgRecFlag = contactManager.getMsgRecFlagForContact(userId, appKey);
    }
    setContentView(R.layout.im_activity_contact_setting);
    initViews();
  }

  public static Intent getContactSettingActivityIntent(Context context, String appKey,
      String userId) {
    Intent intent = new Intent(context, ContactSettingActivity.class);
    intent.putExtra("AppKey", appKey);
    intent.putExtra("UserId", userId);
    return intent;
  }

  private void initViews() {
    contactHead = (ImageView) findViewById(R.id.head);
    contactShowName = (TextView) findViewById(R.id.contact_show_name);
    if (contact != null) {
      contactShowName.setText(contact.getShowName());
    } else {
      contactShowName.setText(userId);
    }

    msgRemindSwitch = (SwitchCompat) findViewById(R.id.receive_msg_remind_switch);
    clearMsgRecordLayout = (RelativeLayout) findViewById(R.id.clear_msg_record);
    if (msgRecFlag != YWProfileSettingsConstants.RECEIVE_PEER_MSG_NOT_REMIND) {
      msgRemindSwitch.setChecked(true);
    } else {
      msgRemindSwitch.setChecked(false);
    }
    msgRemindSwitch.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        setMsgRecType();
      }
    });
    clearMsgRecordLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        clearMsgRecord();
      }
    });
    initTitle();
  }

  private void initTitle() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("聊天设置");
    toolbar.setTitleTextColor(Color.WHITE);
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        finish();
      }
    });
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
  }

  private void setMsgRecType() {
    if (contact == null) {
      return;
    }
    if (msgRecFlag != YWProfileSettingsConstants.RECEIVE_PEER_MSG_NOT_REMIND) {
      contactManager.setContactMsgRecType(contact,
          YWProfileSettingsConstants.RECEIVE_PEER_MSG_NOT_REMIND, 10, new SettingsCallback());
    } else {
      contactManager.setContactMsgRecType(contact, YWProfileSettingsConstants.RECEIVE_PEER_MSG, 10,
          new SettingsCallback());
    }
  }

  class SettingsCallback implements IWxCallback {

    @Override public void onError(int code, String info) {
      
    }

    @Override public void onProgress(int progress) {

    }

    @Override public void onSuccess(Object... result) {
      if (contact != null) {
        msgRecFlag = contactManager.getMsgRecFlagForContact(contact);
      } else {
        msgRecFlag = contactManager.getMsgRecFlagForContact(userId, appKey);
      }
      uiHandler.post(new Runnable() {
        @Override public void run() {
          if (msgRecFlag != YWProfileSettingsConstants.RECEIVE_PEER_MSG_NOT_REMIND) {
            msgRemindSwitch.setChecked(true);
          } else {
            msgRemindSwitch.setChecked(false);
          }
        }
      });
    }
  }

  protected void clearMsgRecord() {
    String message = "清空的消息再次漫游时不会出现。你确定要清空聊天消息吗？";
    //        if (mConversation.getConversationType() == ConversationType.WxConversationType.Room) {
    //            message = getResources().getString(
    //                    R.string.clear_roomchatting_msg_confirm);
    //        }

    MaterialDialog.Builder builder = new MaterialDialog.Builder(ContactSettingActivity.this);
    builder.content(message)
        .cancelable(false)
        .positiveText("确定")
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            conversation.getMessageLoader().deleteAllMessage();
            IMNotificationUtils.showToast("记录已清空", ContactSettingActivity.this);
          }
        })
        .negativeText("取消")
        .onNegative(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
          }
        });
    builder.build().show();
  }
}
