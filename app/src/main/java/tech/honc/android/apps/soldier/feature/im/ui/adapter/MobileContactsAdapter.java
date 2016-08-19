package tech.honc.android.apps.soldier.feature.im.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.model.MobileContacts;
import tech.honc.android.apps.soldier.feature.im.ui.viewholder.InviteContactViewHolder;
import tech.honc.android.apps.soldier.ui.appInterface.ContactOnItemClickListener;
import tech.honc.android.apps.soldier.utils.toolsutils.PinyinUtils;

/**
 * Created by MrJiang on 2016/6/6.
 */
public class MobileContactsAdapter extends RecyclerView.Adapter<InviteContactViewHolder> {

  public Context mContext;
  public ArrayList<MobileContacts> mMobileContactses;
  public LayoutInflater mLayoutInflater;
  public ContactOnItemClickListener mContactOnItemClickListener;

  public MobileContactsAdapter(Context context, ArrayList<MobileContacts> mobileContactses) {
    this.mContext = context;
    mContactOnItemClickListener = (ContactOnItemClickListener) context;
    this.mMobileContactses = mobileContactses;
    mLayoutInflater = LayoutInflater.from(context);
  }

  @Override public InviteContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = mLayoutInflater.inflate(R.layout.list_item_invite_contacts, parent, false);
    return new InviteContactViewHolder(mContext, view);
  }

  @Override public void onBindViewHolder(InviteContactViewHolder holder,
      @SuppressLint("RecyclerView") final int position) {
    holder.mImSimpleAvatar.setImageURI(mMobileContactses.get(position).uri());
    holder.mImTextUsername.setText(mMobileContactses.get(position).nickname);
    holder.mImTextMobile.setText(mMobileContactses.get(position).mobile);
    if (mMobileContactses.get(position).hasAccount) {
      holder.mImTextInvite.setText("已邀请");
    } else {
      holder.mImTextInvite.setText("邀请");
    }
    if (position > 0)
    {
      String currentLetter = PinyinUtils.getFirstLetter(
          PinyinUtils.getPinYin(mMobileContactses.get(position).nickname));
      String oldLetter = PinyinUtils.getFirstLetter(
          PinyinUtils.getPinYin(mMobileContactses.get(position - 1).nickname));
      if (!oldLetter.equals(currentLetter)) {
        holder.mImTextLetter.setVisibility(View.VISIBLE);
        holder.mImTextLetter.setText(currentLetter);
      } else {
        holder.mImTextLetter.setVisibility(View.GONE);
      }
    }
    else {
      String letter = PinyinUtils.getFirstLetter(
          PinyinUtils.getPinYin(mMobileContactses.get(position).nickname));
      holder.mImTextLetter.setVisibility(View.VISIBLE);
      holder.mImTextLetter.setText(letter);
    }

    holder.mRelaytiveLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mContactOnItemClickListener.onItemClick(mMobileContactses.get(position));
      }
    });
  }

  @Override public int getItemCount() {
    return mMobileContactses.size();
  }

  public int getPositionForSection(char section) {
    for (int index = 0; index < getItemCount(); index++) {
      final Object object = mMobileContactses.get(index);
      if (object != null) {
        MobileContacts contact = (MobileContacts) object;
        char firstChar =
            PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(contact.nickname)).charAt(0);
        if (firstChar == section) {
          return index;
        }
      }
    }
    return RecyclerView.NO_POSITION;
  }
}
