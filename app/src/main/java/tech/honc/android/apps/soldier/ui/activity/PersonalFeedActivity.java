package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import tech.honc.android.apps.soldier.ui.fragment.PersonalFeedFragment;

/**
 * Created by MrJiang on 2016/4/28.
 * 我的发布
 */
public class PersonalFeedActivity extends BaseSimpleSinglePaneActivity {
  @Override protected Fragment onCreatePane() {
    PersonalFeedFragment feedFragment = PersonalFeedFragment.create();
    Bundle bundle = new Bundle();
    bundle.putParcelable("user", getIntent().getParcelableExtra("user"));
    feedFragment.setArguments(bundle);
    return feedFragment;
  }
}
