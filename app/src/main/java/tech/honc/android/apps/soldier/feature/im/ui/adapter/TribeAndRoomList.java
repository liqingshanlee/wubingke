package tech.honc.android.apps.soldier.feature.im.ui.adapter;

import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangh on 2016-3-23-0023.
 */
public class TribeAndRoomList {
  private List<YWTribe> mTribeList;
  private List<YWTribe> mRoomList;

  public TribeAndRoomList(List<YWTribe> tribeList, List<YWTribe> roomList) {
    this.mTribeList = tribeList;
    this.mRoomList = roomList;
    if (mTribeList == null) {
      mTribeList = new ArrayList<YWTribe>();
    }
    if (mRoomList == null) {
      mRoomList = new ArrayList<YWTribe>();
    }
  }

  public int size() {
    return mTribeList.size() + mRoomList.size();
  }

  public Object getItem(int position) {
    if (position >= 0 && position < mTribeList.size()) {
      return mTribeList.get(position);
    } else if (position > mTribeList.size() - 1 && position < size()) {
      return mRoomList.get(position - mTribeList.size());
    } else {
      return null;
    }
  }

  public List<YWTribe> getRoomList() {
    return mRoomList;
  }

  public List<YWTribe> getTribeList() {
    return mTribeList;
  }
}
