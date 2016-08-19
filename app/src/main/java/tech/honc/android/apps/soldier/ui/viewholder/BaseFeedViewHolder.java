package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by MrJiang on 4/17/2016.
 * base  feed viewholder
 */
public class BaseFeedViewHolder extends RecyclerView.ViewHolder {

  protected Context context;

  public BaseFeedViewHolder(Context context, View itemView) {
    super(itemView);
    this.context = context;
  }

  public void initData(Object Data) {
  }
}
