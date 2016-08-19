package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Administrator on 2016/5/4.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class AccountCenter extends Entity
{
  public String avatar;
  public String nickname;
  public String city;
  public String level;
  public String gender;
  public String signature;
  public int age;
  public int followers;
  public int followings;
  public int helps;

  public AccountCenter() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.avatar);
    dest.writeString(this.nickname);
    dest.writeString(this.city);
    dest.writeString(this.level);
    dest.writeString(this.gender);
    dest.writeString(this.signature);
    dest.writeInt(this.age);
    dest.writeInt(this.followers);
    dest.writeInt(this.followings);
    dest.writeInt(this.helps);
  }

  protected AccountCenter(Parcel in) {
    this.avatar = in.readString();
    this.nickname = in.readString();
    this.city = in.readString();
    this.level = in.readString();
    this.gender = in.readString();
    this.signature = in.readString();
    this.age = in.readInt();
    this.followers = in.readInt();
    this.followings = in.readInt();
    this.helps = in.readInt();
  }

  public static final Creator<AccountCenter> CREATOR = new Creator<AccountCenter>()
  {
    @Override public AccountCenter createFromParcel(Parcel source) {
      return new AccountCenter(source);
    }

    @Override public AccountCenter[] newArray(int size) {
      return new AccountCenter[size];
    }
  };
}
