package tech.honc.android.apps.soldier.feature.im.helper;

import android.content.Intent;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMNotification;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWMessage;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.ui.app.SoldierApp;

/**
 * Created by wangh on 2016-3-7-0007
 *
 * 初始化自定义通知栏UI.
 */
public class InitNotificationHelper extends IMNotification {
  public InitNotificationHelper(Pointcut pointcut) {
    super(pointcut);
  }

  private static boolean mNeedQuiet;
  private static boolean mNeedVibrator = true;
  private static boolean mNeedSound = true;

  public static void init() {
    YWIMKit imKit = LoginHelper.getInstance().getYWIMKit();
    if (imKit != null) {
      //设置是否开启通知提醒
      imKit.setEnableNotification(true);
      mNeedSound = true;
      mNeedVibrator = true;
      //mNeedSound = (ConfigStore.getNeedSound() == 1);
      //mNeedVibrator = (ConfigStore.getNeedVibration() == 1);
    }
  }

  @Override public String getNotificationTips(YWConversation conversation, YWMessage message,
      int totalUnReadCount) {
    YWConversationType type = conversation.getConversationType();
    if (totalUnReadCount == 0) {
      return null;
    } else {
      if (type == YWConversationType.Tribe) {
        if (message.toString().toString().contains("")) {
        }
        return "您有" + totalUnReadCount + "条未读群组消息";
      } else if (type == YWConversationType.P2P) {
        if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS) {
          return "您有" + totalUnReadCount + "条未读通知";
        } else {
          return "您有" + totalUnReadCount + "条未读消息";
        }
      } else if (type == YWConversationType.Custom) {
        return "收到" + totalUnReadCount + "条系统消息";
      }
      return "您有" + totalUnReadCount + "条未读消息";
    }
  }

  /**
   * 自定义通知栏ticker
   *
   * @param conversation 收到消息的会话
   * @param message 收到的消息
   * @param totalUnReadCount 会话中消息未读数
   * @return 如果返回null，则使用SDK默认的ticker
   */
  @Override public String getTicker(YWConversation conversation, YWMessage message,
      int totalUnReadCount) {
    YWConversationType conversationType = conversation.getConversationType();

    //return "123456";
    return null;
  }

  @Override public boolean needQuiet(YWConversation conversation, YWMessage message) {
    return mNeedQuiet;
  }

  @Override public boolean needVibrator(YWConversation conversation, YWMessage message) {
    return mNeedVibrator;
  }

  @Override public boolean needSound(YWConversation conversation, YWMessage message) {
    return mNeedSound;
  }

  @Override
  public Intent getCustomNotificationIntent(YWConversation conversation, YWMessage message,
      int totalUnReadCount) {
    return null;
  }

  @Override public Intent getCustomNotificationIntent(Intent intent, YWConversation conversation,
      YWMessage message, int totalUnReadCount) {
    return null;
  }

  @Override public int getNotificationIconResID() {
    return R.mipmap.ic_launcher;
  }

  @Override public String getAppName() {
    return SoldierApp.appContext().getString(R.string.app_name);
  }

  @Override public int getNotificationSoundResId() {
    return 0;
  }
}
