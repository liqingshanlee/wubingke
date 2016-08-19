package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import tech.honc.android.apps.soldier.feature.im.ui.fragment.TribeFragment;
import tech.honc.android.apps.soldier.ui.activity.BaseSimpleSinglePaneActivity;

/**
 * Created by wangh on 2016-3-23-0023.
 */
public class TribeActivity extends BaseSimpleSinglePaneActivity {
  @Override protected Fragment onCreatePane() {
    return new TribeFragment();
  }

  public static void startGroup(Activity activity) {
    Intent intent = new Intent(activity, TribeActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    activity.startActivity(intent);
  }
}
