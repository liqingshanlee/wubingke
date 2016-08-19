package tech.honc.android.apps.soldier.ui.activity;

import android.support.v4.app.Fragment;
import tech.honc.android.apps.soldier.ui.fragment.PersonalDetailFragment;

/**
 * Created by MrJiang on 2016/4/26.
 * 个人详情
 */
// TODO: 2016/4/29 界面 deprecated
public class PersonalActivity extends BaseSimpleSinglePaneActivity {
  @Override protected Fragment onCreatePane() {
    return PersonalDetailFragment.create();
  }
}
