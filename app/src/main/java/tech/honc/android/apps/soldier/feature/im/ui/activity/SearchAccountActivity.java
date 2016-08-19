package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.support.v4.app.Fragment;
import tech.honc.android.apps.soldier.feature.im.ui.fragment.SearchOpenIMFragment;
import tech.honc.android.apps.soldier.ui.activity.BaseSimpleSinglePaneActivity;

/**
 * Created by kevin on 16-5-29.
 * 搜索用户
 */
public class SearchAccountActivity extends BaseSimpleSinglePaneActivity {

  @Override protected Fragment onCreatePane() {
    return SearchOpenIMFragment.newInstance();
  }
}
