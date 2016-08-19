package tech.honc.android.apps.soldier.ui.activity;

import android.support.v4.app.Fragment;
import tech.honc.android.apps.soldier.ui.fragment.SoldierDetailFragment;

/**
 * Created by MrJiang on 4/17/2016.
 */
public class SoldierDetailActivity extends BaseSimpleSinglePaneActivity {
  @Override protected Fragment onCreatePane() {
    return SoldierDetailFragment.create();
  }

}
