package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Administrator on 2016/5/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Profile extends Entity
{
  public Integer id;
  public String mobile;
  public String avatar;
  public String nickname;
  public String signature;
  public String level;
  public String city;
  public String gender;
  public String role;
  public String geohash;
  @JsonProperty("open_im_id") public String openimid;
  @JsonProperty("open_im_password") public String openimpassword;
  public String status;
  @JsonProperty("account_detail") public AccountDetail accountdetail;
  public Soldier army;

  public Profile() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.mobile);
    dest.writeString(this.avatar);
    dest.writeString(this.nickname);
    dest.writeString(this.signature);
    dest.writeString(this.level);
    dest.writeString(this.city);
    dest.writeString(this.gender);
    dest.writeString(this.role);
    dest.writeString(this.geohash);
    dest.writeString(this.openimid);
    dest.writeString(this.openimpassword);
    dest.writeString(this.status);
    dest.writeParcelable(this.accountdetail, flags);
    dest.writeParcelable(this.army, flags);
  }

  protected Profile(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.mobile = in.readString();
    this.avatar = in.readString();
    this.nickname = in.readString();
    this.signature = in.readString();
    this.level = in.readString();
    this.city = in.readString();
    this.gender = in.readString();
    this.role = in.readString();
    this.geohash = in.readString();
    this.openimid = in.readString();
    this.openimpassword = in.readString();
    this.status = in.readString();
    this.accountdetail = in.readParcelable(AccountDetail.class.getClassLoader());
    this.army = in.readParcelable(Soldier.class.getClassLoader());
  }

  public static final Creator<Profile> CREATOR = new Creator<Profile>()
  {
    @Override public Profile createFromParcel(Parcel source) {
      return new Profile(source);
    }

    @Override public Profile[] newArray(int size) {
      return new Profile[size];
    }
  };
}
