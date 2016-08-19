package tech.honc.android.apps.soldier.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by MrJiang
 * on 2016/5/9.
 */
public class AppIntroFragment extends Fragment {

  private static final String ARG_LAYOUT_RES_ID = "layoutResId";
  private static final String ARG_DRAWABLE_RES_ID = "drawableResId";
  private int layoutResId;
  private int drawableResId;

  public AppIntroFragment() {
  }


  public static AppIntroFragment newInstance(int layoutResId, int drawableResId) {
    AppIntroFragment sampleSlide = new AppIntroFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
    args.putInt(ARG_DRAWABLE_RES_ID, drawableResId);
    sampleSlide.setArguments(args);
    return sampleSlide;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
      layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
    }
    if (getArguments() != null && getArguments().containsKey(ARG_DRAWABLE_RES_ID)) {
      drawableResId = getArguments().getInt(ARG_DRAWABLE_RES_ID);
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(layoutResId, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ImageView imageView = (ImageView) view;
    imageView.setImageResource(drawableResId);
  }
}
