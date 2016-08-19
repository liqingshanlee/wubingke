package tech.honc.android.apps.soldier.ui.activity;

import android.support.v4.app.Fragment;
import tech.honc.android.apps.soldier.ui.fragment.PersonalFansFragment;

/**
 * Created by MrJiang on 2016/4/27.
 * 我的粉丝
 */
public class FansActivity extends BaseSimpleSinglePaneActivity {
  @Override protected Fragment onCreatePane() {
    return PersonalFansFragment.create();
  }
}
