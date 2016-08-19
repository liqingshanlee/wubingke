package tech.honc.android.apps.soldier.ui.activity;

import android.support.v4.app.Fragment;
import tech.honc.android.apps.soldier.ui.fragment.PersonalHelperFragment;

/**
 * Created by MrJiang on 2016/4/27.
 * 用户助人列表
 */
public class HelpActivity extends BaseSimpleSinglePaneActivity
{

  @Override protected Fragment onCreatePane() {
    return PersonalHelperFragment.create();
  }
}
