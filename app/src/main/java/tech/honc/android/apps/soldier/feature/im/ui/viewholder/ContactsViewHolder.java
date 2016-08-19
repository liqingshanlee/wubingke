package tech.honc.android.apps.soldier.feature.im.ui.viewholder;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.model.Contact;

public class ContactsViewHolder extends EasyViewHolder<Contact> {

  SimpleDraweeView mAvatarView;
  TextView mNicknameTextView;
  Contact contact;

  public ContactsViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.contacts_item);
    mAvatarView = ButterKnife.findById(itemView, R.id.image_support_im_contacts_avatar);
    mNicknameTextView =
        ButterKnife.findById(itemView, R.id.text_support_im_contacts_nickname);
  }

  @Override public void bindTo(int position, Contact value) {
    contact = value;
    if (!TextUtils.isEmpty(value.avatar)) {
      mAvatarView.setImageURI(Uri.parse(value.avatar));
    }
    mNicknameTextView.setText(value.nickName);
  }
}
