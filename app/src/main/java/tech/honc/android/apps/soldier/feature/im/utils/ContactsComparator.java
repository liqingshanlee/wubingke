package tech.honc.android.apps.soldier.feature.im.utils;

import java.util.Comparator;
import tech.honc.android.apps.soldier.feature.im.model.Contact;
import tech.honc.android.apps.soldier.utils.toolsutils.PinyinUtils;

/**
 * Created by MrJiang on 2016/6/3.
 * 手机通讯录比较
 */
public class ContactsComparator implements Comparator<Contact> {
  @Override public int compare(Contact lhs, Contact rhs) {
    String a = PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(lhs.nickName));
    String b = PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(rhs.nickName));
    return a.compareTo(b);
  }
}
