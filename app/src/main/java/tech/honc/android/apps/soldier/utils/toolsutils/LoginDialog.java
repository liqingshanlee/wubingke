package tech.honc.android.apps.soldier.utils.toolsutils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import tech.honc.android.apps.soldier.Navigator;

/**
 * Created by MrJiang on 2016/5/18.
 */
public class LoginDialog {
  /**
   * 显示登陆dialog
   */

  private static LoginDialog mLoginDialog;

  private LoginDialog() {
  }

  public static LoginDialog getInstance() {
    if (mLoginDialog == null) {
      synchronized (LoginDialog.class) {
        mLoginDialog = new LoginDialog();
      }
    }
    return mLoginDialog;
  }

  private Activity mActivity;
  AlertDialog.Builder mBuilder;

  public void init(Activity activity) {
    mActivity = activity;
    mBuilder = new AlertDialog.Builder(mActivity);
    mBuilder.setTitle("温馨提示");
    mBuilder.setMessage("亲,你还没有登陆哦");
    mBuilder.setPositiveButton("去登陆", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        Navigator.startFirstLoginActivity(mActivity);
        mActivity = null;
      }
    });
    mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
  }

  public void showDialog() {
    mBuilder.create().show();
  }
}
