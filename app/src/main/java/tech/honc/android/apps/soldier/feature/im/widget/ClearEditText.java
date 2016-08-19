package tech.honc.android.apps.soldier.feature.im.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wangh on 2016-3-22-0022.
 */
public class ClearEditText extends AppCompatEditText
    implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {
  private Drawable mClearTextIcon;
  private OnFocusChangeListener mOnFocusChangeListener;
  private OnTouchListener mOnTouchListener;

  public ClearEditText(Context context) {
    super(context);
    init(context);
  }

  public ClearEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    Drawable drawable = ContextCompat.getDrawable(context,
        android.support.v7.appcompat.R.drawable.abc_ic_clear_mtrl_alpha);
    Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
    DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
    mClearTextIcon = wrappedDrawable;
    mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicHeight(),
        mClearTextIcon.getIntrinsicWidth());
    setClearIconVisible(false);
    super.setOnTouchListener(this);
    super.setOnFocusChangeListener(this);
    //addTextChangedListener(this);

  }

  private void setClearIconVisible(boolean isShow) {
    mClearTextIcon.setVisible(isShow, false);
    Drawable[] compoundDrawables = getCompoundDrawables();
    setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], isShow ? mClearTextIcon : null,
        compoundDrawables[3]);
  }

  @Override public void onFocusChange(View v, boolean hasFocus) {
    if (hasFocus) {
      setClearIconVisible(getText().length() > 0);
    } else {
      setClearIconVisible(false);
    }
    if (mOnFocusChangeListener != null) {
      mOnFocusChangeListener.onFocusChange(v, hasFocus);
    }
  }

  @Override public boolean onTouch(View v, MotionEvent event) {
    int x = (int) event.getX();
    if (mClearTextIcon.isVisible()
        && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
      if (event.getAction() == event.ACTION_UP) {
        setText("");
      }
      return true;
    }
    return mOnTouchListener != null && mOnTouchListener.onTouch(v, event);
  }

  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    if (isFocused()) {
      setClearIconVisible(text.length() > 0);
    }
  }

  @Override public void afterTextChanged(Editable s) {

  }

  @Override
  public void setOnFocusChangeListener(final OnFocusChangeListener onFocusChangeListener) {
    mOnFocusChangeListener = onFocusChangeListener;
  }

  @Override public void setOnTouchListener(final OnTouchListener onTouchListener) {
    mOnTouchListener = onTouchListener;
  }
}
