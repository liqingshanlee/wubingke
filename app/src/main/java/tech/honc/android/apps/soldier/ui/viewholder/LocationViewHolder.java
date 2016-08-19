package tech.honc.android.apps.soldier.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import tech.honc.android.apps.soldier.R;

/**
 * Created by kevin on 16-6-5.
 */
public class LocationViewHolder extends RecyclerView.ViewHolder {

  public TextView mTextView;

  public LocationViewHolder(View itemView) {
    super(itemView);
    mTextView = (TextView) itemView.findViewById(R.id.ui_city_picker_location);
  }
}
