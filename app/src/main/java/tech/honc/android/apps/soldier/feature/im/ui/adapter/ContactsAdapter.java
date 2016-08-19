package tech.honc.android.apps.soldier.feature.im.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import java.util.ArrayList;
import java.util.List;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.model.Contact;

public class ContactsAdapter extends EasyRecyclerAdapter
    implements StickyRecyclerHeadersAdapter<EasyViewHolder> {

  public ContactsAdapter(Context context) {
    super(context);
  }

  public int getPositionForSection(char section) {
    for (int index = 0; index < getItemCount(); index++) {
      final Object object = get(index);
      if (object instanceof Contact) {
        Contact contact = (Contact) object;
        char firstChar = contact.letterChar;
        if (firstChar == section) {
          return index;
        }
      }
    }
    return RecyclerView.NO_POSITION;
  }

  public List<String> getSortLetter() {
    List<String> sortList = null;
    if (getItems() != null) {
      sortList = new ArrayList<>();
      List<Object> list = getItems();
      for (int i = 0; i < list.size(); i++) {
        Object o = list.get(i);
        if (o instanceof Contact) {
          Contact contact = (Contact) o;
          char firstChar = contact.letterChar;
          if (!sortList.contains(String.valueOf(firstChar))) {
            sortList.add(String.valueOf(firstChar));
          }
        }
      }
      return sortList;
    } else {
      return null;
    }
  }

  @Override public long getHeaderId(int position) {
    final Object object = get(position);
    if (object instanceof Contact) {
      Contact contact = (Contact) object;
      return contact.letterChar;
    }
    return RecyclerView.NO_POSITION;
  }

  @Override public EasyViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
    return new EasyViewHolder(parent.getContext(), parent, R.layout.header) {
      @Override public void bindTo(int position, Object value) {
      }
    };
  }

  @Override public void onBindHeaderViewHolder(EasyViewHolder holder, int position) {
    TextView textView = (TextView) holder.itemView;
    final Object object = get(position);
    if (object instanceof Contact) {
      Contact contact = (Contact) object;
      String showValue = String.valueOf(contact.letterChar);
      textView.setText(showValue);
    }
  }
}
