package tech.honc.android.apps.soldier.feature.im.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.model.ContactsDummy;

public class ContactsDummyViewHolder extends EasyViewHolder<ContactsDummy> {

  ImageView mAvatarView;
  TextView mTitleTextView;

  public ContactsDummyViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.contacts_item_dummy);
    mAvatarView = ButterKnife.findById(itemView, R.id.image_support_im_contacts_dummy_avatar);
    mTitleTextView = ButterKnife.findById(itemView, R.id.text_support_im_contacts_dummy_text);
  }

  @Override public void bindTo(int position, ContactsDummy value) {
    mAvatarView.setImageResource(value.mDrawableRes);
    mTitleTextView.setText(value.mTextRes);
  }
}
