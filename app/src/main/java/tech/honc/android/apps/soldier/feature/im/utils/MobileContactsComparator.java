package tech.honc.android.apps.soldier.feature.im.utils;

import java.util.Comparator;
import tech.honc.android.apps.soldier.feature.im.model.MobileContacts;
import tech.honc.android.apps.soldier.utils.toolsutils.PinyinUtils;

/**
 * Created by MrJiang on 2016/6/3.
 * 手机通讯录比较
 */
public class MobileContactsComparator implements Comparator<MobileContacts> {
  @Override public int compare(MobileContacts lhs, MobileContacts rhs) {
    String a = PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(lhs.nickname));
    String b = PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(rhs.nickname));
    return a.compareTo(b);
  }
}
