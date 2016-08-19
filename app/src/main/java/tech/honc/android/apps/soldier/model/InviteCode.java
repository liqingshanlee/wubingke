package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Kevin on 2016/6/7.
 */
public class InviteCode extends Entity {

  public String title;
  public String url;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.title);
    dest.writeString(this.url);
  }

  public InviteCode() {
  }

  protected InviteCode(Parcel in) {
    this.title = in.readString();
    this.url = in.readString();
  }

  public static final Creator<InviteCode> CREATOR = new Creator<InviteCode>() {
    @Override public InviteCode createFromParcel(Parcel source) {
      return new InviteCode(source);
    }

    @Override public InviteCode[] newArray(int size) {
      return new InviteCode[size];
    }
  };
}
