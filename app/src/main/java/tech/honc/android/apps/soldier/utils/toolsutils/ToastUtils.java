package tech.honc.android.apps.soldier.utils.toolsutils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import tech.honc.android.apps.soldier.R;

/**
 * Created by MrJiang on 4/22/2016.
 */
public class ToastUtils {
  public static Toast mToast;

  /**
   * 显示Toast
   */
  public static void showToast(final Activity context, final String message) {
    LayoutInflater inflater = context.getLayoutInflater();
    View layout = inflater.inflate(R.layout.item_toast_custom, null);
    TextView textView = (TextView) layout.findViewById(R.id.tv_toast);
    textView.setText(message);
    if (mToast == null) {
      mToast = new Toast(context.getApplicationContext());
    }
    mToast.setView(layout);
    mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 300);
    mToast.setDuration(Toast.LENGTH_SHORT);
    mToast.show();
  }

  /**
   * 显示Toast
   */
  public static void showToast(final Context context, final int messageResId) {
    if (mToast == null) {
      mToast = Toast.makeText(context, messageResId, Toast.LENGTH_SHORT);
    } else {
      mToast.setText(messageResId);
      mToast.setDuration(Toast.LENGTH_SHORT);
    }
    mToast.show();
  }
}
