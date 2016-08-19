package tech.honc.android.apps.soldier.feature.im.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import support.ui.adapters.EasyRecyclerAdapter;
import tech.honc.android.apps.soldier.feature.im.model.MobileContacts;
import tech.honc.android.apps.soldier.utils.toolsutils.PinyinUtils;

/**
 * Created by kevin on 16-6-4.
 */
public class ContactMemberAdapter extends EasyRecyclerAdapter {
  public ContactMemberAdapter(Context context) {
    super(context);
  }

  public int getPositionForSection(char section) {
    for (int index = 0; index < getItemCount(); index++) {
      final Object object = get(index);
      if (object instanceof MobileContacts) {
        MobileContacts contact = (MobileContacts) object;
        char firstChar = 'a';
        String letter = PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(contact.nickname));
        if (letter.length() != 0) firstChar = letter.charAt(0);
        if (firstChar == section) {
          return index;
        }
      }
    }
    return RecyclerView.NO_POSITION;
  }
}
