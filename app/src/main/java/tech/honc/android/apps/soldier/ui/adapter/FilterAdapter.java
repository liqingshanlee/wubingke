package tech.honc.android.apps.soldier.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import tech.honc.android.apps.soldier.ui.fragment.FilterFemaleSoldierFragment;
import tech.honc.android.apps.soldier.ui.fragment.FilterSoldierFragment;

/**
 * Created by MrJiang on 4/15/2016.
 * 筛选adapter
 */
public class FilterAdapter extends FragmentPagerAdapter {
  private Fragment mFilterMaleFragment;
  private Fragment mFilterFemaleFragment;
  private final static String TAB_TITLE[] = { "战友", "军嫂" };

  public FilterAdapter(FragmentManager fm) {
    super(fm);
    mFilterMaleFragment = new FilterSoldierFragment();
    mFilterFemaleFragment = new FilterFemaleSoldierFragment();
  }

  @Override public Fragment getItem(int position) {
    Fragment fragment = null;
    switch (position) {
      case 0:
        fragment = mFilterMaleFragment;
        break;
      case 1:
        fragment = mFilterFemaleFragment;
        break;
    }
    return fragment;
  }

  @Override public int getCount() {
    return TAB_TITLE.length;
  }

  @Override public CharSequence getPageTitle(int position) {
    return TAB_TITLE[position];
  }
}
