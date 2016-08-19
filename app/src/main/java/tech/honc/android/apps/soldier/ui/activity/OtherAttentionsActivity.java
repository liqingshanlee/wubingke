package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import com.smartydroid.android.starter.kit.app.SimpleSinglePaneActivity;
import tech.honc.android.apps.soldier.ui.fragment.OtherAttentionsFragment;

/**
 * Created by MrJiang on 2016/5/16.
 * 别人的关注
 */
public class OtherAttentionsActivity extends SimpleSinglePaneActivity {
  @Override protected Fragment onCreatePane() {
    OtherAttentionsFragment otherAttentionsFragment = OtherAttentionsFragment.newInstance();
    Bundle bundle = new Bundle();
    bundle.putInt("id", getIntent().getIntExtra("id", 0));
    otherAttentionsFragment.setArguments(bundle);
    return otherAttentionsFragment;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActionBar bar = getSupportActionBar();
    assert bar != null;
    bar.setDisplayHomeAsUpEnabled(true);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }
}
