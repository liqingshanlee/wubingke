package tech.honc.android.apps.soldier.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import tech.honc.android.apps.soldier.R;

/**
 * Created by Administrator on 2016/4/25.
 */
public class HotCityGridAdapter extends BaseAdapter {

  private Context mContext;
  private List<String> mCities;
  private CityClickListener mCityClickListener;

  public HotCityGridAdapter(Context context) {
    this.mContext = context;
    setCityClickListener((CityClickListener) mContext);
    mCities = new ArrayList<>();
    mCities.add("北京市");
    mCities.add("上海市");
    mCities.add("广州市");
    mCities.add("深圳市");
    mCities.add("杭州市");
    mCities.add("南京市");
    mCities.add("天津市");
    mCities.add("武汉市");
    mCities.add("重庆市");
  }

  @Override public int getCount() {
    return mCities == null ? 0 : mCities.size();
  }

  @Override public String getItem(int position) {
    return mCities == null ? null : mCities.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View view, ViewGroup parent) {
    HotCityViewHolder holder;
    if (view == null) {
      view = LayoutInflater.from(mContext)
          .inflate(R.layout.list_item_hot_city_gridview, parent, false);
      holder = new HotCityViewHolder();
      holder.name = (TextView) view.findViewById(R.id.ui_city_picker_hot_city);
      view.setTag(holder);
    } else {
      holder = (HotCityViewHolder) view.getTag();
    }
    final String city = mCities.get(position);
    holder.name.setText(city);
    holder.name.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mCityClickListener.cityOnClick(city);
      }
    });
    return view;
  }

  public static class HotCityViewHolder {
    TextView name;
  }

  public interface CityClickListener {
    void cityOnClick(String item);
  }

  public void setCityClickListener(CityClickListener cityClickListener) {
    mCityClickListener = cityClickListener;
  }
}
