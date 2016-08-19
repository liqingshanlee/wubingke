package tech.honc.android.apps.soldier.ui.activity;

import android.support.v4.app.Fragment;
import tech.honc.android.apps.soldier.ui.fragment.PersonalTaskFragment;

/**
 * Created by MrJiang on 2016/5/3.
 */
public class PersonalTaskActivity extends BaseSimpleSinglePaneActivity {
  @Override protected Fragment onCreatePane() {
    return PersonalTaskFragment.create();
  }
}
