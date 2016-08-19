package tech.honc.android.apps.soldier.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import support.ui.adapters.EasyViewHolder;
import support.ui.adapters.debounced.DebouncedOnClickListener;
import tech.honc.android.apps.soldier.ui.viewholder.BaseViewHolder;
import tech.honc.android.apps.soldier.ui.viewholder.DynamicViewHolder;
import tech.honc.android.apps.soldier.ui.viewholder.InfoViewHolder;
import tech.honc.android.apps.soldier.ui.viewholder.PhotoCellViewHolder;
import tech.honc.android.apps.soldier.ui.viewholder.RelativeViewHolder;
import tech.honc.android.apps.soldier.ui.viewholder.ServiceViewHolder;
import tech.honc.android.apps.soldier.ui.viewholder.TextViewHolder;
import tech.honc.android.apps.soldier.utils.settings.SettingItems;

/**
 * Created by MrJiang on 4/17/2016.
 * 复用adapter
 */
public class DetailAdapter extends RecyclerView.Adapter<BaseViewHolder> {

  private ArrayList<SettingItems> mSettingItems;
  private Context mContext;
  private EasyViewHolder.OnItemClickListener itemClickListener;

  public DetailAdapter(Context context, ArrayList<SettingItems> settingItems) {
    this.mContext = context;
    this.mSettingItems = settingItems;
  }

  @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    BaseViewHolder baseViewHolder = null;
    switch (viewType) {
      case SettingItems.VIEW_TYPE_RELATIVE_CELL:
        baseViewHolder = new RelativeViewHolder(mContext, parent);
        break;
      case SettingItems.VIEW_TYPE_PHOTO_CELL:
        baseViewHolder = new PhotoCellViewHolder(mContext, parent);
        break;
      case SettingItems.VIEW_TYPE_DYNAMIC_CELL:
        baseViewHolder = new DynamicViewHolder(mContext, parent);
        break;
      case SettingItems.VIEW_TYPE_SIGNATURE_CELL:
        baseViewHolder = new TextViewHolder(mContext, parent);
        break;
      case SettingItems.VIEW_TYPE_SERVICE_CELL:
        baseViewHolder = new ServiceViewHolder(mContext, parent);
        break;
      case SettingItems.VIEW_TYPE_INFO_CELL:
        baseViewHolder = new InfoViewHolder(mContext, parent);
        break;
      case SettingItems.VIEW_TYPE_ACCOUNT_HEADER:
        break;
    }
    bindListeners(baseViewHolder);
    return baseViewHolder;
  }

  public void setOnClickListener(final EasyViewHolder.OnItemClickListener listener) {
    this.itemClickListener = new DebouncedOnClickListener() {
      @Override public boolean onDebouncedClick(View v, int position) {
        if (listener != null) {
          listener.onItemClick(position, v);
        }
        return true;
      }
    };
  }

  private void bindListeners(BaseViewHolder cellViewHolder) {
    if (cellViewHolder != null) {
      cellViewHolder.setItemClickListener(itemClickListener);
    }
  }

  @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
    SettingItems settingItems = mSettingItems.get(position);
    holder.bindView(settingItems);
  }

  @Override public int getItemCount() {
    return mSettingItems == null ? 0 : mSettingItems.size();
  }

  @Override public int getItemViewType(int position) {
    if (mSettingItems.size() != 0) {
      SettingItems settingItem = mSettingItems.get(position);
      return settingItem.itemViewType;
    }
    return 10;
  }
}
