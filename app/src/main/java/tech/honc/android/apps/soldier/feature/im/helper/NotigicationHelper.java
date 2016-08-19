package tech.honc.android.apps.soldier.feature.im.helper;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMNotification;

/**
 * Created by wangh on 2016-5-20-0020.
 */
public class NotigicationHelper extends IMNotification {

  private static boolean mNeedQuiet;
  private static boolean mNeedVibrator = true;
  private static boolean mNeedSound = true;

  public NotigicationHelper(Pointcut pointcut) {
    super(pointcut);
  }

  public static void init() {
    YWIMKit imKit = LoginHelper.getInstance().getYWIMKit();
    if (imKit != null) {

      //设置是否开启通知提醒
      imKit.setEnableNotification(true);

      //mNeedSound = (DemoSimpleKVStore.getNeedSound() == 1);
      //mNeedVibrator = (DemoSimpleKVStore.getNeedVibration() == 1);
    }
  }
}
