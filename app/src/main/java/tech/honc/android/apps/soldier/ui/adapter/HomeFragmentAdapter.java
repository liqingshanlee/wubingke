package tech.honc.android.apps.soldier.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import tech.honc.android.apps.soldier.ui.fragment.FemaleSoldierFragment;
import tech.honc.android.apps.soldier.ui.fragment.MaleSoldierFragment;

/**
 * Created by MrJiang on 4/14/2016.
 * 主页-战友-军嫂适配器
 */
public class HomeFragmentAdapter extends FragmentPagerAdapter {

  private final static String TAB_TITLE[] = { "战友", "军嫂" };
  private Fragment mMaleFragment;
  private Fragment mFemaleFragment;

  public HomeFragmentAdapter(FragmentManager fm) {
    super(fm);
    mMaleFragment = new MaleSoldierFragment();
    mFemaleFragment = new FemaleSoldierFragment();
  }

  @Override public Fragment getItem(int position) {
    Fragment fragment = null;
    switch (position) {
      case 0:
        fragment = mMaleFragment;
        break;
      case 1:
        fragment = mFemaleFragment;
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
