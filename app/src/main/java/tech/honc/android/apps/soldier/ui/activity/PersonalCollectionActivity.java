package tech.honc.android.apps.soldier.ui.activity;

import android.support.v4.app.Fragment;
import tech.honc.android.apps.soldier.ui.fragment.PersonalCollectionFragment;

/**
 * Created by MrJiang on 2016/5/3.
 * 我的收藏
 */
public class PersonalCollectionActivity extends BaseSimpleSinglePaneActivity {
  @Override protected Fragment onCreatePane() {
    return PersonalCollectionFragment.create();
  }
}
