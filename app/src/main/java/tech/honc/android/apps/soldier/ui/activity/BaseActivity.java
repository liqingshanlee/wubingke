package tech.honc.android.apps.soldier.ui.activity;

import com.smartydroid.android.starter.kit.app.StarterActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by MrJiang on 4/13/2016.
 */
public abstract class BaseActivity extends StarterActivity {
  @Override protected void onResume() {
    super.onResume();
    MobclickAgent.onResume(this);
  }

  @Override protected void onPause() {
    super.onPause();
    MobclickAgent.onPause(this);
  }
}
