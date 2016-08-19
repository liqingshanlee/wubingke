package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.BeanCertification;

/**
 * Created by kevin on 2016/6/23.
 */
public class CertificationViewHolder extends EasyViewHolder<BeanCertification> {
  @Bind(R.id.ui_text_title) TextView mUiTextTitle;
  @Bind(R.id.ui_text_value) TextView mUiTextValue;

  public CertificationViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_certification);
    ButterKnife.bind(this, itemView);
  }

  @Override public void bindTo(int position, BeanCertification value) {
    mUiTextTitle.setText(value.title);
    mUiTextValue.setText(value.value);
  }
}
