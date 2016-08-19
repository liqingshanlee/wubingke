package tech.honc.android.apps.soldier.feature.im.model;

import android.os.Parcel;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by MrJiang on 2016/5/30.
 */
public class IMProfileInfo extends Entity {

  public String userId;
  public String nick;
  public String icon;
  public String mobile;
  public String email;
  public String extra;
  public String appkey;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.userId);
    dest.writeString(this.nick);
    dest.writeString(this.icon);
    dest.writeString(this.mobile);
    dest.writeString(this.email);
    dest.writeString(this.extra);
    dest.writeString(this.appkey);
  }

  public IMProfileInfo() {
  }

  protected IMProfileInfo(Parcel in) {
    this.userId = in.readString();
    this.nick = in.readString();
    this.icon = in.readString();
    this.mobile = in.readString();
    this.email = in.readString();
    this.extra = in.readString();
    this.appkey = in.readString();
  }

  public static final Creator<IMProfileInfo> CREATOR = new Creator<IMProfileInfo>() {
    @Override public IMProfileInfo createFromParcel(Parcel source) {
      return new IMProfileInfo(source);
    }

    @Override public IMProfileInfo[] newArray(int size) {
      return new IMProfileInfo[size];
    }
  };
}
