package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.utils.settings.SettingItems;

/**
 * Created by MrJiang on 4/17/2016.
 * 单行文本
 */
public class TextViewHolder extends BaseViewHolder {
  @Bind(R.id.text_signature) TextView mTextSignature;
  @Bind(R.id.title_signature_icon) ImageView mTitleSignatureIcon;
  @Bind(R.id.title_signature_head) TextView mTitleSignatureHead;

  public TextViewHolder(Context context, ViewGroup parent) {
    super(context,
        LayoutInflater.from(context).inflate(R.layout.list_item_detail_signature, parent, false));
    ButterKnife.bind(this, itemView);
  }

  @Override protected void bindTo(SettingItems settingItem) {
    super.bindTo(settingItem);
    if (settingItem != null) {
      mTextSignature.setText(settingItem.mValue);
      mTitleSignatureIcon.setImageResource(settingItem.mIcon);
      mTitleSignatureHead.setText(settingItem.mContent);
    }
  }
}
