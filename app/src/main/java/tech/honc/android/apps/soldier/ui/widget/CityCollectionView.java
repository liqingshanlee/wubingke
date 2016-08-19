package tech.honc.android.apps.soldier.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import java.util.List;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.City;

/**
 * class:CityCollectionView <br>
 * Description:CityCollectionView <br>
 * Creator: MrJiang <br>
 * Date: 4/25/2016 11:01 PM <br>
 */
// TODO: 2016/5/3 城市选择
public class CityCollectionView extends RecyclerView {
  private SimpleAdapter mAdapter;
  private int mItemSize;
  private CityOnClickListener mCityOnClickListener;

  public CityCollectionView(Context context) {
    this(context, null);
  }

  public CityCollectionView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CityCollectionView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initialize();
  }

  private void initialize() {
    setLayoutManager(new LinearLayoutManager(getContext()));
    addItemDecoration(
        new HorizontalDividerItemDecoration.Builder(getContext()).color(Color.TRANSPARENT)
            .size(1)
            .build());
    mAdapter = new SimpleAdapter();
    setAdapter(mAdapter);
  }

  // TODO: 4/17/2016  
  public void setData(ArrayList<City> data) {
    mAdapter.setData(data);
  }

  public void setmItemSize(int mItemSize) {
    this.mItemSize = mItemSize;
  }

  static class SimpleViewHolder extends ViewHolder {
    public City city;
    protected int itemSize;
    public static String mCityLetter = "";
    @Bind(R.id.tv_item_city_listview_letter) TextView mTvItemCityListviewLetter;
    @Bind(R.id.tv_item_city_listview_name) TextView mTvItemCityListviewName;

    private SimpleViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.list_item_city_listview, parent, false));
      ButterKnife.bind(this, itemView);
    }

    static SimpleViewHolder create(Context context, ViewGroup parent) {
      return new SimpleViewHolder(context, parent);
    }

    public void setItemSize(int itemSize) {
      this.itemSize = itemSize;
    }

    public void bind(City city) {
      this.city = city;
      setText(city, mTvItemCityListviewLetter, mTvItemCityListviewName);
    }

    /**
     * 在设置现实城市名，是否显示列表的字母
     */
    private void setText(City city, TextView mTvItemCityListviewLetter,
        TextView mTvItemCityListviewName) {
      if (city.sort.equals(mCityLetter)) {
        mTvItemCityListviewLetter.setVisibility(GONE);
      } else {
        mTvItemCityListviewLetter.setVisibility(VISIBLE);
        mTvItemCityListviewLetter.setText(city.sort);
      }
      mTvItemCityListviewName.setText(city.name);
      mCityLetter = city.sort;
    }
  }

  /**
   * RecyclerView适配器
   */
  private class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<City> mData;

    public SimpleAdapter() {
      mData = new ArrayList<>();
    }

    public void setData(ArrayList<City> data) {
      mData.clear();
      mData.addAll(data);
      notifyDataSetChanged();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return SimpleViewHolder.create(getContext(), parent);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
      super.onBindViewHolder(holder, position, payloads);
    }

    @Override public void onBindViewHolder(ViewHolder holder, final int position) {
      final City city = mData.get(position);
      final SimpleViewHolder viewHolder = (SimpleViewHolder) holder;
      viewHolder.setItemSize(mItemSize);
      viewHolder.bind(city);
      viewHolder.mTvItemCityListviewName.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          //点击城市
          mCityOnClickListener.sendCity(city.name);
        }
      });
    }

    @Override public int getItemCount() {
      return mData.size();
    }
  }

  public interface CityOnClickListener {
    void sendCity(String city);
  }

  public void setCityOnClickListener(CityOnClickListener cityOnClickListener) {
    mCityOnClickListener = cityOnClickListener;
  }

  // TODO: 16-6-4  这种写法只适用与此项目,城市列表中数据
  public int getPositionForSection(char section) {
    for (int index = 0; index < getItemCount(); index++) {
      final Object object = get(index);
      if (object instanceof City) {
        City city = (City) object;
        char firstChar = city.sort.charAt(0);
        if (firstChar == section) {
          return index;
        }
      }
    }
    return RecyclerView.NO_POSITION;
  }

  public int getItemCount() {
    return mAdapter.getItemCount();
  }

  public Object get(int position) {
    return mAdapter.mData.get(position);
  }

  @Override public void scrollToPosition(int position) {
    super.scrollToPosition(position);
  }
}
