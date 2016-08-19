package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Soldier;
import tech.honc.android.apps.soldier.utils.settings.SettingItems;
import tech.honc.android.apps.soldier.utils.toolsutils.DateFormat;

/**
 * Created by MrJiang on 4/17/2016.
 * 服役地区
 */
public class ServiceViewHolder extends BaseViewHolder {
  @Bind(R.id.detail_service_area) TextView mDetailServiceArea;
  @Bind(R.id.detail_service_soldier_area) TextView mDetailServiceSoldierArea;
  @Bind(R.id.detail_service_time_go) TextView mDetailServiceTimeGo;
  @Bind(R.id.detail_service_time_back) TextView mDetailServiceTimeBack;

  public ServiceViewHolder(Context context, ViewGroup parent) {
    super(context,
        LayoutInflater.from(context).inflate(R.layout.list_item_detail_service, parent, false));
    ButterKnife.bind(this, itemView);
  }

  @Override protected void bindTo(SettingItems settingItem) {
    super.bindTo(settingItem);
    final Soldier soldier = (Soldier) settingItem.mData;
    if (soldier != null) {
      mDetailServiceArea.setText(soldier.area);
      mDetailServiceSoldierArea.setText(soldier.region);
      mDetailServiceTimeGo.setText(
          soldier.years != null ? DateFormat.getRelativeTime(soldier.years) : "");
      mDetailServiceTimeBack.setText(
          soldier.enlistmentTime != null ? DateFormat.getRelativeTime(soldier.enlistmentTime) : "");
    }
  }
}
