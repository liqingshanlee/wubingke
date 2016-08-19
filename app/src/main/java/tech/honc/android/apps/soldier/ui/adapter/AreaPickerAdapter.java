package tech.honc.android.apps.soldier.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.ui.appInterface.OnItemClickListener;
import tech.honc.android.apps.soldier.ui.viewholder.CityPickerViewHolder;
import tech.honc.android.apps.soldier.utils.toolsutils.PinyinUtils;

/**
 * Created by Administrator on 2016/6/20.
 */
public class AreaPickerAdapter extends RecyclerView.Adapter<CityPickerViewHolder> {
  private Context mContext;
  private ArrayList<String> mCityDatas = new ArrayList<>();
  private OnItemClickListener mOnItemClickListener;

  @Override public CityPickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View viewAll =
        LayoutInflater.from(mContext).inflate(R.layout.list_item_all_city, parent, false);
    return new CityPickerViewHolder(mContext, viewAll);
  }

  @Override public void onBindViewHolder(CityPickerViewHolder holder,
      @SuppressLint("RecyclerView") final int position) {
    holder.mNameTextView.setText(mCityDatas.get(position));
    if (position < mCityDatas.size()) {
      if (position >= 1) {
        String oldLetter =
            PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(mCityDatas.get(position - 1)));
        String currentLetter =
            PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(mCityDatas.get(position)));
        if (currentLetter.equals(oldLetter)) {
          holder.mLetterTextView.setVisibility(View.GONE);
        } else {
          holder.mLetterTextView.setVisibility(View.VISIBLE);
          holder.mLetterTextView.setText(currentLetter);
        }
      } else {
        holder.mLetterTextView.setVisibility(View.VISIBLE);
        String currentLetter =
            PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(mCityDatas.get(position)));
        holder.mLetterTextView.setText(currentLetter);
      }
    }
    holder.mNameTextView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mOnItemClickListener.onItemClick(mCityDatas.get(position));
      }
    });
  }

  @Override public int getItemCount() {
    return mCityDatas.size();
  }

  public AreaPickerAdapter(Context context, List<String> citys) {
    this.mContext = context;
    mOnItemClickListener = (OnItemClickListener) context;
    mCityDatas = (ArrayList<String>) citys;
  }

  public ArrayList<String> getLetter() {
    ArrayList<String> cityStrings = new ArrayList<>();
    for (String c : mCityDatas) {
      String letter = PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(c));
      if (!cityStrings.contains(letter)) {
        cityStrings.add(letter);
      }
    }
    return cityStrings;
  }

  public int getPositionForSection(char section) {
    for (int index = 0; index < mCityDatas.size(); index++) {
      final String object = mCityDatas.get(index);
      if (object != null) {
        char firstChar = PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(object)).charAt(0);
        if (firstChar == section) {
          return index;
        }
      }
    }
    return RecyclerView.NO_POSITION;
  }
}
