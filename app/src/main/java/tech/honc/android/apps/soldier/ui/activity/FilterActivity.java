package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import butterknife.Bind;
import butterknife.ButterKnife;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.ui.adapter.FilterAdapter;
import tech.honc.android.apps.soldier.ui.app.SoldierApp;

/**
 * Created by MrJiang on 4/15/2016.
 * 筛选容器
 */
public class FilterActivity extends BaseActivity {

  @Bind(R.id.fragment_tab) TabLayout mFragmentTab;
  @Bind(R.id.fragment_viewpager) ViewPager mFragmentViewpager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_filter);
    ButterKnife.bind(this);
    defaultAttributes();
    setUpTab();
  }

  private void defaultAttributes() {
    mFragmentTab.setBackgroundColor(SoldierApp.color(R.color.white));
  }

  private void setUpTab() {
    FilterAdapter mFilterAdapter = new FilterAdapter(getSupportFragmentManager());
    mFragmentViewpager.setAdapter(mFilterAdapter);
    mFragmentTab.setupWithViewPager(mFragmentViewpager);
    mFragmentTab.setTabMode(TabLayout.MODE_FIXED);
  }
}
