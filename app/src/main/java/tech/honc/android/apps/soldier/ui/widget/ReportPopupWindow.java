package tech.honc.android.apps.soldier.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import com.smartydroid.android.starter.kit.utilities.ScreenUtils;

/**
 * Created by MrJiang on 5/9/2016.
 */
public class ReportPopupWindow {

  protected Context mContext;
  protected PopupWindow mWindow;
  protected View mRootView;
  protected Drawable mBackground = null;
  protected WindowManager mWindowManager;

  /**
   * Constructor.
   *
   * @param context Context
   */
  public ReportPopupWindow(Context context) {
    mContext = context;
    mWindow = new PopupWindow(context);

    mWindow.setTouchInterceptor(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
          mWindow.dismiss();
          return true;
        }
        return false;
      }
    });

    mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
  }

  /**
   * On dismiss
   */
  protected void onDismiss() {
  }

  /**
   * On show
   */
  public void onShow() {
    int w = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
    mWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
  }

  /**
   * On pre show
   */
  public void preShow() {
    if (mRootView == null) {
      throw new IllegalStateException("setContentView was not called with a view to display.");
    }

    if (mBackground == null) {
      mBackground = new ColorDrawable(0000000000);
      mWindow.setBackgroundDrawable(mBackground);
    } else {
      mWindow.setBackgroundDrawable(mBackground);
    }
    //mWindow.setAnimationStyle(R.style.AnimationPreview);
    mWindow.setWidth(ScreenUtils.dp2px(200));
    mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
    mWindow.setTouchable(true);
    mWindow.setFocusable(true);
    mWindow.setOutsideTouchable(true);
    mWindow.setContentView(mRootView);
    if (!mWindow.isShowing()) {
      onShow();
    }
  }

  /**
   * Set background drawable.
   *
   * @param background Background drawable
   */
  public void setBackgroundDrawable(Drawable background) {
    mBackground = background;
  }

  /**
   * Set content view.
   *
   * @param root Root view
   */
  public void setContentView(View root) {
    mRootView = root;
    mWindow.setContentView(root);
  }

  /**
   * Set content view.
   *
   * @param layoutResID Resource id
   */
  public void setContentView(int layoutResID) {
    LayoutInflater inflator =
        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    setContentView(inflator.inflate(layoutResID, null));
  }

  /**
   * Set listener on window dismissed.
   */
  public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
    mWindow.setOnDismissListener(listener);
  }

  /**
   * Dismiss the popup window.
   */
  public void dismiss() {
    mWindow.dismiss();
  }

  /**
   * get view
   *
   * @return view
   */
  public View getView() {
    return mRootView;
  }
}
