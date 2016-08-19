package tech.honc.android.apps.soldier.feature.im.model;

import android.os.Parcel;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Author:　Create on 2016-5-24-0024 10:11 by Doublemine
 * Email:
 * Summary:　TODO
 * Update Date: 2016-5-24-0024 10:11 modify by Doublemine
 */
public class Contact extends Entity {
  public String avatar;
  public String appKey;
  public String nickName;
  public String userId;
  public char letterChar;

  public Contact() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.avatar);
    dest.writeString(this.appKey);
    dest.writeString(this.nickName);
    dest.writeString(this.userId);
    dest.writeInt(this.letterChar);
  }

  protected Contact(Parcel in) {
    this.avatar = in.readString();
    this.appKey = in.readString();
    this.nickName = in.readString();
    this.userId = in.readString();
    this.letterChar = (char) in.readInt();
  }

  public static final Creator<Contact> CREATOR = new Creator<Contact>() {
    @Override public Contact createFromParcel(Parcel source) {
      return new Contact(source);
    }

    @Override public Contact[] newArray(int size) {
      return new Contact[size];
    }
  };
}
