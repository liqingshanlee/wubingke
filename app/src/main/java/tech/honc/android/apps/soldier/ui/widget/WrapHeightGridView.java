package tech.honc.android.apps.soldier.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by MrJiang on 2016/4/25.
 */
public class WrapHeightGridView extends GridView {

  public WrapHeightGridView(Context context) {
    this(context, null);
  }

  public WrapHeightGridView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public WrapHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, heightSpec);
  }
}
