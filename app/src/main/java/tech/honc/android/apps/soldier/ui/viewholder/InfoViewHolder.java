package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.AccountDetail;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.utils.settings.SettingItems;

/**
 * Created by MrJiang on 4/17/2016.
 * 基本信息
 */
public class InfoViewHolder extends BaseViewHolder {
  @Bind(R.id.detail_name) TextView mDetailName;
  @Bind(R.id.detail_age) TextView mDetailAge;
  @Bind(R.id.detail_username) TextView mDetailUsername;
  @Bind(R.id.detail_sex) TextView mDetailSex;
  @Bind(R.id.detail_area) TextView mDetailArea;
  @Bind(R.id.detail_educational) TextView mDetailEducational;
  @Bind(R.id.detail_height) TextView mDetailHeight;
  @Bind(R.id.detail_weight) TextView mDetailWeight;
  @Bind(R.id.detail_marry) TextView mDetailMarry;

  public InfoViewHolder(Context context, ViewGroup parent) {
    super(context,
        LayoutInflater.from(context).inflate(R.layout.list_item_base_infomation, parent, false));
    ButterKnife.bind(this, itemView);
  }

  @Override protected void bindTo(SettingItems settingItem) {
    super.bindTo(settingItem);
    User user = (User) settingItem.mData;
    if (user.accountDetail != null) {
      AccountDetail detail = user.accountDetail;
      mDetailName.setText(detail.realName != null ? detail.realName : "");
      mDetailAge.setText(detail.age != null ? detail.age + "" : "");
      mDetailUsername.setText(user.nickname != null ? user.nickname : "");
      mDetailSex.setText(user.getSex() != null ? user.getSex() : "");
      mDetailArea.setText(user.city != null ? user.city : "");
      mDetailEducational.setText(detail.education != null ? detail.education : "");
      mDetailHeight.setText(String.valueOf(detail.height) != null ? detail.height + "" : "");
      mDetailWeight.setText(String.valueOf(detail.weight) != null ? detail.weight + "" : "");
      mDetailMarry.setText(detail.getMarraied());
    }
  }
}
