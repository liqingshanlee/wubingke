package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.ui.adapter.HotCityGridAdapter;
import tech.honc.android.apps.soldier.ui.widget.WrapHeightGridView;
import tech.honc.android.apps.soldier.utils.settings.SettingItems;

/**
 * Created by MrJiang on 4/25/2016.
 */
public class HotCityViewHolder extends BaseViewHolder {
  @Bind(R.id.ui_city_picker_grid) WrapHeightGridView mGridview;
  private Context mContext;

  public HotCityViewHolder(Context context, ViewGroup parent) {
    super(context,
        LayoutInflater.from(context).inflate(R.layout.list_item_gridview, parent, false));
    mContext = context;
    ButterKnife.bind(this, itemView);
  }

  @Override protected void bindTo(SettingItems settingItem) {
    super.bindTo(settingItem);
    mGridview.setAdapter(new HotCityGridAdapter(mContext));
  }
}
