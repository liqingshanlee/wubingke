package tech.honc.android.apps.soldier.utils.settings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.math.BigDecimal;
import tech.honc.android.apps.soldier.ui.app.SoldierApp;

/**
 * Created by wangh on 2016-4-22-0022.
 */
public class VersionUtil
{
  public static int getVersionCode() {
    PackageInfo info = null;
    try {
      info = SoldierApp.appContext()
          .getPackageManager()
          .getPackageInfo(SoldierApp.appContext().getPackageName(), 0);
      // 当前应用的版本名称  
      //String versionName = info.versionName;
      // 当前版本的版本号  
      //int versionCode = info.versionCode;
      return info.versionCode;
      // 当前版本的包名  
      //String packageNames = info.packageName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static String getVersionName() {
    PackageInfo info = null;
    try {
      info = SoldierApp.appContext()
          .getPackageManager()
          .getPackageInfo(SoldierApp.appContext().getPackageName(), 0);
      // 当前应用的版本名称  
      return info.versionName;
      // 当前版本的版本号  
      //int versionCode = info.versionCode;
      //return info.versionCode;
      // 当前版本的包名  
      //String packageNames = info.packageName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String bytes2kb(long bytes) {
    BigDecimal filesize = new BigDecimal(bytes);
    BigDecimal megabyte = new BigDecimal(1024 * 1024);
    float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
    if (returnValue > 1) return (returnValue + "MB");
    BigDecimal kilobyte = new BigDecimal(1024);
    returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
    return (returnValue + "KB");
  }
}
