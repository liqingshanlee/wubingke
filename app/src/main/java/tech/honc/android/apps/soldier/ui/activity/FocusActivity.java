package tech.honc.android.apps.soldier.ui.activity;

import android.support.v4.app.Fragment;
import tech.honc.android.apps.soldier.ui.fragment.PersonalFocusFragment;

/**
 * Created by MrJiang on 2016/4/27.
 */
public class FocusActivity  extends BaseSimpleSinglePaneActivity
{

  @Override protected Fragment onCreatePane() {
    return PersonalFocusFragment.create();
  }
}
