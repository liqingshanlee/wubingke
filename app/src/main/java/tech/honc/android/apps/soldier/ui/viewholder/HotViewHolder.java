package tech.honc.android.apps.soldier.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.ui.widget.WrapHeightGridView;

/**
 * Created by kevin on 16-6-5.
 */
public class HotViewHolder extends RecyclerView.ViewHolder {

  public WrapHeightGridView mGridView;

  public HotViewHolder(View itemView) {
    super(itemView);
    mGridView = (WrapHeightGridView) itemView.findViewById(R.id.ui_city_picker_grid);
  }
}
