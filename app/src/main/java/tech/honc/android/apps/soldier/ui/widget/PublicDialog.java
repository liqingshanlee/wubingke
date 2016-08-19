package tech.honc.android.apps.soldier.ui.widget;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Administrator on 2016/4/19.
 */
public class PublicDialog extends Dialog {

  Context context;

  public PublicDialog(Context context) {
    super(context);
    this.context = context;
  }

  public PublicDialog(Context context, int theme, int layout) {
    super(context, theme);
    this.context = context;
    this.setContentView(layout);
  }
}
