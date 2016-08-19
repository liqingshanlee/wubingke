package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import tech.honc.android.apps.soldier.ui.fragment.UserFeedFragment;

/**
 * Created by MrJiang on 2016/4/28.
 * 别人的 动态
 */
public class UserFeedActivity extends BaseSimpleSinglePaneActivity {

  @Override protected Fragment onCreatePane() {
    UserFeedFragment mBaseFragment = UserFeedFragment.create();
    Bundle bundle = new Bundle();
    bundle.putParcelable("user", getIntent().getParcelableExtra("user"));
    mBaseFragment.setArguments(bundle);
    return mBaseFragment;
  }
}
