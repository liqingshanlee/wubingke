package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.City;

/**
 * Created by MrJiang on 4/26/2016.
 */
public class CityCellViewHolder extends EasyViewHolder<City> {
  @Bind(R.id.tv_item_city_listview_letter) TextView mTvItemCityListviewLetter;
  @Bind(R.id.tv_item_city_listview_name) TextView mTvItemCityListviewName;
  ResultOnClickListener mResultOnClickListener;

  public CityCellViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.list_item_city_listview);
    ButterKnife.bind(this, itemView);
    setResultOnClickListener((ResultOnClickListener) context);
  }

  @Override public void bindTo(int position, final City value) {
    mTvItemCityListviewLetter.setText(value.sort);
    mTvItemCityListviewName.setText(value.name);
    mTvItemCityListviewName.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mResultOnClickListener.sendResult(value.name);
      }
    });
  }

  public interface ResultOnClickListener {
    void sendResult(String result);
  }

  public void setResultOnClickListener(ResultOnClickListener resultOnClickListener) {
    mResultOnClickListener = resultOnClickListener;
  }
}
